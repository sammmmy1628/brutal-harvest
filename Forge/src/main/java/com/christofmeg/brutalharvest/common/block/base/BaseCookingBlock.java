package com.christofmeg.brutalharvest.common.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseCookingBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<OnCampfire> ON_CAMPFIRE = EnumProperty.create("on_campfire", OnCampfire.class);
    public static final DirectionProperty CAMPFIRE_FACING = DirectionProperty.create("campfire_facing", Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST);
    protected String itemPropertyName;

    protected BaseCookingBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(ON_CAMPFIRE, OnCampfire.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING)
                .add(ON_CAMPFIRE)
                .add(CAMPFIRE_FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState blockState = this.defaultBlockState();
        blockState = blockState.setValue(FACING, pContext.getHorizontalDirection());
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
                    dropValidContents(pLevel, pPos, iItemHandler, this.itemPropertyName));
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if (pState.getValue(ON_CAMPFIRE) != OnCampfire.NONE && pRandom.nextInt(10) == 0) {
            pLevel.playLocalSound((double)pPos.getX() + 0.5, (double)pPos.getY() + 0.5, (double)pPos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.6F, false);
        }
    }

    protected static void dropValidContents(Level level, BlockPos pos, IItemHandler inventory, String propertyName) {
        for(int $$5 = 0; $$5 < inventory.getSlots(); ++$$5) {
            ItemStack stack = inventory.getStackInSlot($$5);
            CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains(propertyName)) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    public enum OnCampfire implements StringRepresentable {
        NONE("none", null, 0),
        CAMPFIRE("campfire", Blocks.CAMPFIRE, 15),
        SOUL_CAMPFIRE("soul_campfire", Blocks.SOUL_CAMPFIRE, 10);

        private final String name;
        private final Block block;
        private final int light;

        OnCampfire(String name, @Nullable Block block, int light) {
            this.name = name;
            this.block = block;
            this.light = light;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public Block getBlock() {
            return this.block;
        }

        public int getLight() {return this.light;}
    }
}
