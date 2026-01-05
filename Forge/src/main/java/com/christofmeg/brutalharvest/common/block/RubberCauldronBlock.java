package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.blockentity.RubberCauldronBlockEntity;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RubberCauldronBlock extends BaseEntityBlock {

    private final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(Block.box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
            Block.box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
            Block.box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)), BooleanOp.ONLY_FIRST);

    public RubberCauldronBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        ItemStack playerStack = pPlayer.getItemInHand(pHand);
        RubberCauldronBlockEntity blockEntity = (RubberCauldronBlockEntity) pLevel.getBlockEntity(pPos);
        if (blockEntity != null) {
            Optional<IFluidHandler> optional = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
            if ((playerStack.is(Items.BUCKET) || playerStack.is(Items.BOWL)) && optional.isPresent()) {
                if (((FluidTank) optional.get()).getFluid().getAmount() == 1000) {
                    if (!pLevel.isClientSide) {
                        optional.get().drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        if (playerStack.is(Items.BUCKET)) {
                            pPlayer.addItem(new ItemStack(ItemRegistry.RUBBER_BUCKET.get(), 1));
                        } else if (playerStack.is(Items.BOWL)) {
                            pPlayer.addItem(new ItemStack(ItemRegistry.RUBBER_BOWL.get(), 1));
                        }
                        playerStack.shrink(1);
                        blockEntity.setChanged();
                        pLevel.setBlock(pPos, Blocks.CAULDRON.defaultBlockState(), Block.UPDATE_ALL);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new RubberCauldronBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }
}
