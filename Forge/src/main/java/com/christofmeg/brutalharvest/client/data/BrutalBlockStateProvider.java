package com.christofmeg.brutalharvest.client.data;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.client.data.base.BaseBlockStateProvider;
import com.christofmeg.brutalharvest.common.block.*;
import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BrutalBlockStateProvider extends BaseBlockStateProvider {

    private final ExistingFileHelper fileHelper;

    public BrutalBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
        this.fileHelper = exFileHelper;
    }

    @Override
    public @NotNull String getName() {
        return CommonConstants.MOD_ID + " - BlockModel & BlockState";
    }

    @Override
    protected void registerStatesAndModels() {

        makeCrop(BlockRegistry.TOMATO.get(), TomatoCropBlock.AGE, modLoc("block/lowered_cross"));
        makeCrop(BlockRegistry.LETTUCE.get(), LettuceCropBlock.AGE);
        makeCrop(BlockRegistry.COFFEE.get(), CoffeeCropBlock.AGE);
        makeDoubleCrop(BlockRegistry.CORN.get(), 8, 2, modLoc("block/lowered_cross"), mcLoc("block/cross"));
        makeDoubleCrop(BlockRegistry.CUCUMBER.get(), 7, 4, modLoc("block/lowered_cross"), mcLoc("block/cross"));

        makeCrop(BlockRegistry.COTTON.get(), CottonCropBlock.AGE, modLoc("block/lowered_cross"));
        makeDoubleCrop(BlockRegistry.RAPESEED.get(), 8, 4, modLoc("block/lowered_cross"), mcLoc("block/cross"));
        makeCrop(BlockRegistry.SUGAR_BEET.get(), SugarBeetCropBlock.AGE);
        makeCrop(BlockRegistry.STRAWBERRY.get(), StrawberryCropBlock.AGE, modLoc("block/lowered_cross"));
    //    makeCrop(BlockRegistry.ONION.get(), OnionCropBlock.AGE);
        makeBush(BlockRegistry.BLUEBERRY.get(), BlueberryBushBlock.AGE);

        saplingBlock(BlockRegistry.RUBBER_SAPLING);
        logBlock((RotatedPillarBlock) BlockRegistry.RUBBER_LOG.get());
        axisBlock((RotatedPillarBlock) BlockRegistry.RUBBER_WOOD.get(), blockTexture(BlockRegistry.RUBBER_LOG.get()), blockTexture(BlockRegistry.RUBBER_LOG.get()));
        logBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_RUBBER_LOG.get());
        axisBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_RUBBER_WOOD.get(), blockTexture(BlockRegistry.STRIPPED_RUBBER_LOG.get()), blockTexture(BlockRegistry.STRIPPED_RUBBER_LOG.get()));
        simpleBlock(BlockRegistry.RUBBER_PLANKS.get());
        leavesBlock(BlockRegistry.RUBBER_LEAVES);

        slabBlock(BlockRegistry.DIRT_SLAB.get(), mcLoc("block/dirt"), mcLoc("block/dirt"));
        slabBlock(BlockRegistry.RUBBER_SLAB.get(), modLoc("block/rubber_planks"), modLoc("block/rubber_planks"));

        stairsBlock(BlockRegistry.RUBBER_STAIRS.get(), modLoc("block/rubber_planks"));

        pressurePlateBlock(BlockRegistry.RUBBER_PRESSURE_PLATE.get(), modLoc("block/rubber_planks"));

        buttonBlock(BlockRegistry.RUBBER_BUTTON.get(), modLoc("block/rubber_planks"));

        fenceBlock(BlockRegistry.RUBBER_FENCE.get(), modLoc("block/rubber_planks"));
        fenceGateBlock(BlockRegistry.RUBBER_FENCE_GATE.get(), modLoc("block/rubber_planks"));

        signBlock(BlockRegistry.RUBBER_SIGN.get(), BlockRegistry.RUBBER_WALL_SIGN.get(), modLoc("block/rubber_planks"));
        hangingSignBlock(BlockRegistry.RUBBER_HANGING_SIGN.get(), BlockRegistry.RUBBER_WALL_HANGING_SIGN.get(), modLoc("block/stripped_rubber_log"));

        doorBlockWithRenderType(BlockRegistry.RUBBER_DOOR.get(), modLoc("block/rubber_door_bottom"), modLoc("block/rubber_door_top"), mcLoc("cutout"));

        trapdoorBlockWithRenderType(BlockRegistry.RUBBER_TRAPDOOR.get(), modLoc("block/rubber_trapdoor"), true, mcLoc("cutout"));

        for (Block block : new Block[] {BlockRegistry.WOODEN_CUTTING_BOARD.get(), BlockRegistry.IRON_CUTTING_BOARD.get()}) {

            String name = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

            ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + name), this.fileHelper);
            for (Direction direction : HorizontalDirectionalBlock.FACING.getPossibleValues()) {
                int rot = (int) direction.toYRot();
                getVariantBuilder(block)
                        .partialState().with(CuttingBoardBlock.FACING, direction).modelForState().rotationY(rot).modelFile(model).addModel();
            }
        }

        for (Block block : new Block[] {BlockRegistry.PAN.get(), BlockRegistry.POT.get()}) {

            String name = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

            ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + name), this.fileHelper);
            ModelFile model_on_campfire = new ModelFile.ExistingModelFile(modLoc("block/" + name + "_on_campfire"), this.fileHelper);
            ModelFile model_on_soul_campfire = new ModelFile.ExistingModelFile(modLoc("block/" + name + "_on_soul_campfire"), this.fileHelper);
            for (Direction direction : HorizontalDirectionalBlock.FACING.getPossibleValues()) {
                int rot = (int) direction.getClockWise().toYRot();
                getVariantBuilder(block)
                        .partialState().with(BaseCookingBlock.ON_CAMPFIRE, BaseCookingBlock.OnCampfire.NONE).with(BaseCookingBlock.FACING, direction).modelForState().rotationY(rot).modelFile(model).addModel()
                        .partialState().with(BaseCookingBlock.ON_CAMPFIRE, BaseCookingBlock.OnCampfire.CAMPFIRE).with(BaseCookingBlock.FACING, direction).modelForState().rotationY(rot).modelFile(model_on_campfire).addModel()
                        .partialState().with(BaseCookingBlock.ON_CAMPFIRE, BaseCookingBlock.OnCampfire.SOUL_CAMPFIRE).with(BaseCookingBlock.FACING, direction).modelForState().rotationY(rot).modelFile(model_on_soul_campfire).addModel();
            }
        }

        ModelFile farmland = vanillaModels().withExistingParent("farmland", mcLoc("block/farmland_moist"));
        ModelFile farmland_moist = vanillaModels().withExistingParent("farmland_moist", mcLoc("block/farmland_moist"));
        ModelFile farmland_slab = models().withExistingParent("farmland_slab", modLoc("block/lowered_slab")).texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/dirt")).texture("top", mcLoc("block/farmland"));
        ModelFile farmland_slab_top = models().withExistingParent("farmland_slab_top", modLoc("block/lowered_slab_top")).texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/dirt")).texture("top", mcLoc("block/farmland"));
        ModelFile farmland_slab_moist = models().withExistingParent("farmland_slab_moist", modLoc("block/lowered_slab")).texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/dirt")).texture("top", mcLoc("block/farmland_moist"));
        ModelFile farmland_slab_moist_top = models().withExistingParent("farmland_slab_moist_top", modLoc("block/lowered_slab_top")).texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/dirt")).texture("top", mcLoc("block/farmland_moist"));
        for (int moisture = 0; moisture <= 7; moisture++) {
            ModelFile bottomModel = farmland_slab;
            ModelFile topModel = farmland_slab_top;
            ModelFile doubleModel = farmland;
            if (moisture == 7) {
                bottomModel = farmland_slab_moist;
                topModel = farmland_slab_moist_top;
                doubleModel = farmland_moist;
            }
            getVariantBuilder(BlockRegistry.FARMLAND_SLAB.get())
                    .partialState().with(FarmlandSlabBlock.MOISTURE, moisture).with(SlabBlock.TYPE, SlabType.BOTTOM).modelForState().modelFile(bottomModel).addModel()
                    .partialState().with(FarmlandSlabBlock.MOISTURE, moisture).with(SlabBlock.TYPE, SlabType.TOP).modelForState().modelFile(topModel).addModel()
                    .partialState().with(FarmlandSlabBlock.MOISTURE, moisture).with(SlabBlock.TYPE, SlabType.DOUBLE).modelForState().modelFile(doubleModel).addModel();
        }

        ModelFile dirt_path = models().withExistingParent("dirt_path", mcLoc("block/dirt_path"));
        ModelFile dirt_path_slab = models().withExistingParent("dirt_path_slab", modLoc("block/lowered_slab_upper_texture")).texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/dirt_path_side")).texture("top", mcLoc("block/dirt_path_top"));
        ModelFile dirt_path_slab_top = models().withExistingParent("dirt_path_slab_top", modLoc("block/lowered_slab_top")).texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/dirt_path_side")).texture("top", mcLoc("block/dirt_path_top"));
        getVariantBuilder(BlockRegistry.DIRT_PATH_SLAB.get())
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).modelForState().modelFile(dirt_path_slab).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).modelForState().modelFile(dirt_path_slab_top).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).modelForState().modelFile(dirt_path).addModel();

        ModelFile dirt_track = models().withExistingParent("dirt_track", modLoc("block/dirt_track"));
        ModelFile dirt_track_slab = models().withExistingParent("dirt_track_slab", modLoc("block/lowered_slab")).texture("bottom", mcLoc("block/dirt")).texture("side", modLoc("block/dirt_track_side")).texture("top", modLoc("block/dirt_track_top"));
        ModelFile dirt_track_slab_top = models().withExistingParent("dirt_track_slab_top", modLoc("block/lowered_slab_top")).texture("bottom", mcLoc("block/dirt")).texture("side", modLoc("block/dirt_track_side")).texture("top", modLoc("block/dirt_track_top"));
        getVariantBuilder(BlockRegistry.DIRT_TRACK_SLAB.get())
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).modelForState().modelFile(dirt_track_slab).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).modelForState().modelFile(dirt_track_slab_top).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).modelForState().modelFile(dirt_track).addModel();

        ModelFile grass_block = models().withExistingParent("grass_block", mcLoc("block/grass_block")).renderType("cutout");
        ModelFile grass_block_slab = models().withExistingParent("grass_slab", modLoc("block/overlay_slab"))
                .texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/grass_block_side"))
                .texture("top", mcLoc("block/grass_block_top")).texture("overlay", mcLoc("block/grass_block_side_overlay")).renderType("cutout");
        ModelFile grass_block_slab_top = models().withExistingParent("grass_block_top", modLoc("block/overlay_slab_top"))
                .texture("bottom", mcLoc("block/dirt")).texture("side", mcLoc("block/grass_block_side"))
                .texture("top", mcLoc("block/grass_block_top")).texture("overlay", mcLoc("block/grass_block_side_overlay")).renderType("cutout");
        getVariantBuilder(BlockRegistry.GRASS_SLAB.get())
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).modelForState().modelFile(grass_block_slab).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).modelForState().modelFile(grass_block_slab_top).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).modelForState().modelFile(grass_block).addModel();

        simpleBlock(BlockRegistry.DIRT_TRACK.get(), models().withExistingParent("dirt_track", mcLoc("block/dirt_path")).texture("side", modLoc("block/dirt_track_side")).texture("top", modLoc("block/dirt_track_top")));

        ModelFile normal = models().withExistingParent("rubber_log", mcLoc("block/cube_column")).texture("end", modLoc("block/rubber_log_top")).texture("side", modLoc("block/rubber_log"));
        ModelFile uncut = models().withExistingParent("rubber_log_drained", mcLoc("block/cube_column_horizontal")).texture("end", modLoc("block/rubber_log_top")).texture("north", modLoc("block/rubber_log_drained")).texture("south", modLoc("block/rubber_log")).texture("east", modLoc("block/rubber_log")).texture("west", modLoc("block/rubber_log")).texture("particle", modLoc("block/rubber_log_drained"));
        ModelFile cut = models().withExistingParent("rubber_log_open", mcLoc("block/cube_column_horizontal")).texture("end", modLoc("block/rubber_log_top")).texture("north", modLoc("block/rubber_log_open")).texture("south", modLoc("block/rubber_log")).texture("east", modLoc("block/rubber_log")).texture("west", modLoc("block/rubber_log")).texture("particle", modLoc("block/rubber_log_open"));
        getVariantBuilder(BlockRegistry.RUBBER_LOG_GENERATED.get())
            .partialState().with(RubberLogGeneratedBlock.OPEN, false).with(RubberLogGeneratedBlock.CUT, false).modelForState().modelFile(normal).addModel()
            .partialState().with(RubberLogGeneratedBlock.OPEN, false).with(RubberLogGeneratedBlock.CUT, true).modelForState().modelFile(normal).addModel()

            // CLOSED STATES
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, false).with(RubberLogGeneratedBlock.FACING, Direction.NORTH).modelForState().modelFile(uncut).rotationY(0).addModel()
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, false).with(RubberLogGeneratedBlock.FACING, Direction.SOUTH).modelForState().modelFile(uncut).rotationY(180).addModel()
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, false).with(RubberLogGeneratedBlock.FACING, Direction.EAST).modelForState().modelFile(uncut).rotationY(90).addModel()
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, false).with(RubberLogGeneratedBlock.FACING, Direction.WEST).modelForState().modelFile(uncut).rotationY(270).addModel()

            // OPEN STATES
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, true).with(RubberLogGeneratedBlock.FACING, Direction.NORTH).modelForState().modelFile(cut).rotationY(0).addModel()
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, true).with(RubberLogGeneratedBlock.FACING, Direction.SOUTH).modelForState().modelFile(cut).rotationY(180).addModel()
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, true).with(RubberLogGeneratedBlock.FACING, Direction.EAST).modelForState().modelFile(cut).rotationY(90).addModel()
            .partialState().with(RubberLogGeneratedBlock.OPEN, true).with(RubberLogGeneratedBlock.CUT, true).with(RubberLogGeneratedBlock.FACING, Direction.WEST).modelForState().modelFile(cut).rotationY(270).addModel();
        
    }

    private void hangingSignBlock(CeilingHangingSignBlock ceilingSign, WallHangingSignBlock wallSign, ResourceLocation texture) {
        ModelFile sign = this.models().sign(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(ceilingSign)).getPath(), texture);
        simpleBlock(ceilingSign, sign);
        simpleBlock(wallSign, sign);
    }

}
