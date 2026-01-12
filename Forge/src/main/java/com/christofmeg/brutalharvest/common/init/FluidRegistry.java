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

    public static final RegistryObject<FlowingFluid> SOURCE_RUBBER = FLUIDS.register("rubber", () -> new ForgeFlowingFluid.Source(FluidRegistry.RUBBER_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RUBBER = FLUIDS.register("flowing_rubber", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.RUBBER_PROPERTIES));
    public static final ForgeFlowingFluid.Properties RUBBER_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeRegistry.RUBBER_TYPE, SOURCE_RUBBER, FLOWING_RUBBER);

    public static final RegistryObject<FlowingFluid> SOURCE_STIRRED_EGG = FLUIDS.register("stirred_egg", () -> new ForgeFlowingFluid.Source(FluidRegistry.STIRRED_EGG_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STIRRED_EGG = FLUIDS.register("flowing_stirred_egg", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.STIRRED_EGG_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STIRRED_EGG_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeRegistry.STIRRED_EGG_TYPE, SOURCE_STIRRED_EGG, FLOWING_STIRRED_EGG);

    public static final RegistryObject<FlowingFluid> SOURCE_BLUEBERRY_JAM = FLUIDS.register("blueberry_jam", () -> new ForgeFlowingFluid.Source(FluidRegistry.BLUEBERRY_JAM_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BLUEBERRY_JAM = FLUIDS.register("flowing_blueberry_jam", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.BLUEBERRY_JAM_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BLUEBERRY_JAM_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeRegistry.BLUEBERRY_JAM_TYPE, SOURCE_BLUEBERRY_JAM, FLOWING_BLUEBERRY_JAM);

    public static final RegistryObject<FlowingFluid> SOURCE_STRAWBERRY_JAM = FLUIDS.register("strawberry_jam", () -> new ForgeFlowingFluid.Source(FluidRegistry.STRAWBERRY_JAM_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STRAWBERRY_JAM = FLUIDS.register("flowing_strawberry_jam", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.STRAWBERRY_JAM_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STRAWBERRY_JAM_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeRegistry.STRAWBERRY_JAM_TYPE, SOURCE_STRAWBERRY_JAM, FLOWING_STRAWBERRY_JAM);

    public static void init(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
