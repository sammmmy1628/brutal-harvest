package com.christofmeg.brutalharvest.common.compat.jei;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Cooking;
import com.christofmeg.brutalharvest.common.recipe.custom.Cutting;
import com.christofmeg.brutalharvest.common.recipe.custom.Frying;
import com.christofmeg.brutalharvest.common.recipe.custom.Milling;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JEIBrutalPlugin implements IModPlugin {

    public static final RecipeType<Milling> MILLING_RECIPE_TYPE = new RecipeType<>(new ResourceLocation(CommonConstants.MOD_ID, "milling"), Milling.class);
    public static final RecipeType<Frying> FRYING_RECIPE_TYPE = new RecipeType<>(new ResourceLocation(CommonConstants.MOD_ID, "frying"), Frying.class);
    public static final RecipeType<Cooking> COOKING_RECIPE_TYPE = new RecipeType<>(new ResourceLocation(CommonConstants.MOD_ID, "cooking"), Cooking.class);
    public static final RecipeType<Cutting> CUTTING_RECIPE_TYPE = new RecipeType<>(new ResourceLocation(CommonConstants.MOD_ID, "cutting"), Cutting.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(CommonConstants.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MillingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FryingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CookingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CuttingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            RecipeManager recipeManager = level.getRecipeManager();
            registration.addRecipes(MILLING_RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeTypeRegistry.MILLING_RECIPE_TYPE.get()));
            registration.addRecipes(FRYING_RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get()));
            registration.addRecipes(COOKING_RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get()));
            registration.addRecipes(CUTTING_RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeTypeRegistry.CUTTING_RECIPE_TYPE.get()));
            registration.addIngredientInfo(List.of(ItemRegistry.RUBBER_BUCKET.get().getDefaultInstance(), ItemRegistry.RUBBER_BOWL.get().getDefaultInstance()),
                    VanillaTypes.ITEM_STACK, Component.literal("To obtain rubber try interacting with rubber tree logs using a knife. When you succeed in creating an opening, put a faucet on it, then place a cauldron below and wait for liquid rubber to accumulate."));
        }
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
    }
}
