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

public record Milling(Ingredient ingredient, ResourceLocation id, int spins, ItemStack itemOutput, FluidStack fluidOutput, BrutalContainers container, ItemStack remainder) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return BrutalRecipeUtils.areStacksMatchingNoOrder(container, this.ingredient);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        ItemStack resultStack = this.itemOutput.copy();
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
        ItemStack resultStack = this.itemOutput.copy();
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
        return this.itemOutput.copy();
    }

    public FluidStack getResultFluid() {
        return this.fluidOutput.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.MILLING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.MILLING_RECIPE_TYPE.get();
    }

    public record MillingRecipeSerializer() implements RecipeSerializer<Milling> {

        @Override
        public @NotNull Milling fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            Ingredient ingredients1 = BrutalRecipeUtils.ingredientFromJsonWithCount(jsonObject.get("ingredients"), true);
            int spins = GsonHelper.getAsInt(jsonObject, "spins");
            FluidStack fluid_result = jsonObject.has("fluid_result") ? BrutalRecipeUtils.fluidFromJson(GsonHelper.getAsJsonObject(jsonObject, "fluid_result")) : FluidStack.EMPTY;
            ItemStack result = jsonObject.has("item_result") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "item_result")) : ItemStack.EMPTY;
            BrutalContainers container = BrutalContainers.valueOf(GsonHelper.getAsString(jsonObject, "container").toUpperCase());
            ItemStack remainder = jsonObject.has("remainder") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "remainder")) : ItemStack.EMPTY;
            return new Milling(ingredients1, resourceLocation, spins, result, fluid_result, container, remainder);
        }

        @Override
        public Milling fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            Ingredient ingredient1 = Ingredient.fromNetwork(friendlyByteBuf);
            int spins = friendlyByteBuf.readInt();
            ItemStack stack = friendlyByteBuf.readItem();
            FluidStack fluid = FluidStack.readFromPacket(friendlyByteBuf);
            BrutalContainers container = friendlyByteBuf.readEnum(BrutalContainers.class);
            ItemStack remainder = friendlyByteBuf.readItem();
            return new Milling(ingredient1, resourceLocation, spins, stack, fluid, container, remainder);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Milling milling) {
            milling.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeInt(milling.spins);
            friendlyByteBuf.writeItem(milling.itemOutput);
            milling.fluidOutput.writeToPacket(friendlyByteBuf);
            friendlyByteBuf.writeEnum(milling.container);
            friendlyByteBuf.writeItem(milling.remainder);
        }
    }

    public static class MillingItemsCache {
        private static final Set<Item> ALLOWED = new HashSet<>();
        private static final Set<Fluid> ALLOWED_FLUIDS = new HashSet<>();
        private static final Set<Item> REQUIRES_CONTAINER = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Milling recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.MILLING_RECIPE_TYPE.get())) {
                    Arrays.stream(recipe.ingredient.getItems()).forEach(itemStack -> ALLOWED.add(itemStack.getItem()));
                }
            }
            return ALLOWED.contains(item);
        }

        public static boolean isFluidValid(Level level, Fluid fluid) {
            if (level == null) {
                return false;
            }
            if (ALLOWED_FLUIDS.isEmpty()) {
                for (Milling recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.MILLING_RECIPE_TYPE.get())) {
                    if (recipe.fluidOutput != FluidStack.EMPTY) {
                        ALLOWED_FLUIDS.add(recipe.fluidOutput.getFluid());
                    }
                }
            }
            return ALLOWED_FLUIDS.contains(fluid);
        }

        public static boolean requiresContainer(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (REQUIRES_CONTAINER.isEmpty()) {
                for (Milling recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.MILLING_RECIPE_TYPE.get())) {
                    if (recipe.container != BrutalContainers.NONE) {
                        REQUIRES_CONTAINER.add(recipe.itemOutput.getItem());
                    }
                }
            }
            return REQUIRES_CONTAINER.contains(item);
        }

        public static void invalidate() {
            ALLOWED.clear();
            ALLOWED_FLUIDS.clear();
            REQUIRES_CONTAINER.clear();
        }
    }
}
