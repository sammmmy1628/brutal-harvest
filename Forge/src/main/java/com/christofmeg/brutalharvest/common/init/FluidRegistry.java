package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidRegistry {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, CommonConstants.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_RAPESEED_OIL = FLUIDS.register("rapeseed_oil", () -> new ForgeFlowingFluid.Source(FluidRegistry.RAPESEED_OIL_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RAPESEED_OIL = FLUIDS.register("flowing_rapeseed_oil", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.RAPESEED_OIL_PROPERTIES));
    public static final ForgeFlowingFluid.Properties RAPESEED_OIL_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeRegistry.RAPESEED_OIL_TYPE, SOURCE_RAPESEED_OIL, FLOWING_RAPESEED_OIL);

    public static final RegistryObject<FlowingFluid> SOURCE_COFFEE = FLUIDS.register("coffee", () -> new ForgeFlowingFluid.Source(FluidRegistry.COFFEE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_COFFEE = FLUIDS.register("flowing_coffee", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.COFFEE_PROPERTIES));
    public static final ForgeFlowingFluid.Properties COFFEE_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeRegistry.COFFEE_TYPE, SOURCE_COFFEE, FLOWING_COFFEE);

    public static void init(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
