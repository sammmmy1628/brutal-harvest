package com.christofmeg.brutalharvest.common.blockentity;

import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BrutalHangingSignBlockEntity extends SignBlockEntity {

    public BrutalHangingSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeRegistry.BRUTAL_HANGING_SIGN_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return BlockEntityTypeRegistry.BRUTAL_HANGING_SIGN_BLOCK_ENTITY.get();
    }
}
