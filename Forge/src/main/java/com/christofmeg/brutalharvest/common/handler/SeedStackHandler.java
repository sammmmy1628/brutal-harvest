package com.christofmeg.brutalharvest.common.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SeedStackHandler extends ItemStackHandler {

    private final ItemStack stack;

    public SeedStackHandler(ItemStack stack) {
        super(27);
        this.stack = stack;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return super.isItemValid(slot, stack) && stack.is(Tags.Items.SEEDS);
    }

    @Override
    protected void onContentsChanged(int slot) {
        CompoundTag tag = this.stack.getTag();
        if (tag != null) {
            tag.putFloat("isEmpty", this.stacks.stream().noneMatch(stack -> stack != ItemStack.EMPTY) ? 0.0F : 1.0F);
            this.stack.save(tag);
        }
    }

    @Override
    protected void onLoad() {
        CompoundTag tag = this.stack.getTag();
        if (tag == null) {
            this.stack.addTagElement("isEmpty", FloatTag.valueOf(this.stacks.stream().noneMatch(stack -> stack != ItemStack.EMPTY) ? 0.0F : 1.0F));
        }
    }
}
