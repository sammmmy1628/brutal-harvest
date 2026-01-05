package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.block.base.BaseCropBlock;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.item.KnifeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CoffeeCropBlock extends BaseCropBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 9);
    private static final VoxelShape[] SHAPE_BY_AGE = {
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 5.0F, 12.0F),
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 5.0F, 12.0F),
            Block.box(3.0F, 0.0F, 3.0F, 13.0F, 11.0F, 13.0F),
            Block.box(3.0F, 0.0F, 3.0F, 13.0F, 13.0F, 13.0F),
            Block.box(1.0F, 0.0F, 1.0F, 15.0F, 12.0F, 15.0F),
            Block.box(1.0F, 0.0F, 1.0F, 15.0F, 15.0F, 15.0F),
            Block.box(1.0F, 0.0F, 1.0F, 15.0F, 15.0F, 15.0F),
            Block.box(1.0F, 0.0F, 1.0F, 15.0F, 15.0F, 15.0F),
            Block.box(1.0F, 0.0F, 1.0F, 15.0F, 15.0F, 15.0F),
            Block.box(1.0F, 0.0F, 1.0F, 15.0F, 15.0F, 15.0F)
    };


    public CoffeeCropBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxAge() {
        return 8;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ItemRegistry.COFFEE_BEANS.get();
    }

    @Override
    protected ItemStack getBaseItemStack() {
        return ItemRegistry.COFFEE_CHERRY.get().getDefaultInstance();
    }

    @Override
    protected int getAgeAfterKnife() {
        return 4;
    }

    @Override
    protected @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_BY_AGE[this.getAge(pState)];
    }

    @Override
    public VoxelShape getShapeForAge(int age) {
        return SHAPE_BY_AGE[age];
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        int age = this.getAge(state);
        boolean matureAge = age == this.getMaxAge();
        boolean deadAge = age == this.getMaxAge() + 1;
        ItemStack stack = player.getItemInHand(interactionHand);
        if (stack.getItem() instanceof KnifeItem && !level.isClientSide) {
            if (!matureAge && !deadAge) {
                return InteractionResult.sidedSuccess(false);
            }
            if (matureAge) {
                state = this.getStateForAge(getAgeAfterKnife());
                popResource(level, pos, this.getBaseItemStack());
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            } else {
                level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                state = Blocks.AIR.defaultBlockState();
                int random = level.random.nextInt(2);
                if (random == 0) {
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                    popResource(level, pos, new ItemStack(level.random.nextInt(4) == 0 ? this.getBaseSeedId() : Items.STICK, level.random.nextInt(2)));
                }
            }
            level.setBlock(pos, state, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
            stack.hurtAndBreak(1, player, (livingEntity) -> livingEntity.broadcastBreakEvent(interactionHand));
            return InteractionResult.sidedSuccess(false);
        }
        return super.use(state, level, pos, player, interactionHand, blockHitResult);
    }
}
