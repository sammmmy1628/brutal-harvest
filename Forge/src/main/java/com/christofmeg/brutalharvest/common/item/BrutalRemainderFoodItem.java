package com.christofmeg.brutalharvest.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BrutalRemainderFoodItem extends Item {

    private final Item remainder;

    public BrutalRemainderFoodItem(Properties pProperties, Item remainder) {
        super(pProperties);
        this.remainder = remainder;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof Player player && !player.isCreative()) {
            player.addItem(new ItemStack(this.remainder, 1));
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}
