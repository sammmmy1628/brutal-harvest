package com.christofmeg.brutalharvest.common.handler;

import com.christofmeg.brutalharvest.common.recipe.custom.Milling;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class MillstoneStackHandler extends ItemStackHandler {

    private Level level;

    public MillstoneStackHandler() {
        super(2);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 1 || Milling.MillingItemsCache.isValid(level, stack.getItem());
    }

    public void updateLevel(Level level) {
        this.level = level;
    }
}
