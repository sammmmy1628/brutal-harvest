package com.christofmeg.brutalharvest.common.blockentity;

import com.christofmeg.brutalharvest.common.handler.PotStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Cooking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PotBlockEntity extends BlockEntity {

    private final IItemHandler itemHandler = new PotStackHandler();
    private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> this.itemHandler);
    private final IFluidHandler fluidHandler = new FluidTank(1000, fluidStack -> fluidStack.getFluid().isSame(Fluids.WATER) || Cooking.CookingItemsCache.isFluidValid(this.level, fluidStack.getFluid()));
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> this.fluidHandler);
    private int cookingProgress = 0;
    private int cooldown = 0;

    public PotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeRegistry.POT_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("cookingProgress", this.cookingProgress);
        pTag.putInt("cooldown", this.cooldown);
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> pTag.merge(((PotStackHandler) iItemHandler).serializeNBT()));
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(iFluidHandler -> ((FluidTank) iFluidHandler).writeToNBT(pTag));
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("cookingProgress")) {
            this.cookingProgress = pTag.getInt("cookingProgress");
        }
        if (pTag.contains("cooldown")) {
            this.cooldown = pTag.getInt("cooldown");
        }
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> ((PotStackHandler) iItemHandler).deserializeNBT(pTag));
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
        Optional<Cooking> optional = this.getCurrentRecipe();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
        optional.ifPresent(cooking -> this.cookingProgress = cooking.ticks());
    }

    private Optional<Cooking> getCurrentRecipe() {
        Optional<IItemHandler> itemHandlerOptional = this.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (itemHandlerOptional.isPresent() && this.level != null) {
            SimpleContainer container = ((PotStackHandler) itemHandlerOptional.get()).asContainer();
            return this.level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get(), container, this.level);
        }
        return Optional.empty();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        PotBlockEntity blockEntity = (PotBlockEntity) t;
        Optional<IItemHandler> optional = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        Optional<IFluidHandler> optional1 = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        if (blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress--;
        } else if (optional.isPresent() && optional1.isPresent() && ((PotStackHandler) optional.get()).isEmpty() && !level.isClientSide) {
            if (blockEntity.cooldown == 0) {
                IItemHandler iItemHandler = optional.get();
                blockEntity.getCurrentRecipe().ifPresent(cooking -> {
                    blockEntity.cooldown = 50;
                    if (cooking.isSufficient(iItemHandler)){
                        if (cooking.getResultItem(level.registryAccess()) != ItemStack.EMPTY) {
                            if (iItemHandler.getStackInSlot(0) == ItemStack.EMPTY ||
                                    iItemHandler.getStackInSlot(1).getItem() == cooking.getResultItem(level.registryAccess()).getItem()) {
                                ItemStack result = cooking.getResultItem(level.registryAccess());
                                int count = iItemHandler.getStackInSlot(0).getCount();
                                if (iItemHandler.getStackInSlot(0).getCount() + count + result.getCount() <= 64) {
                                    ((PotStackHandler) iItemHandler).updateLevel(level);
                                    iItemHandler.insertItem(0, cooking.assemble(iItemHandler), false);
                                }
                            }
                        }
                        FluidTank tank = (FluidTank) optional1.get();
                        tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        if (cooking.getResultFluid() != FluidStack.EMPTY && cooking.isSufficient(iItemHandler)) {
                            tank.fill(cooking.assembleFluid(iItemHandler, cooking.getResultItem(level.registryAccess()).isEmpty()), IFluidHandler.FluidAction.EXECUTE);
                        }
                        blockEntity.setChanged();
                    }
                });
            } else {
                blockEntity.cooldown--;
            }
        }
    }
}
