package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.fluid.RapeseedOilFluidType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidTypeRegistry {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CommonConstants.MOD_ID);

    public static final RegistryObject<FluidType> RAPESEED_OIL_TYPE;

    public static void init(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }

    static {
        RAPESEED_OIL_TYPE = FLUID_TYPES.register("rapeseed_oil_fluid", () -> new RapeseedOilFluidType(FluidType.Properties.create()));
    }
}
