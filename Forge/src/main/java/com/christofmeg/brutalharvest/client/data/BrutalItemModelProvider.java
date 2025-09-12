package com.christofmeg.brutalharvest.client.data;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.item.ScytheItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BrutalItemModelProvider extends ItemModelProvider {

    private static final ResourceLocation RUBBER_PLANKS_LOCATION = new ResourceLocation(CommonConstants.MOD_ID, "block/rubber_planks");

    public BrutalItemModelProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, CommonConstants.MOD_ID, existingFileHelper);
    }

    @Override
    public @NotNull String getName() {
        return CommonConstants.MOD_ID + " - ItemModel";
    }

    @Override
    protected void registerModels() {
        ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item -> (!(item instanceof BlockItem)) && (!(item instanceof ScytheItem)))
                .forEach(this::basicItem);

        ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item -> (item instanceof ItemNameBlockItem))
                .forEach(this::basicItem);

        ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item -> (item instanceof ScytheItem))
                .forEach(this::handHeldItem);

        basicItem(ItemRegistry.SEED_SATCHEL.get()).override().predicate(modLoc("filled"), 1.0F).model(basicItem(modLoc("seed_satchel_filled"))).end();
        saplingItem(BlockRegistry.RUBBER_SAPLING);
        withExistingParent(getItemName(BlockRegistry.RUBBER_LOG.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_LOG.get())));
        withExistingParent(getItemName(BlockRegistry.RUBBER_WOOD.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_WOOD.get())));
        withExistingParent(getItemName(BlockRegistry.STRIPPED_RUBBER_LOG.get()), modLoc("block/" + getItemName(BlockRegistry.STRIPPED_RUBBER_LOG.get())));
        withExistingParent(getItemName(BlockRegistry.STRIPPED_RUBBER_WOOD.get()), modLoc("block/" + getItemName(BlockRegistry.STRIPPED_RUBBER_WOOD.get())));
        withExistingParent(getItemName(BlockRegistry.RUBBER_PLANKS.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_PLANKS.get())));
        withExistingParent(getItemName(BlockRegistry.RUBBER_LEAVES.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_LEAVES.get())));
        withExistingParent(getItemName(BlockRegistry.RUBBER_LOG_GENERATED.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_LOG.get())));
        withExistingParent(getItemName(BlockRegistry.FARMLAND_SLAB.get()), modLoc("block/" + getItemName(BlockRegistry.FARMLAND_SLAB.get())));
        withExistingParent(getItemName(BlockRegistry.DIRT_SLAB.get()), modLoc("block/" + getItemName(BlockRegistry.DIRT_SLAB.get())));
        withExistingParent(getItemName(BlockRegistry.GRASS_SLAB.get()), modLoc("block/" + getItemName(BlockRegistry.GRASS_SLAB.get())));
        withExistingParent(getItemName(BlockRegistry.DIRT_PATH_SLAB.get()), modLoc("block/" + getItemName(BlockRegistry.DIRT_PATH_SLAB.get())));
        withExistingParent(getItemName(BlockRegistry.DIRT_TRACK_SLAB.get()), modLoc("block/" + getItemName(BlockRegistry.DIRT_TRACK_SLAB.get())));
        withExistingParent(getItemName(BlockRegistry.DIRT_TRACK.get()), modLoc("block/" + getItemName(BlockRegistry.DIRT_TRACK.get())));
        withExistingParent(CommonConstants.MOD_ID + ":block/potted_rubber_sapling", mcLoc("block/flower_pot_cross"))
                .texture("plant", modLoc("block/" + getItemName(BlockRegistry.RUBBER_SAPLING.get()))).renderType(mcLoc("cutout"));
        slab(CommonConstants.MOD_ID + ":block/rubber_slab", RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION);
        slabTop(CommonConstants.MOD_ID + ":block/rubber_slab_top", RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION);
        stairs(CommonConstants.MOD_ID + ":block/rubber_stairs", RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION);
        stairsInner(CommonConstants.MOD_ID + ":block/rubber_stairs_inner", RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION);
        stairsOuter(CommonConstants.MOD_ID + ":block/rubber_stairs_outer", RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION, RUBBER_PLANKS_LOCATION);
        pressurePlate(CommonConstants.MOD_ID + ":block/rubber_pressure_plate", RUBBER_PLANKS_LOCATION);
        button(CommonConstants.MOD_ID + ":block/rubber_button", RUBBER_PLANKS_LOCATION);
        buttonInventory(CommonConstants.MOD_ID + ":block/rubber_button_inventory", RUBBER_PLANKS_LOCATION);
        buttonPressed(CommonConstants.MOD_ID + ":block/rubber_button_pressed", RUBBER_PLANKS_LOCATION);
        fencePost(CommonConstants.MOD_ID + ":block/rubber_fence_post", RUBBER_PLANKS_LOCATION);
        fenceSide(CommonConstants.MOD_ID + ":block/rubber_fence_side", RUBBER_PLANKS_LOCATION);
        fenceInventory(CommonConstants.MOD_ID + ":block/rubber_fence_inventory", RUBBER_PLANKS_LOCATION);
        fenceGate(CommonConstants.MOD_ID + ":block/rubber_fence_gate", RUBBER_PLANKS_LOCATION);
        fenceGateOpen(CommonConstants.MOD_ID + ":block/rubber_fence_gate_open", RUBBER_PLANKS_LOCATION);
        fenceGateWall(CommonConstants.MOD_ID + ":block/rubber_fence_gate_wall", RUBBER_PLANKS_LOCATION);
        fenceGateWallOpen(CommonConstants.MOD_ID + ":block/rubber_fence_gate_wall_open", RUBBER_PLANKS_LOCATION);
        withExistingParent(getItemName(BlockRegistry.RUBBER_SLAB.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_SLAB.get())));
        withExistingParent(getItemName(BlockRegistry.RUBBER_STAIRS.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_STAIRS.get())));
        withExistingParent(getItemName(BlockRegistry.RUBBER_PRESSURE_PLATE.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_PRESSURE_PLATE.get())));
        withExistingParent(getItemName(BlockRegistry.RUBBER_BUTTON.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_BUTTON.get()) + "_inventory"));
        withExistingParent(getItemName(BlockRegistry.RUBBER_FENCE.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_FENCE.get()) + "_inventory"));
        withExistingParent(getItemName(BlockRegistry.RUBBER_FENCE_GATE.get()), modLoc("block/" + getItemName(BlockRegistry.RUBBER_FENCE_GATE.get())));
    }

    @SuppressWarnings("deprecation")
    private String getItemName(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem()).getPath();
    }

    private void saplingItem(RegistryObject<Block> item) {
        withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CommonConstants.MOD_ID, "block/" + item.getId().getPath()))
                .renderType(mcLoc("cutout"));
    }

    private void handHeldItem(Item item) {
        this.handHeldItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    private void handHeldItem(ResourceLocation item) {
        this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }

}