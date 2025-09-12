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
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public record Frying(Ingredient ingredient, int ticks, ItemStack result, boolean needsBowl) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return this.ingredient.test(container.getItem(0));
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
        return new ResourceLocation(CommonConstants.MOD_ID, result.getDescriptionId() + "_from_frying");
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.FRYING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.FRYING_RECIPE_TYPE.get();
    }

    public record FryingRecipeSerializer() implements RecipeSerializer<Frying> {

        @Override
        public @NotNull Frying fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            int i = GsonHelper.getAsInt(jsonObject, "time");
            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            boolean bowl = GsonHelper.getAsBoolean(jsonObject, "bowl");
            return new Frying(input, i, stack, bowl);
        }

        @Override
        public @NotNull Frying fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            ItemStack itemStack = friendlyByteBuf.readItem();
            int i = friendlyByteBuf.readInt();
            Ingredient input = Ingredient.fromNetwork(friendlyByteBuf);
            boolean bowl = friendlyByteBuf.getBoolean(0);
            return new Frying(input, i, itemStack, bowl);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Frying frying) {
            frying.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeInt(frying.ticks);
            friendlyByteBuf.writeItem(frying.result);
            friendlyByteBuf.writeBoolean(frying.needsBowl);
        }
    }

    public static class FryingItemsCache {
        private static final Set<Item> ALLOWED = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Frying recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get())) {
                    ALLOWED.add(recipe.ingredient.getItems()[0].getItem());
                }
            }
            return ALLOWED.contains(item);
        }

        public static void invalidate() {
            ALLOWED.clear();
        }
    }
}
