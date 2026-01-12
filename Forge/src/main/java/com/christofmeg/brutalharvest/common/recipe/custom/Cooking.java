package com.christofmeg.brutalharvest.common.recipe.custom;

import com.christofmeg.brutalharvest.common.init.RecipeSerializerRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.christofmeg.brutalharvest.common.util.BrutalRecipeUtils;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public record Cooking(Ingredient ingredient, ResourceLocation id, int ticks, ItemStack result, FluidStack fluidResult, BrutalContainers container, ItemStack remainder) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return BrutalRecipeUtils.areStacksMatchingNoOrder(container, this.ingredient);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        ItemStack resultStack = result.copy();
        resultStack.getOrCreateTag();
        resultStack.addTagElement("container", StringTag.valueOf(this.container.getName()));
        int smallest = 64;
        for (int iter = 0; iter < 6; iter++) {
            ItemStack stack = container.getItem(iter);
            if (!stack.isEmpty()) {
                int compare = stack.getCount() / BrutalRecipeUtils.matchingStack(stack.getItem(), this.ingredient).getCount();
                if (compare < smallest) {
                    smallest = compare;
                }
            }
        }
        for (int iter = 0; iter < 6; iter++) {
            ItemStack stack = container.getItem(iter);
            if (!stack.isEmpty()) {
                stack.shrink(smallest * BrutalRecipeUtils.matchingStack(stack.getItem(), this.ingredient).getCount());
            }
        }
        resultStack.setCount(resultStack.getCount() * smallest);
        container.setChanged();
        return resultStack;
    }

    public ItemStack assemble(IItemHandler handler) {
        ItemStack resultStack = result.copy();
        resultStack.getOrCreateTag();
        resultStack.addTagElement("container", StringTag.valueOf(this.container.getName()));
        return BrutalRecipeUtils.assembleFromIItemHandler(handler, this.ingredient, resultStack);
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.result.copy();
    }

    public FluidStack getResultFluid() {
        return this.fluidResult.copy();
    }

    public FluidStack assembleFluid(IItemHandler handler, boolean isOnlyResult) {
        FluidStack fluidStack = fluidResult.copy();
        if (isOnlyResult) {
            for (int iter = 1; iter < 7; iter++) {
                ItemStack stack = handler.getStackInSlot(iter);
                if (!stack.isEmpty()) {
                    stack.shrink(BrutalRecipeUtils.matchingStack(stack.getItem(), this.ingredient).getCount());
                }
            }
        }
        return fluidStack;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.COOKING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.COOKING_RECIPE_TYPE.get();
    }

    public record CookingRecipeSerializer() implements RecipeSerializer<Cooking> {

        @Override
        public @NotNull Cooking fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            Ingredient ingredient1 = BrutalRecipeUtils.ingredientFromJsonWithCount(jsonObject.get("ingredients"), true);
            int i = GsonHelper.getAsInt(jsonObject, "time");
            ItemStack stack = jsonObject.has("item_result") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "item_result")) : ItemStack.EMPTY;
            FluidStack fluid = jsonObject.has("fluid_result") ? BrutalRecipeUtils.fluidFromJson(GsonHelper.getAsJsonObject(jsonObject, "fluid_result")) : FluidStack.EMPTY;
            BrutalContainers container = BrutalContainers.valueOf(GsonHelper.getAsString(jsonObject, "container").toUpperCase());
            ItemStack remainder = jsonObject.has("remainder") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "remainder")) : ItemStack.EMPTY;
            return new Cooking(ingredient1, resourceLocation, i, stack, fluid, container, remainder);
        }

        @Override
        public @NotNull Cooking fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            int i = friendlyByteBuf.readInt();
            Ingredient ingredient1 = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack itemStack = friendlyByteBuf.readItem();
            FluidStack fluid = FluidStack.readFromPacket(friendlyByteBuf);
            BrutalContainers container = friendlyByteBuf.readEnum(BrutalContainers.class);
            ItemStack remainder = friendlyByteBuf.readItem();
            return new Cooking(ingredient1, resourceLocation, i, itemStack, fluid, container, remainder);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Cooking cooking) {
            cooking.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeInt(cooking.ticks);
            friendlyByteBuf.writeItem(cooking.result);
            friendlyByteBuf.writeFluidStack(cooking.fluidResult);
            friendlyByteBuf.writeEnum(cooking.container);
            friendlyByteBuf.writeItem(cooking.remainder);
        }
    }

    public static class CookingItemsCache {
        private static final Set<Item> ALLOWED = new HashSet<>();
        private static final Set<Fluid> FLUIDS = new HashSet<>();
        private static final Set<Item> RESULTS = new HashSet<>();
        private static final Set<Item> REQUIRES_CONTAINER = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Cooking recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get())) {
                    Arrays.stream(recipe.ingredient.getItems()).forEach(itemStack -> ALLOWED.add(itemStack.getItem()));
                    FLUIDS.add(recipe.fluidResult.getFluid());
                }
            }
            return ALLOWED.contains(item);
        }

        public static boolean isFluidValid(Level level, Fluid fluid) {
            if (level == null) {
                return false;
            }
            if (FLUIDS.isEmpty()) {
                for (Cooking recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get())) {
                    Arrays.stream(recipe.ingredient.getItems()).forEach(itemStack -> ALLOWED.add(itemStack.getItem()));
                    FLUIDS.add(recipe.fluidResult.getFluid());
                }
            }
            return FLUIDS.contains(fluid);
        }

        public static boolean isResult(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (RESULTS.isEmpty()) {
                for (Cooking recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get())) {
                    RESULTS.add(recipe.getResultItem(level.registryAccess()).getItem());
                }
            }
            return RESULTS.contains(item);
        }

        public static boolean requiresContainer(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (REQUIRES_CONTAINER.isEmpty()) {
                for (Cooking recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get())) {
                    if (recipe.container != BrutalContainers.NONE) {
                        REQUIRES_CONTAINER.add(recipe.result.getItem());
                    }
                }
            }
            return REQUIRES_CONTAINER.contains(item);
        }

        public static void invalidate() {
            ALLOWED.clear();
            FLUIDS.clear();
            RESULTS.clear();
            REQUIRES_CONTAINER.clear();
        }
    }
}
