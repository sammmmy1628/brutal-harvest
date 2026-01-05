package com.christofmeg.brutalharvest.common.recipe.custom;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.RecipeSerializerRegistry;
import com.christofmeg.brutalharvest.common.init.RecipeTypeRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public record Cooking(List<ItemStack> ingredients, int ticks, ItemStack result, FluidStack fluidResult, Containers container) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        for (ItemStack ingredient : this.ingredients) {
            for (byte iter = 0; iter < container.getContainerSize(); iter++) {
                if (!ingredient.is(container.getItem(iter).getItem()) && !container.getItem(iter).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
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
                int compare = stack.getCount() / this.matchingStack(stack.getItem()).getCount();
                if (compare < smallest) {
                    smallest = compare;
                }
            }
        }
        for (int iter = 0; iter < 6; iter++) {
            ItemStack stack = container.getItem(iter);
            if (!stack.isEmpty()) {
                stack.shrink(smallest * this.matchingStack(stack.getItem()).getCount());
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
        int smallest = 64;
        for (int iter = 1; iter < 7; iter++) {
            ItemStack stack = handler.getStackInSlot(iter);
            if (!stack.isEmpty()) {
                int compare = stack.getCount() / this.matchingStack(stack.getItem()).getCount();
                if (compare < smallest) {
                    smallest = compare;
                }
            }
        }
        for (int iter = 1; iter < 7; iter++) {
            ItemStack stack = handler.getStackInSlot(iter);
            if (!stack.isEmpty()) {
                stack.shrink(smallest * this.matchingStack(stack.getItem()).getCount());
            }
        }
        resultStack.setCount(resultStack.getCount() * smallest);
        return resultStack;
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
                    stack.shrink(this.matchingStack(stack.getItem()).getCount());
                }
            }
        }
        return fluidStack;
    }

    public boolean isSufficient(IItemHandler handler) {
        for (int iter = 1; iter < 7; iter++) {
            ItemStack stack =  handler.getStackInSlot(iter);
            if (stack.getCount() < this.matchingStack(stack.getItem()).getCount()) {
                return false;
            }
        }
        return true;
    }

    private ItemStack matchingStack(Item matching) {
        for (ItemStack stack : this.ingredients) {
            if (stack.is(matching) && !stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        if (result == ItemStack.EMPTY && fluidResult != FluidStack.EMPTY) {
            return new ResourceLocation(CommonConstants.MOD_ID, Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidResult.getFluid())).getPath() + "_from_cooking");
        } else {
            return new ResourceLocation(CommonConstants.MOD_ID, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.getItem())).getPath() + "_from_cooking");
        }
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
            String type = GsonHelper.getAsString(jsonObject, "result_type");
            List<ItemStack> input = new ArrayList<>();
            JsonArray array = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            for (JsonElement element : array) {
                input.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject()));
            }
            int i = GsonHelper.getAsInt(jsonObject, "time");
            ItemStack stack = type.equals("both") || type.equals("item") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "item_result")) : ItemStack.EMPTY;
            FluidStack fluid = type.equals("both") || type.equals("fluid") ? Milling.fluidFromJson(jsonObject) : FluidStack.EMPTY;
            Containers container = Containers.byName(GsonHelper.getAsString(jsonObject, "container"));
            return new Cooking(input, i, stack, fluid, container);
        }

        @Override
        public @NotNull Cooking fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            int i = friendlyByteBuf.readInt();
            List<ItemStack> input = new ArrayList<>();
            for (short iter = 0; iter < 6; iter++) {
                input.add(friendlyByteBuf.readItem());
            }
            ItemStack itemStack = friendlyByteBuf.readItem();
            FluidStack fluid = FluidStack.readFromPacket(friendlyByteBuf);
            Containers container = friendlyByteBuf.readEnum(Containers.class);
            return new Cooking(input, i, itemStack, fluid, container);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Cooking cooking) {
            for (ItemStack stack : cooking.ingredients) {
                friendlyByteBuf.writeItemStack(stack, false);
            }
            friendlyByteBuf.writeInt(cooking.ticks);
            friendlyByteBuf.writeItem(cooking.result);
            friendlyByteBuf.writeFluidStack(cooking.fluidResult);
            friendlyByteBuf.writeEnum(cooking.container);
        }
    }

    public static class CookingItemsCache {
        private static final Set<Item> ALLOWED = new HashSet<>();
        private static final Set<Fluid> FLUIDS = new HashSet<>();
        private static final Set<Item> RESULTS = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Cooking recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COOKING_RECIPE_TYPE.get())) {
                    recipe.ingredients.forEach(itemStack -> ALLOWED.add(itemStack.getItem()));
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
                    recipe.ingredients.forEach(itemStack -> ALLOWED.add(itemStack.getItem()));
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

        public static void invalidate() {
            ALLOWED.clear();
            FLUIDS.clear();
            RESULTS.clear();
        }
    }
}
