package com.christofmeg.brutalharvest.common.handler;

import com.christofmeg.brutalharvest.common.handler.base.BaseBrutalStackHandler;
import com.christofmeg.brutalharvest.common.recipe.custom.Cooking;
import net.minecraft.nbt.FloatTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PotStackHandler extends BaseBrutalStackHandler {

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
        if (slot == 0 && Cooking.CookingItemsCache.requiresContainer(this.level, stack.getItem())) {
            stack.getOrCreateTag();
            stack.addTagElement("inPot", FloatTag.valueOf(1.0F));
        }
    }

}
