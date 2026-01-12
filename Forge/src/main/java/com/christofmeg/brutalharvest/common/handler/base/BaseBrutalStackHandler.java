package com.christofmeg.brutalharvest.common.handler.base;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public abstract class BaseBrutalStackHandler extends ItemStackHandler {

    protected BaseBrutalStackHandler(int size) {
        super(size);
    }

    public int getFirstMatchingSlot(Item matching) {
        for (int iter = 1; iter < this.getSlots(); iter++) {
            ItemStack stack = this.getStackInSlot(iter);
            if (stack.is(matching)) {
                if (stack.getCount() == this.getSlotLimit(iter)) {
                    return -1;
                }
                return iter;
            }
        }
        for (int iter = 1; iter < this.getSlots(); iter++) {
            if (this.getStackInSlot(iter).isEmpty()) {
                return iter;
            }
        }
        return -1;
    }

    public SimpleContainer asContainer() {
        SimpleContainer container = new SimpleContainer(this.getSlots() - 1 - this.countEmptySlots());
        ItemStack stack;
        for (int iter = 1; iter < this.getSlots(); iter++) {
            stack = this.getStackInSlot(iter);
            if (!stack.isEmpty()) {
                container.addItem(stack);
            }
        }
        return container;
    }

    public boolean isEmpty() {
        return this.stacks.stream().allMatch(ItemStack::isEmpty);
    }

    private byte countEmptySlots() {
        byte count = 0;
        for (byte iter = 1; iter < this.getSlots(); iter++) {
            if (this.getStackInSlot(iter).isEmpty()) {
                count++;
            }
        }
        return count;
    }
}
