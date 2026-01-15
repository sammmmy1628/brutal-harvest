package com.christofmeg.brutalharvest.common.blockentity;

import com.christofmeg.brutalharvest.common.handler.PanStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.christofmeg.brutalharvest.common.init.SoundRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Frying;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PanBlockEntity extends BlockEntity {

    private final IItemHandler itemHandler = new PanStackHandler();
    private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> this.itemHandler);
    private final IFluidHandler fluidHandler = new FluidTank(250, fluidStack -> Frying.FryingItemsCache.isValidFluid(this.level, fluidStack.getFluid()));
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> this.fluidHandler);
    private int fryingProgress = 0;
    private int cooldown = 0;

    public PanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeRegistry.PAN_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("fryingProgress", this.fryingProgress);
        pTag.putInt("cooldown", this.cooldown);
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> pTag.merge(((PanStackHandler) iItemHandler).serializeNBT()));
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(iFluidHandler -> ((FluidTank) iFluidHandler).writeToNBT(pTag));
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("fryingProgress")) {
            this.fryingProgress = pTag.getInt("fryingProgress");
        }
        if (pTag.contains("cooldown")) {
            this.cooldown = pTag.getInt("cooldown");
        }
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> ((PanStackHandler) iItemHandler).deserializeNBT(pTag));
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
        this.itemHandlerLazyOptional.invalidate();
        this.fluidHandlerLazyOptional.invalidate();
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
        Optional<Frying> optional = this.getCurrentRecipe();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
        optional.ifPresent(frying -> this.fryingProgress = frying.ticks());
    }

    private Optional<Frying> getCurrentRecipe() {
        Optional<IItemHandler> itemHandlerOptional = this.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        Optional<IFluidHandler> fluidHandlerOptional = this.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        if (itemHandlerOptional.isPresent() && fluidHandlerOptional.isPresent() && this.level != null && !this.level.isClientSide) {
            SimpleContainer container = new SimpleContainer(itemHandlerOptional.get().getStackInSlot(1));
            return this.level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get()).stream().filter(
                    recipe -> recipe.matches(container, this.level) && (recipe.fluidIngredient().isEmpty()
                            || recipe.fluidIngredient().isFluidStackIdentical(((FluidTank) fluidHandlerOptional.get()).getFluid()))
            ).findFirst();
        }
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        PanBlockEntity blockEntity = (PanBlockEntity) t;
        Optional<IItemHandler> optional = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        Optional<IFluidHandler> optional1 = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        if (blockEntity.fryingProgress > 0) {
            blockEntity.fryingProgress--;
            if (blockEntity.fryingProgress % 5 == 0) {
                level.playSound(null, pos, SoundRegistry.PAN_FRYING.get(), SoundSource.BLOCKS, 0.2F, 1.0F);
            }
        } else if (optional.isPresent() && optional1.isPresent() && !level.isClientSide) {
            if (blockEntity.cooldown % 40 == 0) {
                IItemHandler iItemHandler = optional.get();
                blockEntity.cooldown = 200;
                blockEntity.getCurrentRecipe().ifPresent(frying -> {
                    if (!frying.ingredient().isEmpty() && frying.ingredient().getItems()[0].getCount() <= iItemHandler.getStackInSlot(1).getCount()) {
                        if (iItemHandler.getStackInSlot(0).isEmpty()) {
                            ItemStack result = frying.getResultItem(level.registryAccess());
                            int count = !frying.ingredient().isEmpty() ? iItemHandler.getStackInSlot(1).getCount() / frying.ingredient().getItems()[0].getCount() : 1;
                            if (iItemHandler.getStackInSlot(0).getCount() + count * result.getCount() <= 64) {
                                ((PanStackHandler) iItemHandler).updateLevel(level);
                                iItemHandler.insertItem(0, frying.assemble(new SimpleContainer(iItemHandler.getStackInSlot(1)), level.registryAccess()), false);
                            }
                        }
                        optional1.get().drain(250, IFluidHandler.FluidAction.EXECUTE);
                        level.playSound(null, pos, SoundRegistry.PAN_FRYING.get(), SoundSource.BLOCKS, 2.0F, 1.5F);
                        blockEntity.setChanged();
                    }
                });
            } else {
                blockEntity.cooldown--;
            }
        }
    }
}
