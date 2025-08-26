package com.christofmeg.brutalharvest.common.provider;

import com.christofmeg.brutalharvest.common.handler.SeedStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SeedSatchelCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    private final SeedStackHandler handler;
    private final LazyOptional<IItemHandler> optional;

    public SeedSatchelCapabilityProvider(ItemStack stack) {
        this.handler = new SeedStackHandler(stack);
        this.optional = LazyOptional.of(() -> handler);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(capability, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.handler.deserializeNBT(tag);
    }
}
