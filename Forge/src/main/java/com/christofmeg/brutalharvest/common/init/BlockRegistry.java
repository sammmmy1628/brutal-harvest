package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.block.*;
import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import com.christofmeg.brutalharvest.common.block.base.BaseCropBlock;
import com.christofmeg.brutalharvest.common.block.woodtype.BrutalWoodTypes;
import com.christofmeg.brutalharvest.common.blockentity.BrutalHangingSignBlockEntity;
import com.christofmeg.brutalharvest.common.blockentity.BrutalSignBlockEntity;
import com.christofmeg.brutalharvest.common.item.CookingBlockItem;
import com.christofmeg.brutalharvest.common.item.MillstoneBlockItem;
import com.christofmeg.brutalharvest.common.world.tree.RubberTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CommonConstants.MOD_ID);
    public static BlockBehaviour. Properties cropPropeties = BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);

//    public static final RegistryObject<Block> CROP_SUPPORT;
    public static final RegistryObject<Block> TOMATO;
    public static final RegistryObject<Block> LETTUCE;
    public static final RegistryObject<Block> COFFEE;
    public static final RegistryObject<Block> CORN;
    public static final RegistryObject<Block> CUCUMBER;
    public static final RegistryObject<Block> COTTON;
    public static final RegistryObject<Block> RAPESEED;
    public static final RegistryObject<Block> SUGAR_BEET;
    public static final RegistryObject<Block> STRAWBERRY;
//    public static final RegistryObject<Block> ONION;
    public static final RegistryObject<Block> BLUEBERRY;

    public static final RegistryObject<Block> WILD_TOMATO;
    public static final RegistryObject<Block> WILD_LETTUCE;
    public static final RegistryObject<Block> WILD_COFFEE;
    public static final RegistryObject<Block> WILD_CORN;
    public static final RegistryObject<Block> WILD_CUCUMBER;
    public static final RegistryObject<Block> WILD_COTTON;
    public static final RegistryObject<Block> WILD_RAPESEED;
    public static final RegistryObject<Block> WILD_SUGAR_BEET;
    public static final RegistryObject<Block> WILD_STRAWBERRY;

    public static final RegistryObject<Block> RUBBER_SAPLING;
    public static final RegistryObject<Block> RUBBER_LOG;
    public static final RegistryObject<Block> RUBBER_WOOD;
    public static final RegistryObject<Block> STRIPPED_RUBBER_LOG;
    public static final RegistryObject<Block> STRIPPED_RUBBER_WOOD;
    public static final RegistryObject<Block> RUBBER_PLANKS;
    public static final RegistryObject<Block> RUBBER_LEAVES;
    public static final RegistryObject<Block> RUBBER_LOG_GENERATED;
    public static final RegistryObject<Block> POTTED_RUBBER_SAPLING;
    public static final RegistryObject<SlabBlock> RUBBER_SLAB;
    public static final RegistryObject<StairBlock> RUBBER_STAIRS;
    public static final RegistryObject<PressurePlateBlock> RUBBER_PRESSURE_PLATE;
    public static final RegistryObject<ButtonBlock> RUBBER_BUTTON;
    public static final RegistryObject<FenceBlock> RUBBER_FENCE;
    public static final RegistryObject<FenceGateBlock> RUBBER_FENCE_GATE;
    public static final RegistryObject<StandingSignBlock> RUBBER_SIGN;
    public static final RegistryObject<WallSignBlock> RUBBER_WALL_SIGN;
    public static final RegistryObject<CeilingHangingSignBlock> RUBBER_HANGING_SIGN;
    public static final RegistryObject<WallHangingSignBlock> RUBBER_WALL_HANGING_SIGN;
    public static final RegistryObject<DoorBlock> RUBBER_DOOR;
    public static final RegistryObject<TrapDoorBlock> RUBBER_TRAPDOOR;

    public static final RegistryObject<Block> MILLSTONE;
    public static final RegistryObject<Block> PAN;
    public static final RegistryObject<Block> POT;
    public static final RegistryObject<Block> WOODEN_CUTTING_BOARD;
    public static final RegistryObject<Block> IRON_CUTTING_BOARD;

    public static final RegistryObject<Block> FAUCET;
    public static final RegistryObject<Block> RUBBER_CAULDRON;
    public static final RegistryObject<Block> DRIED_RUBBER_BLOCK;

    public static final RegistryObject<Block> FARMLAND_SLAB;
    public static final RegistryObject<Block> DIRT_SLAB;
    public static final RegistryObject<Block> GRASS_SLAB;
    public static final RegistryObject<Block> DIRT_PATH_SLAB;
    public static final RegistryObject<Block> DIRT_TRACK_SLAB;

    public static final RegistryObject<Block> DIRT_TRACK;

    private BlockRegistry() {
    }

    public static void init(@Nonnull IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

    static {
//        CROP_SUPPORT = BLOCKS.register("crop_support", () -> new CropSupportBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.BAMBOO_WOOD).pushReaction(PushReaction.DESTROY)));
//        ItemRegistry.ITEMS.register("crop_support", () -> new BlockItem(CROP_SUPPORT.get(), new Item.Properties()));

        TOMATO = BLOCKS.register("tomato", () -> new TomatoCropBlock(cropPropeties));
        LETTUCE = BLOCKS.register("lettuce", () -> new LettuceCropBlock(cropPropeties));
        COFFEE = BLOCKS.register("coffee", () -> new CoffeeCropBlock(cropPropeties));
        CORN = BLOCKS.register("corn", () -> new CornCropBlock(cropPropeties));
        CUCUMBER = BLOCKS.register("cucumber", () -> new CucumberCropBlock(cropPropeties));
        COTTON = BLOCKS.register("cotton", () -> new CottonCropBlock(cropPropeties));
        RAPESEED = BLOCKS.register("rapeseed", () -> new RapeseedCropBlock(cropPropeties));
        SUGAR_BEET = BLOCKS.register("sugar_beet", () -> new SugarBeetCropBlock(cropPropeties));
        STRAWBERRY = BLOCKS.register("strawberry", () -> new StrawberryCropBlock(cropPropeties));
//        ONION = BLOCKS.register("onion", () -> new OnionCropBlock(cropPropeties));
        BLUEBERRY = BLOCKS.register("blueberry", () -> new BlueberryBushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));

        WILD_TOMATO = BLOCKS.register("wild_tomato", () -> new WildCropBlock(
                Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)));
        WILD_LETTUCE = BLOCKS.register("wild_lettuce", () -> new WildCropBlock(
                Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)));
        WILD_COFFEE = BLOCKS.register("wild_coffee", () -> new WildCropBlock(
                Block.box(1.0F, 0.0F, 1.0F, 15.0F, 15.0F, 15.0F)));
        WILD_CORN = BLOCKS.register("wild_corn", () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));
        WILD_CUCUMBER = BLOCKS.register("wild_cucumber", () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));
        WILD_COTTON = BLOCKS.register("wild_cotton", () -> new WildCropBlock(
                Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0)));
        WILD_RAPESEED = BLOCKS.register("wild_rapeseed", () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));
        WILD_SUGAR_BEET = BLOCKS.register("wild_sugar_beet", () -> new WildCropBlock(
                Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0)));
        WILD_STRAWBERRY = BLOCKS.register("wild_strawberry", () -> new WildCropBlock(
                Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)));

        RUBBER_SAPLING = BLOCKS.register("rubber_sapling", () -> new SaplingBlock(new RubberTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
        ItemRegistry.ITEMS.register("rubber_sapling", () -> new BlockItem(RUBBER_SAPLING.get(), new Item.Properties()));

        RUBBER_LOG = BLOCKS.register("rubber_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)) {
            @Override
            public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
                if (context.getItemInHand().getItem() instanceof AxeItem) {
                    if (state.is(BlockRegistry.RUBBER_LOG.get())) {
                        return BlockRegistry.STRIPPED_RUBBER_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                    }
                }
                return super.getToolModifiedState(state, context, toolAction, simulate);
            }
        });
        ItemRegistry.ITEMS.register("rubber_log", () -> new BlockItem(RUBBER_LOG.get(), new Item.Properties()));

        RUBBER_WOOD = BLOCKS.register("rubber_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)) {
            @Override
            public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
                if (context.getItemInHand().getItem() instanceof AxeItem) {
                    if (state.is(BlockRegistry.RUBBER_WOOD.get())) {
                        return BlockRegistry.STRIPPED_RUBBER_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                    }
                }
                return super.getToolModifiedState(state, context, toolAction, simulate);
            }
        });
        ItemRegistry.ITEMS.register("rubber_wood", () -> new BlockItem(RUBBER_WOOD.get(), new Item.Properties()));

        STRIPPED_RUBBER_LOG = BLOCKS.register("stripped_rubber_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
        ItemRegistry.ITEMS.register("stripped_rubber_log", () -> new BlockItem(STRIPPED_RUBBER_LOG.get(), new Item.Properties()));

        STRIPPED_RUBBER_WOOD = BLOCKS.register("stripped_rubber_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
        ItemRegistry.ITEMS.register("stripped_rubber_wood", () -> new BlockItem(STRIPPED_RUBBER_WOOD.get(), new Item.Properties()));

        RUBBER_PLANKS = block("rubber_planks", BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS));

        RUBBER_LEAVES = BLOCKS.register("rubber_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
        ItemRegistry.ITEMS.register("rubber_leaves", () -> new BlockItem(RUBBER_LEAVES.get(), new Item.Properties()));

        RUBBER_LOG_GENERATED = BLOCKS.register("rubber_log_generated", () -> new RubberLogGeneratedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
        ItemRegistry.ITEMS.register("rubber_log_generated", () -> new BlockItem(RUBBER_LOG_GENERATED.get(), new Item.Properties()));

        RUBBER_SLAB = BLOCKS.register("rubber_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
        ItemRegistry.ITEMS.register("rubber_slab", () -> new BlockItem(RUBBER_SLAB.get(), new Item.Properties()));

        RUBBER_STAIRS = BLOCKS.register("rubber_stairs", () -> new StairBlock(Blocks.OAK_STAIRS::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
        ItemRegistry.ITEMS.register("rubber_stairs", () -> new BlockItem(RUBBER_STAIRS.get(), new Item.Properties()));

        RUBBER_PRESSURE_PLATE = BLOCKS.register("rubber_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BrutalWoodTypes.RUBBER_SET_TYPE));
        ItemRegistry.ITEMS.register("rubber_pressure_plate", () -> new BlockItem(RUBBER_PRESSURE_PLATE.get(), new Item.Properties()));

        RUBBER_BUTTON = BLOCKS.register("rubber_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BrutalWoodTypes.RUBBER_SET_TYPE, 30, true));
        ItemRegistry.ITEMS.register("rubber_button", () -> new BlockItem(RUBBER_BUTTON.get(), new Item.Properties()));

        RUBBER_FENCE = BLOCKS.register("rubber_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
        ItemRegistry.ITEMS.register("rubber_fence", () -> new BlockItem(RUBBER_FENCE.get(), new Item.Properties()));

        RUBBER_FENCE_GATE = BLOCKS.register("rubber_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), BrutalWoodTypes.RUBBER_WOOD_TYPE));
        ItemRegistry.ITEMS.register("rubber_fence_gate", () -> new BlockItem(RUBBER_FENCE_GATE.get(), new Item.Properties()));

        RUBBER_SIGN = BLOCKS.register("rubber_sign", () -> new StandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), BrutalWoodTypes.RUBBER_WOOD_TYPE) {
            @Override
            public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
                return new BrutalSignBlockEntity(pPos, pState);
            }
        });
        RUBBER_WALL_SIGN = BLOCKS.register("rubber_wall_sign", () -> new WallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN), BrutalWoodTypes.RUBBER_WOOD_TYPE) {
            @Override
            public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
                return new BrutalSignBlockEntity(pPos, pState);
            }
        });
        ItemRegistry.ITEMS.register("rubber_sign", () -> new SignItem(new Item.Properties().stacksTo(16), RUBBER_SIGN.get(), RUBBER_WALL_SIGN.get()));

        RUBBER_HANGING_SIGN = BLOCKS.register("rubber_hanging_sign", () -> new CeilingHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN), BrutalWoodTypes.RUBBER_WOOD_TYPE) {
            @Override
            public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
                return new BrutalHangingSignBlockEntity(pPos, pState);
            }
        });
        RUBBER_WALL_HANGING_SIGN = BLOCKS.register("rubber_wall_hanging_sign", () -> new WallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), BrutalWoodTypes.RUBBER_WOOD_TYPE) {
            @Override
            public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
                return new BrutalHangingSignBlockEntity(pPos, pState);
            }
        });
        ItemRegistry.ITEMS.register("rubber_hanging_sign", () -> new HangingSignItem(RUBBER_HANGING_SIGN.get(), RUBBER_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));

        RUBBER_DOOR = BLOCKS.register("rubber_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BrutalWoodTypes.RUBBER_SET_TYPE));
        ItemRegistry.ITEMS.register("rubber_door", () -> new BlockItem(RUBBER_DOOR.get(), new Item.Properties()));

        RUBBER_TRAPDOOR = BLOCKS.register("rubber_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), BrutalWoodTypes.RUBBER_SET_TYPE));
        ItemRegistry.ITEMS.register("rubber_trapdoor", () -> new BlockItem(RUBBER_TRAPDOOR.get(), new Item.Properties()));

        POTTED_RUBBER_SAPLING = BLOCKS.register("potted_rubber_sapling", () -> new FlowerPotBlock(null, RUBBER_SAPLING, BlockBehaviour.Properties.copy(Blocks.POTTED_OAK_SAPLING)));

        FARMLAND_SLAB = BLOCKS.register("farmland_slab", () -> new FarmlandSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).randomTicks().strength(0.6F).sound(SoundType.GRAVEL).isViewBlocking(BlockRegistry::always).isSuffocating(BlockRegistry::always)));
        ItemRegistry.ITEMS.register("farmland_slab", () -> new BlockItem(FARMLAND_SLAB.get(), new Item.Properties()));

        DIRT_SLAB = BLOCKS.register("dirt_slab", () -> new DirtSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).randomTicks().strength(0.5F).sound(SoundType.GRAVEL)));
        ItemRegistry.ITEMS.register("dirt_slab", () -> new BlockItem(DIRT_SLAB.get(), new Item.Properties()));

        GRASS_SLAB = BLOCKS.register("grass_slab", () -> new GrassSlab(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));
        ItemRegistry.ITEMS.register("grass_slab", () -> new BlockItem(GRASS_SLAB.get(), new Item.Properties()));

        DIRT_PATH_SLAB = BLOCKS.register("dirt_path_slab", () ->  new DirtPathSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.65F).sound(SoundType.GRASS).isViewBlocking(BlockRegistry::always).isSuffocating(BlockRegistry::always)));
        ItemRegistry.ITEMS.register("dirt_path_slab", () -> new BlockItem(DIRT_PATH_SLAB.get(), new Item.Properties()));

        DIRT_TRACK_SLAB = BLOCKS.register("dirt_track_slab", () ->  new DirtPathSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.65F).sound(SoundType.GRASS).isViewBlocking(BlockRegistry::always).isSuffocating(BlockRegistry::always)));
        ItemRegistry.ITEMS.register("dirt_track_slab", () -> new BlockItem(DIRT_TRACK_SLAB.get(), new Item.Properties()));

        DIRT_TRACK = BLOCKS.register("dirt_track", () -> new DirtPathBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.65F).sound(SoundType.GRASS).isViewBlocking(BlockRegistry::always).isSuffocating(BlockRegistry::always)));
        ItemRegistry.ITEMS.register("dirt_track", () -> new BlockItem(DIRT_TRACK.get(), new Item.Properties()));

        MILLSTONE = BLOCKS.register("millstone", () -> new MillstoneBlock(BlockBehaviour.Properties.of().noOcclusion().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 6.0F)));
        ItemRegistry.ITEMS.register("millstone", () -> new MillstoneBlockItem(new Item.Properties()));

        PAN = BLOCKS.register("pan", () -> new PanBlock(BlockBehaviour.Properties.of().noOcclusion().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(1.25F, 4.2F).lightLevel(state -> state.getValue(BaseCookingBlock.ON_CAMPFIRE) != BaseCookingBlock.OnCampfire.NONE ? 15 : 0)));
        ItemRegistry.ITEMS.register("pan", () -> new CookingBlockItem(PAN.get(), new Item.Properties()));

        POT = BLOCKS.register("pot", () -> new PotBlock(BlockBehaviour.Properties.of().noOcclusion().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(1.25F, 4.2F).lightLevel(state -> state.getValue(BaseCookingBlock.ON_CAMPFIRE) != BaseCookingBlock.OnCampfire.NONE ? 15 : 0)));
        ItemRegistry.ITEMS.register("pot", () -> new CookingBlockItem(POT.get(), new Item.Properties()));

        WOODEN_CUTTING_BOARD = BLOCKS.register("wooden_cutting_board", () -> new CuttingBoardBlock(BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.WOOD).strength(2.0F)));
        ItemRegistry.ITEMS.register("wooden_cutting_board", () -> new BlockItem(WOODEN_CUTTING_BOARD.get(), new Item.Properties()));

        IRON_CUTTING_BOARD = BLOCKS.register("iron_cutting_board", () -> new CuttingBoardBlock(BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.METAL).strength(2.0F, 4.2F)));
        ItemRegistry.ITEMS.register("iron_cutting_board", () -> new BlockItem(IRON_CUTTING_BOARD.get(), new Item.Properties()));

        FAUCET = BLOCKS.register("faucet", () -> new FaucetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion().randomTicks()));
        ItemRegistry.ITEMS.register("faucet", () -> new BlockItem(FAUCET.get(), new Item.Properties()));

        RUBBER_CAULDRON = BLOCKS.register("rubber_cauldron", () -> new RubberCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));

        DRIED_RUBBER_BLOCK = BLOCKS.register("dried_rubber_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK)));
        ItemRegistry.ITEMS.register("dried_rubber_block", () -> new BlockItem(DRIED_RUBBER_BLOCK.get(), new Item.Properties()));
    }

    private static RegistryObject<Block> block(String name, BlockBehaviour.Properties blockProperties) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(blockProperties));
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static boolean always(BlockState p_50775_, BlockGetter p_50776_, BlockPos p_50777_) {
        return true;
    }

}
