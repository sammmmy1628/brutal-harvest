package com.christofmeg.brutalharvest.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BrutalRecipeUtils {

    public static boolean areStacksMatchingNoOrder(Container container, Ingredient ingredient) {
        ItemStack[] stacks = ingredient.getItems();
        List<ItemStack> content = new ArrayList<>();
        for (int iter = 0; iter < container.getContainerSize(); iter++) {
            content.add(container.getItem(iter));
        }
        for (ItemStack itemStack : stacks) {
            if (content.isEmpty()) {
                return false;
            }
            if (!content.removeIf(testStack -> testStack.is(itemStack.getItem()) && testStack.getCount() >= itemStack.getCount())){
                return false;
            }
        }
        return content.isEmpty();
    }

    public static FluidStack fluidFromJson(JsonObject json) {
        if (json == null) {
            return FluidStack.EMPTY;
        }
        int amount = GsonHelper.getAsInt(json, "amount");
        String fluid_name = GsonHelper.getAsString(json, "fluid");
        String[] namespaceAndPath = fluid_name.split(":");
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(namespaceAndPath[0], namespaceAndPath[1]));
        if (fluid == null) {
            throw new IllegalArgumentException(fluid_name + " is not a valid fluid name!");
        }
        return new FluidStack(fluid, amount);
    }

    private static Ingredient.Value valueFromJsonWithCount(JsonObject jsonObject) {
        Ingredient.Value value = Ingredient.valueFromJson(jsonObject);
        for (ItemStack valueStack : value.getItems()) {
            valueStack.setCount(GsonHelper.getAsInt(jsonObject, "count", 1));
        }
        return value;
    }

    public static Ingredient ingredientFromJsonWithCount(@Nullable JsonElement pJson, boolean pCanBeEmpty) {
        if (pJson != null && !pJson.isJsonNull()) {
            if (pJson.isJsonObject()) {
                return Ingredient.fromValues(Stream.of(valueFromJsonWithCount(pJson.getAsJsonObject())));
            } else if (pJson.isJsonArray()) {
                JsonArray jsonarray = pJson.getAsJsonArray();
                if (jsonarray.isEmpty() && !pCanBeEmpty) {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                } else {
                    return Ingredient.fromValues(StreamSupport.stream(jsonarray.spliterator(), false).map((p_289756_) ->
                            valueFromJsonWithCount(GsonHelper.convertToJsonObject(p_289756_, "item"))));
                }
            } else {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    public static ItemStack matchingStack(Item matching, Ingredient ingredient) {
        for (ItemStack stack : ingredient.getItems()) {
            if (stack.is(matching) && !stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack assembleFromIItemHandler(IItemHandler handler, Ingredient ingredient, ItemStack resultStack) {
        short smallest = getResultMultiplier(handler, ingredient);
        for (int iter = 1; iter < 7; iter++) {
            ItemStack stack = handler.getStackInSlot(iter);
            if (!stack.isEmpty()) {
                stack.shrink(smallest * BrutalRecipeUtils.matchingStack(stack.getItem(), ingredient).getCount());
            }
        }
        resultStack.setCount(resultStack.getCount() * smallest);
        return resultStack;
    }

    public static boolean isSufficient(IItemHandler handler, Ingredient ingredient) {
        for (int iter = 1; iter < 7; iter++) {
            ItemStack stack =  handler.getStackInSlot(iter);
            if (stack.getCount() < BrutalRecipeUtils.matchingStack(stack.getItem(), ingredient).getCount()) {
                return false;
            }
        }
        return true;
    }

    public static short getResultMultiplier(IItemHandler handler, Ingredient ingredient) {
        short smallest = 64;
        for (int iter = 1; iter < 7; iter++) {
            ItemStack stack = handler.getStackInSlot(iter);
            if (!stack.isEmpty()) {
                int compare = stack.getCount() / BrutalRecipeUtils.matchingStack(stack.getItem(), ingredient).getCount();
                if (compare < smallest) {
                    smallest = (short) compare;
                }
            }
        }
        return smallest;
    }

    public static List<List<ItemStack>> getSortedIngredients(Ingredient ingredient) {
        List<List<ItemStack>> sortedIngredients = new ArrayList<>();
        List<ItemStack> oneTypeItems = new ArrayList<>();
        ItemStack compareItemStack = ingredient.getItems()[0];
        for (ItemStack ingredientStack : ingredient.getItems()) {
            if (!ingredientStack.is(compareItemStack.getItem())) {
                sortedIngredients.add(oneTypeItems);
                oneTypeItems = new ArrayList<>();
            }
            if (!ingredientStack.isEmpty()) {
                oneTypeItems.add(ingredientStack.copy());
            }
            compareItemStack = ingredientStack;
        }
        if (!oneTypeItems.contains(compareItemStack) && !compareItemStack.isEmpty()) {
            oneTypeItems.add(compareItemStack.copy());
        }
        if (!oneTypeItems.isEmpty()) {
            sortedIngredients.add(oneTypeItems);
        }
        return sortedIngredients;
    }
}
