package com.christofmeg.brutalharvest;

import com.christofmeg.brutalharvest.client.event.ClientSetupEvent;
import com.christofmeg.brutalharvest.common.event.CommonSetupEvent;
import com.christofmeg.brutalharvest.common.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mod(CommonConstants.MOD_ID)
public class BrutalHarvest {

    public BrutalHarvest() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        GeckoLib.initialize();
        init(bus);
        bus.addListener(ClientSetupEvent::clientSetupEvent);
        bus.addListener(ClientSetupEvent::registerRenderers);
        bus.addListener(ClientSetupEvent::registerLayerDefinitions);
        bus.addListener(ClientSetupEvent::registerBlockColors);
        bus.addListener(ClientSetupEvent::registerItemColors);
        bus.addListener(ClientSetupEvent::registerParticleProviders);

        bus.addListener(CommonSetupEvent::commonSetupEvent);
    }

    private void init(@Nonnull IEventBus modEventBus) {
        MinecraftForge.EVENT_BUS.register(this);
        BlockRegistry.init(modEventBus);
        ItemRegistry.init(modEventBus);
        CreativeModeTabRegistry.init(modEventBus);
        BlockEntityTypeRegistry.init(modEventBus);
        EntityTypeRegistry.init(modEventBus);
        AdvancementRegistry.register();
        LootModifierRegistry.init(modEventBus);
        SoundRegistry.init(modEventBus);
        RecipeTypeRegistry.init(modEventBus);
        RecipeSerializerRegistry.init(modEventBus);
        TrunkPlacerTypeRegistry.init(modEventBus);
        EnchantmentRegistry.init(modEventBus);
        MenuRegistry.init(modEventBus);
        FluidTypeRegistry.init(modEventBus);
        FluidRegistry.init(modEventBus);
        ParticleTypeRegistry.init(modEventBus);
    }

    //TODO JEI recipe compatibility
}
