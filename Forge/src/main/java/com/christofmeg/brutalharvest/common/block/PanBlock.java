package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import com.christofmeg.brutalharvest.common.blockentity.PanBlockEntity;
import com.christofmeg.brutalharvest.common.handler.PanStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.FluidRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.BrutalContainers;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PanBlock extends BaseCookingBlock {

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 3, 16);
    private static final VoxelShape SHAPE_ON_CAMPFIRE = Block.box(0, 0, 0, 16, 10, 16);

    public PanBlock(Properties pProperties) {
        super(pProperties);
        this.itemPropertyName = "onPan";
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PanBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        PanBlockEntity panBlockEntity = (PanBlockEntity) pLevel.getBlockEntity(pPos);
        ItemStack playerStack = pPlayer.getItemInHand(pHand);
        if (panBlockEntity != null) {
            Optional<IItemHandler> optional = panBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
            Optional<IFluidHandler> optional1 =  panBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
            if (optional.isPresent() ) {
                PanStackHandler stackHandler = (PanStackHandler) optional.get();
                ItemStack stack = stackHandler.getStackInSlot(1);
                ItemStack resultStack = stackHandler.getStackInSlot(0);
                stackHandler.updateLevel(pLevel);
                Item requiredItem = Items.AIR;
                if (!resultStack.isEmpty()) {
                    CompoundTag tag = resultStack.getTag();
                    byte extracted = (byte) resultStack.getCount();
                    if (tag != null) {
                        if (tag.contains("container")) {
                            requiredItem = BrutalContainers.valueOf(tag.getString("container").toUpperCase()).getItem();
                        }
                        if (playerStack.is(requiredItem)) {
                            if (!pPlayer.isCreative()) {
                                if (playerStack.getCount() < resultStack.getCount() && requiredItem != Items.AIR) {
                                    extracted = (byte) playerStack.getCount();
                                    playerStack.shrink(extracted);
                                }
                            }
                            if (tag.contains("onPan")) {
                                tag.remove("onPan");
                            }
                            tag.remove("container");
                            if (!pLevel.isClientSide) {
                                stackHandler.extractItem(0, extracted, false);
                                pPlayer.addItem(resultStack.copyWithCount(extracted));
                                panBlockEntity.setChanged();
                            }
                        } else {
                            return InteractionResult.PASS;
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else if (optional1.isPresent()) {
                    FluidTank tank = (FluidTank) optional1.get();
                    if (pState.getValue(ON_CAMPFIRE) != OnCampfire.NONE && tank.isEmpty()) {
                        if (playerStack.is(ItemRegistry.RAPESEED_OIL_BOTTLE.get())) {
                            if (!pLevel.isClientSide) {
                                if (!pPlayer.isCreative()) {
                                    playerStack.shrink(1);
                                }
                                tank.fill(new FluidStack(FluidRegistry.SOURCE_RAPESEED_OIL.get(), 250), IFluidHandler.FluidAction.EXECUTE);
                                pLevel.setBlockAndUpdate(pPos, pState);
                                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                                pPlayer.addItem(new ItemStack(Items.GLASS_BOTTLE, 1));
                                panBlockEntity.setChanged();
                            }
                            return InteractionResult.SUCCESS;
                        } else if (playerStack.is(ItemRegistry.STIRRED_EGG_BOTTLE.get())) {
                            if (!pLevel.isClientSide) {
                                if (!pPlayer.isCreative()) {
                                    playerStack.shrink(1);
                                }
                                tank.fill(new FluidStack(FluidRegistry.SOURCE_STIRRED_EGG.get(), 250), IFluidHandler.FluidAction.EXECUTE);
                                pLevel.setBlockAndUpdate(pPos, pState);
                                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                                pPlayer.addItem(new ItemStack(Items.GLASS_BOTTLE, 1));
                                panBlockEntity.setChanged();
                            }
                            return InteractionResult.SUCCESS;
                        }
                    } else if (pPlayer.isShiftKeyDown() && stack != ItemStack.EMPTY) {
                        if (!pLevel.isClientSide) {
                            pPlayer.addItem(stack.copy());
                            stackHandler.extractItem(1, stack.getCount(), false);
                            panBlockEntity.setChanged();
                        }
                        return InteractionResult.SUCCESS;
                    } else if (stack.getCount() < 6 && stackHandler.isItemValid(1, playerStack) && tank.getFluid().getAmount() == 250 &&
                            tank.getFluid().getFluid().isSame(FluidRegistry.SOURCE_RAPESEED_OIL.get())) {
                        if (!pLevel.isClientSide) {
                            stackHandler.insertItem(1, playerStack.copyWithCount(1), false);
                            if (!pPlayer.isCreative()) {
                                playerStack.shrink(1);
                            }
                            panBlockEntity.setChanged();
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
        return pBlockEntityType == BlockEntityTypeRegistry.PAN_BLOCK_ENTITY.get() && !pLevel.isClientSide ? PanBlockEntity::tick : null;
    }
}
