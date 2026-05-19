package com.christofmeg.brutalharvest.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BrutalCakeBlock extends CakeBlock {

    private final VoxelShape[] shapes;
    private final int nutrition;
    private final float saturation;

    public BrutalCakeBlock(Properties pProperties, int height, int nutrition, float saturation) {
        super(pProperties);
        this.shapes = new VoxelShape[]{Block.box(1.0, 0.0, 1.0, 15.0, height, 15.0), Block.box(3.0, 0.0, 1.0, 15.0, height, 15.0), Block.box(5.0, 0.0, 1.0, 15.0, height, 15.0), Block.box(7.0, 0.0, 1.0, 15.0, height, 15.0), Block.box(9.0, 0.0, 1.0, 15.0, height, 15.0), Block.box(11.0, 0.0, 1.0, 15.0, height, 15.0), Block.box(13.0, 0.0, 1.0, 15.0, height, 15.0)};
        this.nutrition = nutrition;
        this.saturation = saturation;
    }

    protected InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(this.nutrition, this.saturation);
            int $$4 = state.getValue(BITES);
            level.gameEvent(player, GameEvent.EAT, pos);
            if ($$4 < 6) {
                level.setBlock(pos, state.setValue(BITES, $$4 + 1), 3);
            } else {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return this.shapes[pState.getValue(CakeBlock.BITES)];
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            if (eat(pLevel, pPos, pState, pPlayer).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (pPlayer.getItemInHand(pHand).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return this.eat(pLevel, pPos, pState, pPlayer);
    }
}
