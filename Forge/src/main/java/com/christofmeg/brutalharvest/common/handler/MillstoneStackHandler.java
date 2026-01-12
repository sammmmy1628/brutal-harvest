package com.christofmeg.brutalharvest.common.handler;

import com.christofmeg.brutalharvest.common.handler.base.BaseBrutalStackHandler;
import com.christofmeg.brutalharvest.common.recipe.custom.Milling;
import net.minecraft.nbt.FloatTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MillstoneStackHandler extends BaseBrutalStackHandler {

    private Level level;

    public MillstoneStackHandler() {
        super(7);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 0 || Milling.MillingItemsCache.isValid(level, stack.getItem());
    }

    public void updateLevel(Level level) {
        this.level = level;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        ItemStack stack = this.getStackInSlot(0);
        if (slot == 0 && (Milling.MillingItemsCache.requiresContainer(this.level, stack.getItem()))) {
            stack.getOrCreateTag();
            stack.addTagElement("inMillstone", FloatTag.valueOf(1.0F));
        }
    }
}
