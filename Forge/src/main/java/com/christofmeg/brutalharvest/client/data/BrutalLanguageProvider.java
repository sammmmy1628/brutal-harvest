package com.christofmeg.brutalharvest.client.data;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.block.WildCropBlock;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.init.EnchantmentRegistry;
import com.christofmeg.brutalharvest.common.init.EntityTypeRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.codehaus.plexus.util.StringUtils;

import java.util.Objects;

public class BrutalLanguageProvider extends LanguageProvider {

    public BrutalLanguageProvider(PackOutput output, String locale) {
        super(output, CommonConstants.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        String locale = this.getName().replace("Languages: ", "");
        if (locale.equals("en_us")) {
            add("itemGroup." + CommonConstants.MOD_ID, CommonConstants.MOD_NAME);

            ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item -> !(item == ItemRegistry.CORN_SEEDS.get()) &&
                        !(item == BlockRegistry.RUBBER_LOG_GENERATED.get().asItem()) &&
                        !(item == ItemRegistry.CHEFS_HAT.get().asItem()) &&
                        !(item == ItemRegistry.CHEFS_APRON.get().asItem())
                )
                .forEach(item -> addItem(() -> item,
                StringUtils.capitaliseAllWords(item.getDescription().getString()
                    .replace("item." + CommonConstants.MOD_ID + ".", "")
                        .replace("block." + CommonConstants.MOD_ID + ".", "")
                    .replace("_", " ")
                )
            ));

            BlockRegistry.BLOCKS.getEntries().stream().filter(block -> block.get() instanceof WildCropBlock)
                    .forEach(wildCrop -> addBlock(wildCrop, StringUtils.capitaliseAllWords(
                            Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(wildCrop.get())).getPath()
                                    .replace('_', ' ')
                    )));

            addItem(ItemRegistry.CORN_SEEDS, "Corn Seeds (Kernel)");
            addItem(ItemRegistry.CHEFS_HAT, "Chef's Hat");
            addItem(ItemRegistry.CHEFS_APRON, "Chef's Apron");
            add("block." + CommonConstants.MOD_ID + "." + "rubber_log_generated", "Rubber Log");

            add(BlockRegistry.POTTED_RUBBER_SAPLING.get(), "Potted Rubber Sapling");
            add(BlockRegistry.WILD_CORN.get(), "Wild Corn");
            add(BlockRegistry.WILD_CUCUMBER.get(), "Wild Cucumber");
            add(BlockRegistry.WILD_RAPESEED.get(), "Wild Rapeseed");

            add(BlockRegistry.RUBBER_CAULDRON.get(), "Rubber Cauldron");

            add("advancement" + "." + CommonConstants.MOD_ID + "." + "root" + ".desc", "Obtain some tomatoes");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "rotten_tomatoes", "Rotten Tomatoes");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "rotten_tomatoes" + ".desc", "Throw a tomato at a villager");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "grim_reaper", "Grim Reaper");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "grim_reaper" + ".desc", "Craft a scythe");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "reaperang", "Reaparang");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "reaperang" + ".desc", "Throw a scythe with the Boomerang enchantment");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "throwing_knives", "Throwing Knives?");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "throwing_knives" + ".desc", "Throw a knife with the Launch enchantment");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "corn_seeds", "Colonel Cornelius Cornwall");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "corn_seeds" + ".desc", "Plant a Corn Seed (Kernel)");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "corn", "IT'S CORN");
            add("advancement" + "." + CommonConstants.MOD_ID + "." + "corn" + ".desc", "Obtain some corn");

            add("sounds." + CommonConstants.MOD_ID + "." + "tomato_splat", "Tomato Splat");

            EnchantmentRegistry.ENCHANTMENTS.getEntries().stream().map(RegistryObject::get)
                .forEach(enchantment -> addEnchantment(() -> enchantment,
                    StringUtils.capitaliseAllWords(enchantment.getDescriptionId()
                        .replace("enchantment." + CommonConstants.MOD_ID + ".", "")
                    )
                ));

            add("enchantment." + CommonConstants.MOD_ID + "." + "boomerang" + ".desc", "Allows the Scythe to automatically return after being thrown");
            add("enchantment." + CommonConstants.MOD_ID + "." + "launch" + ".desc", "Allows the Knife to be thrown");

            addEntityType(EntityTypeRegistry.TOMATO_PROJECTILE, "Tomato");
            addEntityType(EntityTypeRegistry.THROWN_SCYTHE, "Scythe");
            addEntityType(EntityTypeRegistry.THROWN_KNIFE, "Knife");

            add("fluid_type." + CommonConstants.MOD_ID + ".rapeseed_oil", "Rapeseed Oil");
            add("fluid_type." + CommonConstants.MOD_ID + ".coffee", "Coffee");
            add("fluid_type." + CommonConstants.MOD_ID + ".rubber", "Rubber");
            add("fluid_type." + CommonConstants.MOD_ID + ".stirred_egg", "Stirred Egg");
            add("fluid_type." + CommonConstants.MOD_ID + ".blueberry_jam", "Blueberry Jam");
            add("fluid_type." + CommonConstants.MOD_ID + ".strawberry_jam", "Strawberry Jam");

            add("container." + CommonConstants.MOD_ID + ".seedSatchel", "Seed Satchel");
            add("entity." + CommonConstants.MOD_ID + ".brutal_boat.rubber", "Rubber Boat");
            add("entity." + CommonConstants.MOD_ID + ".brutal_chest_boat.rubber", "Rubber Boat with Chest");

            add("recipe." + CommonConstants.MOD_ID + ".milling", "Milling");
            add("recipe." + CommonConstants.MOD_ID + ".frying", "Frying");
            add("recipe." + CommonConstants.MOD_ID + ".cooking", "Cooking");
            add("recipe." + CommonConstants.MOD_ID + ".cutting", "Cutting");
        }
    }

}