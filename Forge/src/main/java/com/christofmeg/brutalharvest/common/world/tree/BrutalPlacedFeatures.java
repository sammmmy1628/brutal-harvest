package com.christofmeg.brutalharvest.common.world.tree;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class BrutalPlacedFeatures {

    public static final ResourceKey<PlacedFeature> RUBBER_PLACED_KEY = registerKey("rubber_placed");

    public static final ResourceKey<PlacedFeature> TOMATO_PLACED_KEY = registerKey("tomato_placed");
    public static final ResourceKey<PlacedFeature> LETTUCE_PLACED_KEY = registerKey("lettuce_placed");
    public static final ResourceKey<PlacedFeature> RAPESEED_PLACED_KEY = registerKey("rapeseed_placed");
    public static final ResourceKey<PlacedFeature> SUGAR_BEET_PLACED_KEY = registerKey("sugar_beet_placed");
    public static final ResourceKey<PlacedFeature> STRAWBERRY_PLACED_KEY = registerKey("strawberry_placed");
    public static final ResourceKey<PlacedFeature> CORN_PLACED_KEY = registerKey("corn_placed");
    public static final ResourceKey<PlacedFeature> COTTON_PLACED_KEY = registerKey("cotton_placed");
    public static final ResourceKey<PlacedFeature> COFFEE_PLACED_KEY = registerKey("coffee_placed");
    public static final ResourceKey<PlacedFeature> CUCUMBER_PLACED_KEY = registerKey("cucumber_placed");
    public static final ResourceKey<PlacedFeature> BLUEBERRY_PLACED_KEY = registerKey("blueberry_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, RUBBER_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.RUBBER_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1f, 1), //10 % chance to spawn 1 tree in a forge:is_hot/overworld biome
                        BlockRegistry.RUBBER_SAPLING.get()));

        register(context, TOMATO_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.TOMATO_KEY),
                List.of(RarityFilter.onAverageOnceEvery(45),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, LETTUCE_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.LETTUCE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(20),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, RAPESEED_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.RAPESEED_KEY),
                List.of(RarityFilter.onAverageOnceEvery(45),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, SUGAR_BEET_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.SUGAR_BEET_KEY),
                List.of(RarityFilter.onAverageOnceEvery(45),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, STRAWBERRY_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.STRAWBERRY_KEY),
                List.of(RarityFilter.onAverageOnceEvery(20),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, CORN_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.CORN_KEY),
                List.of(RarityFilter.onAverageOnceEvery(45),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, COTTON_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.COTTON_KEY),
                List.of(RarityFilter.onAverageOnceEvery(80),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, COFFEE_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.COFFEE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(80),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, CUCUMBER_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.CUCUMBER_KEY),
                List.of(RarityFilter.onAverageOnceEvery(45),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

        register(context, BLUEBERRY_PLACED_KEY, configuredFeatures.getOrThrow(BrutalConfiguredFeatures.BLUEBERRY_KEY),
                List.of(RarityFilter.onAverageOnceEvery(20),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.WILD_TOMATO.get().defaultBlockState(), Vec3i.ZERO))));

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CommonConstants.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

}
