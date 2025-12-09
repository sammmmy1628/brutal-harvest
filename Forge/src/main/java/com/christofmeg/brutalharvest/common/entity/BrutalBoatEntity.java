package com.christofmeg.brutalharvest.common.entity;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.init.EntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class BrutalBoatEntity extends Boat {

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(BrutalBoatEntity.class, EntityDataSerializers.INT);

    public BrutalBoatEntity(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(EntityTypeRegistry.BRUTAL_BOAT_ENTITY.get(), pLevel);
    }

    public BrutalBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(EntityTypeRegistry.BRUTAL_BOAT_ENTITY.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    public @NotNull Item getDropItem() {
        return ItemRegistry.RUBBER_BOAT_ITEM.get();
    }

    public void setVariant(Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }

    public BrutalBoatEntity.Type getBrutalVariant() {
        return Type.RUBBER;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, this.getBrutalVariant().ordinal());
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        pCompound.putByte("Type", (byte) this.getVariant().ordinal());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        if (pCompound.contains("Type")) {
            this.setVariant(Type.byId(pCompound.getByte("Type")));
        }
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable( "entity." + CommonConstants.MOD_ID + ".brutal_boat." + this.getBrutalVariant().getName());
    }

    public enum Type implements StringRepresentable {
        RUBBER(BlockRegistry.RUBBER_PLANKS.get(), "rubber");

        private final String name;
        private final Block planks;
        private static final IntFunction<BrutalBoatEntity.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        Type(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public String toString() {
            return this.name;
        }

        public static BrutalBoatEntity.Type byId(int pId) {
            return BY_ID.apply(pId);
        }
    }
}
