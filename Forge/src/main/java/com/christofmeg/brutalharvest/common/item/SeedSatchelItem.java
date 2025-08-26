package com.christofmeg.brutalharvest.common.item;

import com.christofmeg.brutalharvest.common.menu.SeedSatchelMenu;
import com.christofmeg.brutalharvest.common.provider.SeedSatchelCapabilityProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SeedSatchelItem extends Item {

    public SeedSatchelItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        pPlayer.openMenu(new SimpleMenuProvider((containerId, inventory, player) -> new SeedSatchelMenu(
                containerId, inventory, stack), Component.translatable("container.seedSatchel")));
        return InteractionResultHolder.success(stack);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new SeedSatchelCapabilityProvider(stack);
    }
}
