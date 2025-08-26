package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.recipe.custom.Milling;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypeRegistry {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CommonConstants.MOD_ID);

    public static final RegistryObject<RecipeType<Milling>> MILLING_RECIPE_TYPE;

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }

    static {
        MILLING_RECIPE_TYPE = RECIPE_TYPES.register(CommonConstants.MOD_ID, () -> RecipeType.simple(new ResourceLocation(CommonConstants.MOD_ID, "milling")));
    }
}
