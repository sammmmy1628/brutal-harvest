
package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.blockentity.MillstoneBlockEntity;
import com.christofmeg.brutalharvest.common.handler.MillstoneStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.FluidRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.BrutalContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
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

public class MillstoneBlock extends BaseEntityBlock {

    private static final Property<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 13, 16);

    public MillstoneBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        MillstoneBlockEntity blockEntity = (MillstoneBlockEntity) pLevel.getBlockEntity(pHit.getBlockPos());
        if (blockEntity != null) {
            Optional<IItemHandler> optional1 = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
            Optional<IFluidHandler> optional2 = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
            if (optional1.isPresent() && optional2.isPresent()) {
                MillstoneStackHandler handler = (MillstoneStackHandler) optional1.get();
                FluidTank tank = (FluidTank) optional2.get();
                ItemStack stack = handler.getStackInSlot(0);
                ItemStack ingredientStack = handler.getStackInSlot(1);
                if (!pPlayer.isShiftKeyDown()) {
                    handler.updateLevel(pLevel);
                    ItemStack playerStack = pPlayer.getItemInHand(pHand);
                    int firstMatchingSlotIndex = handler.getFirstMatchingSlot(playerStack.getItem());
                    if (!ingredientStack.isEmpty() && playerStack.isEmpty() && blockEntity.cooldown == 0) {
                        blockEntity.craft();
                        return InteractionResult.SUCCESS;
                    } else if (!stack.isEmpty() && !pLevel.isClientSide) {
                        CompoundTag tag = stack.getTag();
                        byte extracted = (byte) stack.getCount();
                        if (tag != null && tag.contains("container")) {
                            Item requiredItem = BrutalContainers.valueOf(tag.getString("container").toUpperCase()).getItem();
                            if (playerStack.is(requiredItem)) {
                                if (!pPlayer.isCreative()) {
                                    if (playerStack.getCount() < stack.getCount() && requiredItem != Items.AIR) {
                                        extracted = (byte) playerStack.getCount();
                                        playerStack.shrink(extracted);
                                    }
                                }
                                if (tag.contains("inMillstone")) {
                                    tag.remove("inMillstone");
                                }
                                tag.remove("container");
                            } else {
                                return InteractionResult.PASS;
                            }
                        }
                        pPlayer.addItem(stack.copyWithCount(extracted));
                        handler.extractItem(0, extracted, false);
                        blockEntity.setChanged();
                        return InteractionResult.SUCCESS;
                    } else if (tank.isEmpty() &&
                            (playerStack.is(Items.GLASS_BOTTLE) || playerStack.is(Items.BUCKET)) && !pLevel.isClientSide) {
                        FluidStack fluidStack = tank.getFluidInTank(0);
                        if (fluidStack.getAmount() >= 250 && playerStack.is(Items.GLASS_BOTTLE)) {
                            if (fluidStack.getFluid().isSame(FluidRegistry.SOURCE_RAPESEED_OIL.get())) {
                                tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                                pLevel.playSound(pPlayer, pPos, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                                if (!pPlayer.isCreative()) {
                                    playerStack.shrink(1);
                                }
                                pPlayer.addItem(new ItemStack(ItemRegistry.RAPESEED_OIL_BOTTLE.get(), 1));
                            } else if (fluidStack.getFluid().isSame(FluidRegistry.SOURCE_STIRRED_EGG.get())) {
                                tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                                pLevel.playSound(pPlayer, pPos, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                                if (!pPlayer.isCreative()) {
                                    playerStack.shrink(1);
                                }
                                pPlayer.addItem(new ItemStack(ItemRegistry.STIRRED_EGG_BOTTLE.get(), 1));
                            }
                        } else if (fluidStack.getAmount() == 1000 && playerStack.is(Items.BUCKET)) {
                            FluidUtil.tryFillContainer(playerStack, tank, 1000, pPlayer, true);
                        }
                        blockEntity.setChanged();
                        return InteractionResult.SUCCESS;
                    } else if (handler.isItemValid(1, playerStack) && firstMatchingSlotIndex != -1) {
                        if (!pLevel.isClientSide) {
                            if (!pPlayer.isCreative()) {
                                playerStack.shrink(1);
                            }
                            handler.insertItem(firstMatchingSlotIndex, playerStack.copyWithCount(1), false);
                            blockEntity.setChanged();
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    if (!pLevel.isClientSide) {
                        for (int iter = 1; iter < 7; iter++) {
                            ItemStack currentIngredientStack = handler.getStackInSlot(iter);
                            if (!currentIngredientStack.isEmpty()) {
                                pPlayer.addItem(currentIngredientStack);
                                handler.extractItem(iter, currentIngredientStack.getCount(), false);
                            }
                        }
                        blockEntity.setChanged();
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == BlockEntityTypeRegistry.MILLSTONE_BLOCK_ENTITY.get() && !pLevel.isClientSide ? MillstoneBlockEntity::tick : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new MillstoneBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
       pBuilder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pMovedByPiston) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity != null) {
           blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> dropValidContents(pLevel, pPos, iItemHandler));
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    private static void dropValidContents(Level level, BlockPos pos, IItemHandler inventory) {
        for(int $$5 = 0; $$5 < inventory.getSlots(); ++$$5) {
            ItemStack stack = inventory.getStackInSlot($$5);
            CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("inMillstone")) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }
}
