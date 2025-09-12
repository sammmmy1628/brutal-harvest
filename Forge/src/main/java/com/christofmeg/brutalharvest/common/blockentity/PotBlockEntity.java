package com.christofmeg.brutalharvest.common.blockentity;

import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import com.christofmeg.brutalharvest.common.handler.PotStackHandler;
import com.christofmeg.brutalharvest.common.init.BlockEntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Cooking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PotBlockEntity extends BlockEntity {

    private final IItemHandler itemHandler = new PotStackHandler();
    private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> this.itemHandler);
    private int cookingProgress = 0;

    public PotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeRegistry.POT_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("cookingProgress", this.cookingProgress);
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> pTag.merge(((PotStackHandler) iItemHandler).serializeNBT()));
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("cookingProgress")) {
            this.cookingProgress = pTag.getInt("cookingProgress");
        }
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> ((PotStackHandler) iItemHandler).deserializeNBT(pTag));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHandlerLazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerLazyOptional.invalidate();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        Optional<Cooking> optional = this.getCurrentRecipe();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
        optional.ifPresent(cooking -> this.cookingProgress = cooking.ticks());
    }

    private Optional<Cooking> getCurrentRecipe() {
        Optional<IItemHandler> itemHandlerOptional = this.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (itemHandlerOptional.isPresent() && this.level != null) {
            SimpleContainer container = new SimpleContainer(itemHandlerOptional.get().getStackInSlot(0));
            return this.level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get(), container, this.level);
        }
        return Optional.empty();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        PotBlockEntity blockEntity = (PotBlockEntity) t;
        Optional<IItemHandler> optional = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress--;
        } else if (optional.isPresent() && optional.get().getStackInSlot(0) != ItemStack.EMPTY) {
            IItemHandler iItemHandler = optional.get();
            blockEntity.getCurrentRecipe().ifPresent(frying -> {
                if (iItemHandler.getStackInSlot(1) == ItemStack.EMPTY ||
                        iItemHandler.getStackInSlot(1).getItem() == frying.getResultItem(level.registryAccess()).getItem()) {
                    ItemStack result = frying.getResultItem(level.registryAccess());
                    int count = iItemHandler.getStackInSlot(0).getCount();
                    result.setCount(result.getCount() * count);
                    if (iItemHandler.getStackInSlot(1).getCount() + result.getCount() <= 64) {
                        iItemHandler.extractItem(0, count, false);
                        iItemHandler.insertItem(1, result, false);
                    }
                }
            });
            level.setBlockAndUpdate(pos, state.setValue(BaseCookingBlock.FILLED, false));
        }
    }
}
