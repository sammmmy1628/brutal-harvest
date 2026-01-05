package com.christofmeg.brutalharvest.common.blockentity;

import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.FluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RubberCauldronBlockEntity extends BlockEntity {

    private final FluidTank tank = new FluidTank(1000, fluidStack -> fluidStack.getFluid().isSame(FluidRegistry.SOURCE_RUBBER.get()));
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> this.tank);

    public RubberCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeRegistry.RUBBER_CAULDRON_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().ifPresent(iFluidHandler -> ((FluidTank) iFluidHandler).writeToNBT(pTag));
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().ifPresent(iFluidHandler -> ((FluidTank) iFluidHandler).readFromNBT(pTag));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap.equals(ForgeCapabilities.FLUID_HANDLER)) {
            return this.fluidHandlerLazyOptional.cast();
        }
        return LazyOptional.empty();
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
    public void setChanged() {
        super.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.fluidHandlerLazyOptional.invalidate();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
