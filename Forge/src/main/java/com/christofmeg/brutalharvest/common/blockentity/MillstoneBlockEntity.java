package com.christofmeg.brutalharvest.common.blockentity;

import com.christofmeg.brutalharvest.common.handler.MillstoneStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Milling;
import com.christofmeg.brutalharvest.common.util.BrutalRecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class MillstoneBlockEntity extends BlockEntity implements GeoBlockEntity {

    private static final RawAnimation SPIN_ANIMATION = RawAnimation.begin().thenPlay("millstone.animation.spin");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final MillstoneStackHandler handler;
    private final FluidTank fluidHandler;
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional;
    private final LazyOptional<IItemHandler> itemHandlerLazyOptional;
    private int spins = 0;
    public short cooldown = 0;

    public MillstoneBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeRegistry.MILLSTONE_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.handler = new MillstoneStackHandler();
        this.fluidHandler = new FluidTank(1000, fluidStack -> Milling.MillingItemsCache.isFluidValid(this.level, fluidStack.getFluid()));
        this.fluidHandlerLazyOptional = LazyOptional.of(() -> this.fluidHandler);
        this.itemHandlerLazyOptional = LazyOptional.of(() -> this.handler);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("spins", this.spins);
        pTag.putShort("cooldown", this.cooldown);
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> pTag.merge(((MillstoneStackHandler) iItemHandler).serializeNBT()));
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(iFluidHandler -> ((FluidTank) iFluidHandler).writeToNBT(pTag));
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("spins")) {
            this.spins = pTag.getInt("spins");
        }
        if (pTag.contains("cooldown")) {
            this.cooldown = pTag.getShort("cooldown");
        }
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> ((MillstoneStackHandler) iItemHandler).deserializeNBT(pTag));
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(iFluidHandler -> ((FluidTank) iFluidHandler).readFromNBT(pTag));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHandlerLazyOptional.cast();
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.fluidHandlerLazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.fluidHandlerLazyOptional.invalidate();
        this.itemHandlerLazyOptional.invalidate();
    }

    public void craft() {
        Optional<Milling> milling = this.getCurrentRecipe();
        Optional<IItemHandler> itemHandler1 = this.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        Optional<IFluidHandler> fluidHandler1 = this.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        if (milling.isPresent() && itemHandler1.isPresent() && fluidHandler1.isPresent() && this.level != null) {
            Milling milling1 = milling.get();
            MillstoneStackHandler stackHandler = (MillstoneStackHandler) itemHandler1.get();
            if (BrutalRecipeUtils.isSufficient(stackHandler, milling1.ingredient())) {
                FluidTank fluidTank = (FluidTank) fluidHandler1.get();
                ItemStack resultStack = milling1.getResultItem(this.level.registryAccess());
                FluidStack fluidResult = milling1.getResultFluid();
                if ((stackHandler.getStackInSlot(0).isEmpty() || stackHandler.getStackInSlot(0).is(resultStack.getItem()) || resultStack.isEmpty())
                        && (fluidTank.getFluidInTank(0).isEmpty() || fluidTank.getFluidInTank(0).isFluidStackIdentical(fluidResult)) || fluidResult.isEmpty()) {
                    this.triggerAnim("spin_controller", "spin");
                    if (!this.level.isClientSide) {
                        if (this.spins == milling1.spins() - 1) {
                            int currentCount = handler.getStackInSlot(0).getCount();
                            short multiplier = BrutalRecipeUtils.getResultMultiplier(handler, milling1.ingredient());
                            if (currentCount + multiplier * milling1.itemOutput().getCount() <= 64) {
                                if (!milling1.remainder().isEmpty()) {
                                    BlockPos pos = this.getBlockPos();
                                    ItemStack remainderStack = milling1.remainder().copyWithCount(milling1.remainder().getCount() * multiplier);
                                    Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), remainderStack);
                                }
                                handler.updateLevel(this.level);
                                handler.insertItem(0, milling1.assemble(handler), false);
                                fluidTank.fill(fluidResult, IFluidHandler.FluidAction.EXECUTE);
                            }
                            this.spins = 0;
                        } else {
                            this.spins++;
                        }
                        this.cooldown = 50;
                        this.setChanged();
                    }
                }
            }
        }
    }

    private Optional<Milling> getCurrentRecipe() {
        Optional<IItemHandler> itemHandlerOptional = this.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (itemHandlerOptional.isPresent() && this.level != null && !this.level.isClientSide && this.cooldown == 0) {
            SimpleContainer container = ((MillstoneStackHandler) itemHandlerOptional.get()).asContainer();
            return this.level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.MILLING_RECIPE_TYPE.get(), container, this.level);
        }
        return Optional.empty();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoBlockEntity>(this, "spin_controller", testAnim -> PlayState.STOP)
                .triggerableAnim("spin", SPIN_ANIMATION).setSoundKeyframeHandler(state -> {
                    Level level = ClientUtils.getLevel();
                    if (level != null) {
                        level.playLocalSound(this.getBlockPos(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                    }
                }).setParticleKeyframeHandler(particleKeyframeEvent -> {
                    Level level1 = ClientUtils.getLevel();
                    BlockPos pos = this.getBlockPos();
                    this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
                        if (!iItemHandler.getStackInSlot(1).isEmpty()) {
                            for (int i = 0; i < (int) (Math.random() * 10) + 10; i++) {
                                level1.addParticle(new ItemParticleOption(ParticleTypes.ITEM, iItemHandler.getStackInSlot(1)),
                                        pos.getX() + 0.5F, pos.getY() + 0.375F, pos.getZ() + 0.5F, 0.2F * (Math.random() - 0.5F), 0.3F * Math.random(), 0.2F * (Math.random() - 0.5F));
                            }
                        }
                    });
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    @SuppressWarnings("unused")
    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        MillstoneBlockEntity blockEntity = (MillstoneBlockEntity) t;
        if (blockEntity.cooldown > 0 && !level.isClientSide) {
            blockEntity.cooldown--;
        }
    }
}
