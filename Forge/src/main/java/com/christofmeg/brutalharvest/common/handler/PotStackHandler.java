package com.christofmeg.brutalharvest.common.handler;

import com.christofmeg.brutalharvest.common.recipe.custom.Cooking;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class PotStackHandler extends ItemStackHandler {

    private Level level;

    public PotStackHandler() {
        super(2);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 1 || Cooking.CookingItemsCache.isValid(this.level, stack.getItem());
    }

    public void updateLevel(Level level) {
        this.level = level;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot == 0 ? 6 : 64;
    }
}
