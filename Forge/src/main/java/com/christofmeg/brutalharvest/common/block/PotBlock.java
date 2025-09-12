package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import com.christofmeg.brutalharvest.common.blockentity.PotBlockEntity;
import com.christofmeg.brutalharvest.common.handler.PotStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PotBlock extends BaseCookingBlock {

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 9, 15);
    private static final VoxelShape SHAPE_ON_CAMPFIRE = Block.box(0, 0, 0, 16, 16, 16);

    public PotBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PotBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        PotBlockEntity potBlockEntity = (PotBlockEntity) pLevel.getBlockEntity(pPos);
        ItemStack playerStack = pPlayer.getItemInHand(pHand);
        if (playerStack.is(Items.WATER_BUCKET) && pLevel.getBlockState(pPos).getValue(ON_CAMPFIRE) != OnCampfire.NONE && !pState.getValue(BaseCookingBlock.FILLED)) {
            if (!pPlayer.isCreative()) {
                playerStack.shrink(1);
            }
            pState = pState.setValue(FILLED, true);
            pLevel.setBlockAndUpdate(pPos, pState);
            pLevel.playSound(pPlayer, pPos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
            pPlayer.addItem(new ItemStack(Items.BUCKET, 1));
            return InteractionResult.SUCCESS;
        } else if (potBlockEntity != null) {
            Optional<IItemHandler> optional = potBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
            if (optional.isPresent() && !pLevel.isClientSide) {
                PotStackHandler stackHandler = (PotStackHandler) optional.get();
                ItemStack stack = stackHandler.getStackInSlot(0);
                ItemStack resultStack = stackHandler.getStackInSlot(1);
                stackHandler.updateLevel(pLevel);
                if (pPlayer.isShiftKeyDown() && stack != ItemStack.EMPTY) {
                    pPlayer.addItem(stack.copy());
                    stackHandler.extractItem(0, stack.getCount(), false);
                    potBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                } else if (resultStack != ItemStack.EMPTY) {
                    stackHandler.extractItem(1, resultStack.getCount(), false);
                    pPlayer.addItem(resultStack);
                    potBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                } else if (stack.getCount() < 6 && stackHandler.isItemValid(0, playerStack) && pState.getValue(BaseCookingBlock.FILLED)) {
                    if (!pPlayer.isCreative()) {
                        playerStack.shrink(1);
                    }
                    stackHandler.insertItem(0, new ItemStack(playerStack.getItem(), 1), false);
                    potBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return pState.getValue(ON_CAMPFIRE) == OnCampfire.NONE ? SHAPE : SHAPE_ON_CAMPFIRE;
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == BlockEntityTypeRegistry.POT_BLOCK_ENTITY.get() && !pLevel.isClientSide ? PotBlockEntity::tick : null;
    }
}
