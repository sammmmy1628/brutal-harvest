package com.christofmeg.brutalharvest.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WildCropBlock extends BushBlock {

    private final VoxelShape shape;

    public WildCropBlock(VoxelShape shape) {
        super(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS));
        this.shape = shape;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState pState, @NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
        BlockState blockBelow = pLevel.getBlockState(pPos.below());
        return blockBelow.is(Blocks.DIRT) || blockBelow.is(Blocks.GRASS_BLOCK) || blockBelow.is(Blocks.PODZOL)
                || blockBelow.is(Blocks.SAND) || blockBelow.is(Blocks.RED_SAND);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return this.shape;
    }
}
