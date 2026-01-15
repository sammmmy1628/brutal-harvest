package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CommonConstants.MOD_ID);

    public static final RegistryObject<SoundEvent> TOMATO_SPLAT = registerSoundEvents("tomato_splat");

    public static final RegistryObject<SoundEvent> POT_BOILING = registerSoundEvents("pot_boiling");
    public static final RegistryObject<SoundEvent> PAN_FRYING = registerSoundEvents("pan_frying");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CommonConstants.MOD_ID, name)));
    }

    public static void init(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}