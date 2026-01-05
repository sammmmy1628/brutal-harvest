package com.christofmeg.brutalharvest.client.event;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.client.model.ThrownKnifeModel;
import com.christofmeg.brutalharvest.client.model.ThrownScytheModel;
import com.christofmeg.brutalharvest.client.particle.FallingRubberParticles;
import com.christofmeg.brutalharvest.client.renderer.*;
import com.christofmeg.brutalharvest.client.screen.SeedSatchelScreen;
import com.christofmeg.brutalharvest.common.block.woodtype.BrutalWoodTypes;
import com.christofmeg.brutalharvest.client.renderer.PanBlockEntityRenderer;
import com.christofmeg.brutalharvest.client.renderer.PotBlockEntityRenderer;
import com.christofmeg.brutalharvest.common.init.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSetupEvent {

    public static void clientSetupEvent(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.addWoodType(BrutalWoodTypes.RUBBER_WOOD_TYPE);
            EntityRenderers.register(EntityTypeRegistry.TOMATO_PROJECTILE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.THROWN_SCYTHE.get(), ThrownScytheRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.THROWN_KNIFE.get(), ThrownKnifeRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BRUTAL_BOAT_ENTITY.get(), context -> new BrutalBoatRenderer(context, false));
            EntityRenderers.register(EntityTypeRegistry.BRUTAL_CHEST_BOAT_ENTITY.get(), context -> new BrutalBoatRenderer(context, true));
            BlockEntityRenderers.register(BlockEntityTypeRegistry.MILLSTONE_BLOCK_ENTITY.get(), MillstoneBlockEntityRenderer::new);
            MenuScreens.register(MenuRegistry.SEED_SATCHEL_MENU_TYPE.get(), SeedSatchelScreen::new);
            ItemProperties.register(ItemRegistry.SEED_SATCHEL.get(), new ResourceLocation(CommonConstants.MOD_ID, "filled"),
                    (stack, level, living, id) -> {
                        CompoundTag tag = stack.getTag();
                        if (tag != null && tag.contains("isEmpty")) {
                            return tag.getFloat("isEmpty");
                        }
                        return 0.0F;
                    });
            ItemProperties.register(ItemRegistry.POPCORN.get(), new ResourceLocation(CommonConstants.MOD_ID, "on_pan"),
                    (stack, level, living, id) -> {
                        CompoundTag tag = stack.getTag();
                        if (tag != null && tag.contains("onPan")) {
                            return tag.getFloat("onPan");
                        }
                        return 0.0F;
                    });
            ItemProperties.register(ItemRegistry.PASTA.get(), new ResourceLocation(CommonConstants.MOD_ID, "in_pot"),
                    (stack, level, living, id) -> {
                        CompoundTag tag = stack.getTag();
                        if (tag != null && tag.contains("inPot")) {
                            return tag.getFloat("inPot");
                        }
                        return 0.0F;
                    });
        });
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityTypeRegistry.PAN_BLOCK_ENTITY.get(), PanBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityTypeRegistry.POT_BLOCK_ENTITY.get(), PotBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityTypeRegistry.BRUTAL_SIGN_BLOCK_ENTITY.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityTypeRegistry.BRUTAL_HANGING_SIGN_BLOCK_ENTITY.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityTypeRegistry.RUBBER_CAULDRON_BLOCK_ENTITY.get(), RubberCauldronBlockEntityRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RenderLayers.register("scythe"), ThrownScytheModel::createLayer);
        event.registerLayerDefinition(RenderLayers.register("knife"), ThrownKnifeModel::createLayer);
        event.registerLayerDefinition(RenderLayers.register("boat/rubber"), BoatModel::createBodyModel);
        event.registerLayerDefinition(RenderLayers.register("chest_boat/rubber"), ChestBoatModel::createBodyModel);
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : -1, BlockRegistry.GRASS_SLAB.get());}

    public static void registerItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> GrassColor.get(0.5D, 1.0D), BlockRegistry.GRASS_SLAB.get());
    }

    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeRegistry.FALLING_RUBBER.get(), FallingRubberParticles.Provider::new);
    }
}
