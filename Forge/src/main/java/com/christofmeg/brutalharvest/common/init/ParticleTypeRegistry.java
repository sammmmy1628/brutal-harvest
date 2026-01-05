package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleTypeRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CommonConstants.MOD_ID);

    public static final RegistryObject<SimpleParticleType> FALLING_RUBBER = PARTICLE_TYPES.register("falling_rubber", () -> new SimpleParticleType(true));

    public static void init(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
