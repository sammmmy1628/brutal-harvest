package com.christofmeg.brutalharvest.common.recipe.custom;

import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum BrutalContainers {
    NONE("none", Items.AIR),
    BOTTLE("bottle", Items.GLASS_BOTTLE),
    BOWL("bowl", Items.BOWL),
    BUCKET("bucket", Items.BUCKET),
    JAR("jar", ItemRegistry.JAR.get());

    private final String name;
    private final Item item;

    BrutalContainers(String name, Item item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return this.name;
    }

    public Item getItem() {
        return this.item;
    }
}
