
package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.blockentity.MillstoneBlockEntity;
import com.christofmeg.brutalharvest.common.handler.MillstoneStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.FluidRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
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
            if (!pPlayer.isShiftKeyDown() && optional1.isPresent()) {
                ((MillstoneStackHandler) optional1.get()).updateLevel(pLevel);
                ItemStack playerStack = pPlayer.getItemInHand(pHand);
                if (playerStack == ItemStack.EMPTY) {
                    ItemStack stack = optional1.get().getStackInSlot(1);
                    if (stack == ItemStack.EMPTY && !pLevel.isClientSide && blockEntity.cooldown == 0) {
                        blockEntity.craft();
                    } else if (stack != ItemStack.EMPTY) {
                        pPlayer.addItem(stack.copy());
                        optional1.get().extractItem(1, stack.getCount(), false);
                    }
                } else if (optional2.isPresent() && (playerStack.is(Items.GLASS_BOTTLE) || playerStack.is(Items.BUCKET))) {
                    FluidTank fluidTank = (FluidTank) optional2.get();
                    FluidStack fluidStack = fluidTank.getFluidInTank(0);
                    if (fluidStack.getAmount() >= 250 && playerStack.is(Items.GLASS_BOTTLE)) {
                        if (fluidStack.getFluid().isSame(FluidRegistry.SOURCE_RAPESEED_OIL.get())) {
                            fluidTank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                            pLevel.playSound(pPlayer, pPos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                            pPlayer.addItem(new ItemStack(ItemRegistry.RAPESEED_OIL_BOTTLE.get(), 1));
                        }
                    } else if (fluidStack.getAmount() == 1000 && playerStack.is(Items.BUCKET)) {
                        FluidUtil.tryFillContainer(playerStack, fluidTank, 1000, pPlayer, true);
                    }
                } else {
                    MillstoneStackHandler stackHandler = (MillstoneStackHandler) optional1.get();
                    if (stackHandler.isItemValid(0, playerStack)) {
                        if (!pPlayer.isCreative()) {
                            playerStack.shrink(1);
                        }
                        stackHandler.insertItem(0, new ItemStack(playerStack.getItem(), 1), false);
                    }
                }
            } else if (!pLevel.isClientSide) {
                optional1.ifPresent(handler -> {
                    ItemStack stack = handler.getStackInSlot(0);
                    handler.extractItem(0, stack.getCount(), false);
                    pPlayer.addItem(stack);
                });
            }
            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
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
           blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
               SimpleContainer container = new SimpleContainer(iItemHandler.getStackInSlot(0), iItemHandler.getStackInSlot(1));
               Containers.dropContents(pLevel, pPos, container);
           });
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
