package com.christofmeg.brutalharvest.common.recipe.custom;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.RecipeSerializerRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.StringTag;
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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record Frying(ItemStack ingredient, int ticks, ItemStack result, Containers container) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return this.ingredient.is(container.getItem(0).getItem());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        ItemStack stack = result.copy();
        stack.getOrCreateTag();
        stack.addTagElement("container", StringTag.valueOf(this.container.getName()));
        int multiplier = container.getItem(0).getCount() / ingredient.getCount();
        stack.setCount(result.getCount() * multiplier);
        container.getItem(0).shrink(ingredient.getCount() * multiplier);
        container.setChanged();
        return stack;
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
        return new ResourceLocation(CommonConstants.MOD_ID, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.getItem())).getPath() + "_from_frying");
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
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            int i = GsonHelper.getAsInt(jsonObject, "time");
            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            Containers container = Containers.byName(GsonHelper.getAsString(jsonObject, "container"));
            return new Frying(input, i, stack, container);
        }

        @Override
        public @NotNull Frying fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            ItemStack input = friendlyByteBuf.readItem();
            int i = friendlyByteBuf.readInt();
            ItemStack itemStack = friendlyByteBuf.readItem();
            Containers container = friendlyByteBuf.readEnum(Containers.class);
            return new Frying(input, i, itemStack, container);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Frying frying) {
            friendlyByteBuf.writeItemStack(frying.ingredient, false);
            friendlyByteBuf.writeInt(frying.ticks);
            friendlyByteBuf.writeItemStack(frying.result, false);
            friendlyByteBuf.writeEnum(frying.container);
        }
    }

    public static class FryingItemsCache {
        private static final Set<Item> ALLOWED = new HashSet<>();
        private static final Set<Item> RESULTS = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Frying recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get())) {
                    ALLOWED.add(recipe.ingredient.getItem());
                }
            }
            return ALLOWED.contains(item);
        }

        public static boolean isResult(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (RESULTS.isEmpty()) {
                for (Frying recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get())) {
                    RESULTS.add(recipe.getResultItem(level.registryAccess()).getItem());
                }
            }
            return RESULTS.contains(item);
        }

        public static void invalidate() {
            ALLOWED.clear();
            RESULTS.clear();
        }
    }
}
