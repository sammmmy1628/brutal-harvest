package com.christofmeg.brutalharvest.common.entity;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.EntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BrutalChestBoatEntity extends ChestBoat {

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(BrutalChestBoatEntity.class, EntityDataSerializers.INT);


    public BrutalChestBoatEntity(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(EntityTypeRegistry.BRUTAL_CHEST_BOAT_ENTITY.get(), pLevel);
    }

    public BrutalChestBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(EntityTypeRegistry.BRUTAL_CHEST_BOAT_ENTITY.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    public @NotNull Item getDropItem() {
        return ItemRegistry.RUBBER_CHEST_BOAT_ITEM.get();
    }

    public void setVariant(BrutalBoatEntity.Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }

    public BrutalBoatEntity.Type getBrutalVariant() {
        return BrutalBoatEntity.Type.RUBBER;
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
            this.setVariant(BrutalBoatEntity.Type.byId(pCompound.getByte("Type")));
        }
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable("entity." + CommonConstants.MOD_ID + ".brutal_chest_boat." + this.getBrutalVariant().getName());
    }
}
