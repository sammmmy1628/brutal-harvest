package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.init.TagRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Cutting;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CuttingBoardBlock extends Block {

    private static final VoxelShape[] SHAPES = {
            Block.box(1, 0, 4, 15, 1, 12),
            Block.box(4, 0, 1, 12, 1, 15)
    };
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public CuttingBoardBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        ItemStack playerStackMain = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack playerStackOff = pPlayer.getItemInHand(InteractionHand.OFF_HAND);
        if (playerStackMain.is(TagRegistry.Items.KNIVES) && Cutting.CuttingItemsCache.isValid(pLevel, playerStackOff.getItem())) {
            pPlayer.playSound(SoundEvents.PLAYER_ATTACK_SWEEP);
            if (!pLevel.isClientSide) {
                if (!pPlayer.isCreative()) {
                    if (playerStackMain.isDamageableItem()) {
                        playerStackMain.getItem().damageItem(playerStackMain, 1, pPlayer, player -> player.playSound(SoundEvents.ITEM_BREAK));
                    }
                    playerStackOff.shrink(1);
                }
                pPlayer.addItem(Cutting.CuttingItemsCache.getCuttingResult(pLevel, playerStackOff.getItem()));
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPES[pState.getValue(FACING).get2DDataValue() % 2];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }
}
