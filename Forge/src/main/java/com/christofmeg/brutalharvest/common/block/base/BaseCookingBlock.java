package com.christofmeg.brutalharvest.common.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseCookingBlock extends BaseEntityBlock {

    public static final Property<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<OnCampfire> ON_CAMPFIRE = EnumProperty.create("on_campfire", OnCampfire.class);

    protected BaseCookingBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(ON_CAMPFIRE, OnCampfire.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(ON_CAMPFIRE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState blockState = this.defaultBlockState();
        BlockState clickedBlock = pContext.getLevel().getBlockState(pContext.getClickedPos());
        blockState = blockState.setValue(FACING, pContext.getHorizontalDirection());
        if (clickedBlock.is(Blocks.CAMPFIRE)) {
            blockState = blockState.setValue(ON_CAMPFIRE, OnCampfire.CAMPFIRE);
        } else if (clickedBlock.is(Blocks.SOUL_CAMPFIRE)) {
            blockState = blockState.setValue(ON_CAMPFIRE, OnCampfire.SOUL_CAMPFIRE);
        } else {
            blockState = blockState.setValue(ON_CAMPFIRE, OnCampfire.NONE);
        }
        return blockState;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pMovedByPiston) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity != null) {
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler ->
                    Containers.dropContents(pLevel, pPos, new SimpleContainer(iItemHandler.getStackInSlot(0), iItemHandler.getStackInSlot(1))));
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    public enum OnCampfire implements StringRepresentable {
        NONE("none", null),
        CAMPFIRE("campfire", Blocks.CAMPFIRE),
        SOUL_CAMPFIRE("soul_campfire", Blocks.SOUL_CAMPFIRE);

        private final String name;
        private final Block block;

        OnCampfire(String name, @Nullable Block block) {
            this.name = name;
            this.block = block;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public Block getBlock() {
            return this.block;
        }
    }
}
