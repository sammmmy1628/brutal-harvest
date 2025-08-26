package com.christofmeg.brutalharvest.common.menu;

import com.christofmeg.brutalharvest.common.handler.SeedStackHandler;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.init.MenuRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SeedSatchelMenu extends AbstractContainerMenu {

    public SeedSatchelMenu(int pContainerId, Inventory inventory) {
        this(pContainerId, inventory, ItemStack.EMPTY);
    }

    public SeedSatchelMenu(int pContainerId, Inventory inventory, ItemStack stack) {
        super(MenuRegistry.SEED_SATCHEL_MENU_TYPE.get(), pContainerId);
        SeedStackHandler handler = new SeedStackHandler(stack);
        Optional<IItemHandler> optional = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (optional.isPresent()) {
            handler = (SeedStackHandler) optional.get();
        }
        for(int $$9 = 0; $$9 < 3; ++$$9) {
            for(int $$8 = 0; $$8 < 9; ++$$8) {
                this.addSlot(new SlotItemHandler(handler, $$8 + $$9 * 9, 8 + $$8 * 18, 18 + $$9 * 18));
            }
        }

        for(int $$9 = 0; $$9 < 3; ++$$9) {
            for(int $$8 = 0; $$8 < 9; ++$$8) {
                this.addSlot(new Slot(inventory, $$8 + $$9 * 9 + 9, 8 + $$8 * 18, 84 + $$9 * 18));
            }
        }

        for(int $$9 = 0; $$9 < 9; ++$$9) {
            this.addSlot(new Slot(inventory, $$9, 8 + $$9 * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.getSlot(i);
        if (slot.hasItem()) {
            stack = slot.getItem();
            if (i < 27) {
                if (!this.moveItemStackTo(stack, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 27, false) || !slot.mayPlace(stack)) {
                return ItemStack.EMPTY;
            }
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return stack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.SEED_SATCHEL.get());
    }
}
