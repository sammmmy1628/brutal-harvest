package com.christofmeg.brutalharvest.common.handler;

import com.christofmeg.brutalharvest.common.recipe.custom.Frying;
import net.minecraft.nbt.FloatTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class PanStackHandler extends ItemStackHandler {

    private Level level;

    public PanStackHandler() {
        super(2);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 0 && Frying.FryingItemsCache.isResult(this.level, stack.getItem()) || Frying.FryingItemsCache.isValid(this.level, stack.getItem());
    }

    public void updateLevel(Level level) {
        this.level = level;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot == 1 ? 6 : 64;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        ItemStack stack = this.getStackInSlot(0);
        if (slot == 0 && (Frying.FryingItemsCache.requiresContainer(this.level, stack.getItem()))) {
            stack.getOrCreateTag();
            stack.addTagElement("onPan", FloatTag.valueOf(1.0F));
        }
    }
}
