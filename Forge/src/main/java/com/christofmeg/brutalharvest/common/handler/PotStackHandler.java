package com.christofmeg.brutalharvest.common.handler;

import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Cooking;
import net.minecraft.nbt.FloatTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class PotStackHandler extends ItemStackHandler {

    private Level level;

    public PotStackHandler() {
        super(7);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 0 && Cooking.CookingItemsCache.isResult(this.level, stack.getItem()) || Cooking.CookingItemsCache.isValid(this.level, stack.getItem());
    }

    public void updateLevel(Level level) {
        this.level = level;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot > 0 ? 6 : 64;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        ItemStack stack = this.getStackInSlot(0);
        if (slot == 0 && stack.is(ItemRegistry.PASTA.get())) {
            stack.getOrCreateTag();
            stack.addTagElement("inPot", FloatTag.valueOf(1.0F));
        }
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
        SimpleContainer container = new SimpleContainer(this.getSlots() - 1);
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
        return this.stacks.stream().anyMatch(stack -> !stack.isEmpty());
    }
}
