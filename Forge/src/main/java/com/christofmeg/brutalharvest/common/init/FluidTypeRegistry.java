package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.fluid.BrutalFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidTypeRegistry {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CommonConstants.MOD_ID);

    public static final RegistryObject<FluidType> RAPESEED_OIL_TYPE;
    public static final RegistryObject<FluidType> COFFEE_TYPE;
    public static final RegistryObject<FluidType> RUBBER_TYPE;

    public static void init(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }

    static {
        RAPESEED_OIL_TYPE = FLUID_TYPES.register("rapeseed_oil", () -> new BrutalFluidType(
                new ResourceLocation(CommonConstants.MOD_ID, "block/rapeseed_oil_still"), new ResourceLocation(CommonConstants.MOD_ID, "block/rapeseed_oil_flowing")));
        COFFEE_TYPE = FLUID_TYPES.register("coffee", () -> new BrutalFluidType(
                new ResourceLocation(CommonConstants.MOD_ID, "block/coffee_still"), new ResourceLocation(CommonConstants.MOD_ID, "block/coffee_flowing")));
        RUBBER_TYPE = FLUID_TYPES.register("rubber", () -> new BrutalFluidType(
                new ResourceLocation(CommonConstants.MOD_ID,"block/rubber_still"), new ResourceLocation(CommonConstants.MOD_ID, "block/rubber_flowing")));
    }
}
