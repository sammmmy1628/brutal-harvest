package com.christofmeg.brutalharvest.common.block;

import com.christofmeg.brutalharvest.common.blockentity.RubberCauldronBlockEntity;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.init.FluidRegistry;
import com.christofmeg.brutalharvest.common.init.ParticleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FaucetBlock extends HorizontalDirectionalBlock {

    private static final VoxelShape[] SHAPES = {
            Block.box(5, 3, 0, 11, 11, 9),
            Block.box(7, 3, 5, 16, 11, 11),
            Block.box(5, 3, 7, 11, 11, 16),
            Block.box(0, 3, 5, 9, 11, 11)
    };

    public FaucetBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        BlockPos cauldronPos = getCauldronPosBelow(pPos, pLevel);
        if (canExtractRubber(pState, pPos, pLevel) && cauldronPos != null) {
            BlockState cauldron = pLevel.getBlockState(cauldronPos);
            if (cauldron.is(Blocks.CAULDRON)) {
                pLevel.setBlock(cauldronPos, BlockRegistry.RUBBER_CAULDRON.get().defaultBlockState(), Block.UPDATE_ALL);
            }
            if (cauldron.is(BlockRegistry.RUBBER_CAULDRON.get())) {
                RubberCauldronBlockEntity blockEntity = (RubberCauldronBlockEntity) pLevel.getBlockEntity(cauldronPos);
                if (blockEntity != null) {
                    blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(iFluidHandler -> {
                        if (((FluidTank) iFluidHandler).getFluid().getAmount() < 1000) {
                            iFluidHandler.fill(new FluidStack(FluidRegistry.SOURCE_RUBBER.get(), 60), IFluidHandler.FluidAction.EXECUTE);
                            blockEntity.setChanged();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (canExtractRubber(pState, pPos, pLevel) && pRandom.nextInt(3) == 0) {
            pLevel.addParticle(ParticleTypeRegistry.FALLING_RUBBER.get(), pPos.getX() + 0.5, pPos.getY() + 0.1875F, pPos.getZ() + 0.5, 0, -0.2F, 0);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPES[pState.getValue(HorizontalDirectionalBlock.FACING).get2DDataValue()];
    }

    private static boolean canExtractRubber(BlockState state, BlockPos pos, Level level) {
        Direction attachementDirection = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
        BlockPos posBehind = pos.relative(attachementDirection, 1);
        BlockState stateBehind = level.getBlockState(posBehind);
        return stateBehind.is(BlockRegistry.RUBBER_LOG_GENERATED.get()) && stateBehind.getValue(RubberLogGeneratedBlock.OPEN)
                && stateBehind.getValue(RubberLogGeneratedBlock.CUT) && stateBehind.getValue(RubberLogGeneratedBlock.FACING).getOpposite().equals(attachementDirection);
    }

    private static @Nullable BlockPos getCauldronPosBelow(BlockPos pos, Level level) {
        BlockPos cauldronPos = pos;
        for (int dist = 1; dist < 3; dist++) {
            cauldronPos = cauldronPos.below();
            BlockState checkedState = level.getBlockState(cauldronPos);
            if (checkedState.is(Blocks.CAULDRON) || checkedState.is(BlockRegistry.RUBBER_CAULDRON.get())) {
                return cauldronPos;
            } else if (!checkedState.is(Blocks.AIR)) {
                return null;
            }
        }
        return null;
    }
}
