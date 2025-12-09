package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import com.christofmeg.brutalharvest.common.blockentity.PotBlockEntity;
import com.christofmeg.brutalharvest.common.handler.PotStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.FluidRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
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
        if (potBlockEntity != null) {
            Optional<IItemHandler> optional = potBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
            Optional<IFluidHandler> optional1 = potBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
            if (optional.isPresent()) {
                PotStackHandler stackHandler = (PotStackHandler) optional.get();
                ItemStack resultStack = stackHandler.getStackInSlot(0);
                stackHandler.updateLevel(pLevel);
                Item requiredItem = Items.AIR;
                int slot = stackHandler.getFirstMatchingSlot(playerStack.getItem());
                if (!resultStack.isEmpty()) {
                    CompoundTag tag = resultStack.getTag();
                    byte extracted = (byte) resultStack.getCount();
                    if (tag != null) {
                        if (tag.contains("container")) {
                            requiredItem = Containers.byName(tag.getString("container")).getItem();
                        }
                        if (playerStack.is(requiredItem)) {
                            if (!pPlayer.isCreative()){
                                if (playerStack.getCount() < resultStack.getCount()) {
                                    extracted = (byte) playerStack.getCount();
                                }
                                if (requiredItem != Items.AIR) {
                                    playerStack.shrink(extracted);
                                }
                            }
                            if (tag.contains("inPot")) {
                                tag.putFloat("inPot", 0.0F);
                            }
                            tag.remove("container");
                            if (!pLevel.isClientSide) {
                                stackHandler.extractItem(1, extracted, false);
                                pPlayer.addItem(resultStack);
                                potBlockEntity.setChanged();
                            }
                        } else {
                            return InteractionResult.PASS;
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else if (pPlayer.isShiftKeyDown()) {
                    if (!pLevel.isClientSide) {
                        for (int iter = 1; iter < 7; iter++) {
                            ItemStack stack = stackHandler.getStackInSlot(iter);
                            if (!stack.isEmpty()) {
                                pPlayer.addItem(stack);
                                stackHandler.extractItem(iter, stack.getCount(), false);
                            }
                        }
                        potBlockEntity.setChanged();
                    }
                    return InteractionResult.SUCCESS;
                } else if (optional1.isPresent()) {
                    FluidTank tank = (FluidTank) optional1.get();
                    if (!tank.getFluid().isEmpty() && !tank.getFluid().getFluid().isSame(Fluids.WATER)) {
                        if (!pLevel.isClientSide) {
                            if (playerStack.is(Items.BUCKET)) {
                                FluidUtil.tryFillContainer(playerStack, tank, 1000, pPlayer, true);
                            } else if (tank.getFluid().getFluid().isSame(FluidRegistry.SOURCE_COFFEE.get())) {
                                tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                                pPlayer.addItem(new ItemStack(ItemRegistry.COFFEE_BOTTLE.get(), 1));
                                potBlockEntity.setChanged();
                            }
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (playerStack.is(Items.WATER_BUCKET) && pLevel.getBlockState(pPos).getValue(ON_CAMPFIRE) != OnCampfire.NONE && tank.isEmpty()) {
                        if (!pLevel.isClientSide) {
                            if (!pPlayer.isCreative()) {
                                playerStack.shrink(1);
                            }
                            tank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                            pLevel.setBlockAndUpdate(pPos, pState);
                            pLevel.playSound(null, pPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                            pPlayer.addItem(new ItemStack(Items.BUCKET, 1));
                            potBlockEntity.setChanged();
                            }
                        return InteractionResult.sidedSuccess(pLevel.isClientSide);
                    } if (slot != -1 && stackHandler.isItemValid(slot, playerStack) && tank.getFluid().getFluid().isSame(Fluids.WATER) && tank.getFluid().getAmount() == 1000) {
                        if (!pLevel.isClientSide) {
                            stackHandler.insertItem(slot, playerStack.copyWithCount(1), false);
                            if (!pPlayer.isCreative()) {
                                playerStack.shrink(1);
                            }
                            potBlockEntity.setChanged();
                        }
                        return InteractionResult.sidedSuccess(pLevel.isClientSide);
                    }
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
