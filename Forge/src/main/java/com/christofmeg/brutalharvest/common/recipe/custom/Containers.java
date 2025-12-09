package com.christofmeg.brutalharvest.common.recipe.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum Containers {
    NONE("none", Items.AIR),
    BOTTLE("bottle", Items.GLASS_BOTTLE),
    BOWL("bowl", Items.BOWL),
    BUCKET("bucket", Items.BUCKET);

    private final String name;
    private final Item item;

    Containers(String name, Item item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return this.name;
    }

    public Item getItem() {
        return this.item;
    }

    public static Containers byName(String name) {
        for (Containers container : Containers.values()) {
            if (container.getName().equals(name)) {
                return container;
            }
        }
        return Containers.NONE;
    }
}
