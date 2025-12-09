package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class BlockEntityTypeRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CommonConstants.MOD_ID);

    public static final RegistryObject<BlockEntityType<MillstoneBlockEntity>> MILLSTONE_BLOCK_ENTITY;
    public static final RegistryObject<BlockEntityType<PanBlockEntity>> PAN_BLOCK_ENTITY;
    public static final RegistryObject<BlockEntityType<PotBlockEntity>> POT_BLOCK_ENTITY;
    public static final RegistryObject<BlockEntityType<BrutalSignBlockEntity>> BRUTAL_SIGN_BLOCK_ENTITY;
    public static final RegistryObject<BlockEntityType<BrutalHangingSignBlockEntity>> BRUTAL_HANGING_SIGN_BLOCK_ENTITY;

    public static void init(@Nonnull IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    static {
        MILLSTONE_BLOCK_ENTITY = BLOCK_ENTITIES.register("millstone", () -> BlockEntityType.Builder.of(MillstoneBlockEntity::new, BlockRegistry.MILLSTONE.get()).build(null));
        PAN_BLOCK_ENTITY = BLOCK_ENTITIES.register("pan", () -> BlockEntityType.Builder.of(PanBlockEntity::new, BlockRegistry.PAN.get()).build(null));
        POT_BLOCK_ENTITY = BLOCK_ENTITIES.register("pot", () -> BlockEntityType.Builder.of(PotBlockEntity::new, BlockRegistry.POT.get()).build(null));
        BRUTAL_SIGN_BLOCK_ENTITY = BLOCK_ENTITIES.register("brutal_sign", () -> BlockEntityType.Builder.of(BrutalSignBlockEntity::new, BlockRegistry.RUBBER_SIGN.get(), BlockRegistry.RUBBER_WALL_SIGN.get()).build(null));
        BRUTAL_HANGING_SIGN_BLOCK_ENTITY = BLOCK_ENTITIES.register("brutal_hanging_sign", () -> BlockEntityType.Builder.of(BrutalHangingSignBlockEntity::new, BlockRegistry.RUBBER_HANGING_SIGN.get(), BlockRegistry.RUBBER_WALL_HANGING_SIGN.get()).build(null));
    }

}
