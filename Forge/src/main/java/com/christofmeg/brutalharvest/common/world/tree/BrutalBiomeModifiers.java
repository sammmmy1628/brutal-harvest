package com.christofmeg.brutalharvest.common.world.tree;

import com.christofmeg.brutalharvest.CommonConstants;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class BrutalBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_TREE_RUBBER = registerKey("add_rubber_tree");

    public static final ResourceKey<BiomeModifier> ADD_TOMATO_PLAINS = registerKey("add_tomato_plains");
    public static final ResourceKey<BiomeModifier> ADD_TOMATO_FOREST = registerKey("add_tomato_forest");
    public static final ResourceKey<BiomeModifier> ADD_LETTUCE_PLAINS = registerKey("add_lettuce_plains");
    public static final ResourceKey<BiomeModifier> ADD_LETTUCE_TAIGA = registerKey("add_lettuce_taiga");
    public static final ResourceKey<BiomeModifier> ADD_RAPESEED_PLAINS = registerKey("add_rapeseed_plains");
    public static final ResourceKey<BiomeModifier> ADD_SUGAR_BEET_PLAINS = registerKey("add_sugar_beet_plains");
    public static final ResourceKey<BiomeModifier> ADD_SUGAR_BEET_TAIGA = registerKey("add_sugar_beet_taiga");
    public static final ResourceKey<BiomeModifier> ADD_CORN_PLAINS = registerKey("add_corn_plains");
    public static final ResourceKey<BiomeModifier> ADD_CORN_SAVANNA = registerKey("add_corn_savanna");
    public static final ResourceKey<BiomeModifier> ADD_COTTON_SAVANNA = registerKey("add_cotton_savanna");
    public static final ResourceKey<BiomeModifier> ADD_COTTON_BADLANDS = registerKey("add_cotton_badlands");
    public static final ResourceKey<BiomeModifier> ADD_COFFEE_JUNGLE = registerKey("add_coffee_forest");
    public static final ResourceKey<BiomeModifier> ADD_CUCUMBER_PLAINS = registerKey("add_cucumber_plains");
    public static final ResourceKey<BiomeModifier> ADD_CUCUMBER_FOREST = registerKey("add_cucumber_forest");
    public static final ResourceKey<BiomeModifier> ADD_STRAWBERRY_FOREST = registerKey("add_strawberry_forest");
    public static final ResourceKey<BiomeModifier> ADD_BLUEBERRY_FOREST = registerKey("add_blueberry_forest");


    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_TREE_RUBBER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_HOT_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.RUBBER_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_TOMATO_PLAINS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.TOMATO_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_TOMATO_FOREST, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.TOMATO_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_LETTUCE_PLAINS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.LETTUCE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_LETTUCE_TAIGA, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_TAIGA),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.LETTUCE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_CORN_PLAINS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.CORN_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_CORN_SAVANNA, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_SAVANNA),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.CORN_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_RAPESEED_PLAINS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.RAPESEED_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_SUGAR_BEET_PLAINS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.SUGAR_BEET_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_SUGAR_BEET_TAIGA, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_TAIGA),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.SUGAR_BEET_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_CUCUMBER_PLAINS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.CUCUMBER_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_CUCUMBER_FOREST, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.CUCUMBER_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_COTTON_SAVANNA, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_SAVANNA),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.COTTON_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_COTTON_BADLANDS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_BADLANDS),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.COTTON_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_COFFEE_JUNGLE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.COFFEE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_STRAWBERRY_FOREST, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.STRAWBERRY_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_BLUEBERRY_FOREST, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placedFeatures.getOrThrow(BrutalPlacedFeatures.BLUEBERRY_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CommonConstants.MOD_ID, name));
    }

}
