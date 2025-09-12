package com.christofmeg.brutalharvest.common.item;

import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CookingBlockItem extends BlockItem {

    public CookingBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos placePos = pContext.getClickedPos().below();
        BlockState clicked = level.getBlockState(placePos);
        Block block = this.getBlock();
        BlockState toPlace = Objects.requireNonNull(block.getStateForPlacement(pContext));
        if (clicked.is(Blocks.CAMPFIRE)) {
            toPlace = toPlace.setValue(BaseCookingBlock.ON_CAMPFIRE, BaseCookingBlock.OnCampfire.CAMPFIRE);
            level.setBlock(placePos, toPlace, Block.UPDATE_ALL);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (clicked.is(Blocks.SOUL_CAMPFIRE)) {
            toPlace = toPlace.setValue(BaseCookingBlock.ON_CAMPFIRE, BaseCookingBlock.OnCampfire.SOUL_CAMPFIRE);
            level.setBlock(placePos, toPlace, Block.UPDATE_ALL);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.place(pContext);
        }
    }
}
