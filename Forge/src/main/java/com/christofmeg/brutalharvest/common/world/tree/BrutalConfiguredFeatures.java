package com.christofmeg.brutalharvest.common.world.tree;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BrutalConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_KEY = registerKey("rubber");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TOMATO_KEY = registerKey("tomato");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LETTUCE_KEY = registerKey("lettuce");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAPESEED_KEY = registerKey("rapeseed");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SUGAR_BEET_KEY = registerKey("sugar_beet");
    public static final ResourceKey<ConfiguredFeature<?, ?>> STRAWBERRY_KEY = registerKey("strawberry");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORN_KEY = registerKey("corn");
    public static final ResourceKey<ConfiguredFeature<?, ?>> COTTON_KEY = registerKey("cotton");
    public static final ResourceKey<ConfiguredFeature<?, ?>> COFFEE_KEY = registerKey("coffee");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CUCUMBER_KEY = registerKey("cucumber");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUEBERRY_KEY = registerKey("blueberry");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        register(context, RUBBER_KEY, Feature.TREE, createStraightBlobTree(BlockRegistry.RUBBER_LOG_GENERATED.get(), BlockRegistry.RUBBER_LEAVES.get(), 4, 2, 0, 2).ignoreVines().build());

        register(context, TOMATO_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_TOMATO.get(), 10, 4));
        register(context, LETTUCE_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_LETTUCE.get(), 20, 5));
        register(context, RAPESEED_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_RAPESEED.get(), 10, 4));
        register(context, SUGAR_BEET_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_SUGAR_BEET.get(), 10, 4));
        register(context, STRAWBERRY_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_STRAWBERRY.get(), 20, 5));
        register(context, CORN_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_CORN.get(), 10, 4));
        register(context, COTTON_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_COTTON.get(), 5, 3));
        register(context, COFFEE_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.WILD_COFFEE.get(), 5, 3));
        register(context, CUCUMBER_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.BLUEBERRY.get(), 10, 4));
        register(context, BLUEBERRY_KEY, Feature.RANDOM_PATCH, createRandomPatch(BlockRegistry.BLUEBERRY.get(), 20, 5));

    }

    private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(Block pLogBlock, Block pLeavesBlock, int pBaseHeight, int pHeightRandA, int pHeightRandB, int pRadius) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(pLogBlock),
                new RubberTrunkPlacer(pBaseHeight, pHeightRandA, pHeightRandB), BlockStateProvider.simple(pLeavesBlock),
                new BlobFoliagePlacer(ConstantInt.of(pRadius), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1));
    }

    private static RandomPatchConfiguration createRandomPatch(Block block, int tries, int spread) {
        return new RandomPatchConfiguration(tries, spread, 3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(block)), BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS)));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CommonConstants.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
