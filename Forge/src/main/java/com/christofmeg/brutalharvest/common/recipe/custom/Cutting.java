package com.christofmeg.brutalharvest.common.recipe.custom;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.RecipeSerializerRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record Cutting(Ingredient ingredient, ItemStack result) implements Recipe<Container> {
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return new ResourceLocation(CommonConstants.MOD_ID, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).getPath() + "_from_cutting");
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.CUTTING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.CUTTING_RECIPE_TYPE.get();
    }

    public record CuttingRecipeSerializer() implements RecipeSerializer<Cutting> {

        @Override
        public @NotNull Cutting fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new Cutting(input, stack);
        }

        @Override
        public @NotNull Cutting fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            Ingredient input = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack stack = friendlyByteBuf.readItem();
            return new Cutting(input, stack);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Cutting cutting) {
            cutting.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(cutting.result);
        }
    }

    public static class CuttingItemsCache {

        private static final Map<Item, ItemStack> CUTTING_RESULTS = new HashMap<>();
        private static final Set<Item> ALLOWED = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Cutting recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.CUTTING_RECIPE_TYPE.get())) {
                    ALLOWED.add(recipe.ingredient.getItems()[0].getItem());
                }
            }
            return ALLOWED.contains(item);
        }

        public static ItemStack getCuttingResult(Level level, Item input) {
            if (level == null) {
                return ItemStack.EMPTY;
            }
            if (CUTTING_RESULTS.isEmpty()) {
                for (Cutting recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.CUTTING_RECIPE_TYPE.get())) {
                    CUTTING_RESULTS.put(recipe.ingredient.getItems()[0].getItem(), recipe.result);
                }
            }
            return CUTTING_RESULTS.get(input).copy();
        }

        public static void invalidate() {
            ALLOWED.clear();
            CUTTING_RESULTS.clear();
        }
    }
}
