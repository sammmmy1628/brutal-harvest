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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record Milling(Ingredient ingredient, int spins, ItemStack itemOutput, FluidStack fluidOutput) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return this.itemOutput.copy();
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
        if (itemOutput == ItemStack.EMPTY && fluidOutput != FluidStack.EMPTY) {
            return new ResourceLocation(CommonConstants.MOD_ID, Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidOutput.getFluid())).getPath() + "_from_milling");
        } else {
            return new ResourceLocation(CommonConstants.MOD_ID, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemOutput.getItem())).getPath() + "_from_milling");
        }
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.MILLING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.MILLING_RECIPE_TYPE.get();
    }

    public ItemStack getInputStack() {
        return this.ingredient.getItems()[0];
    }

    public static FluidStack fluidFromJson(JsonObject json) {
        JsonObject fluidObject = GsonHelper.getAsJsonObject(json, "fluid_result");
        int amount = GsonHelper.getAsInt(fluidObject, "amount");
        String fluid_name = GsonHelper.getAsString(fluidObject, "fluid");
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluid_name));
        if (fluid == null) {
            throw new IllegalArgumentException(fluid_name + " is not a valid fluid name!");
        }
        return new FluidStack(fluid, amount);
    }

    public record MillingRecipeSerializer() implements RecipeSerializer<Milling> {

        @Override
        public @NotNull Milling fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            String results = GsonHelper.getAsString(jsonObject, "result_type");
            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"), false);
            int spins = GsonHelper.getAsInt(jsonObject, "spins");
            FluidStack fluid_result = results.equals("both") || results.equals("fluid") ? fluidFromJson(jsonObject) : FluidStack.EMPTY;
            ItemStack result = results.equals("both") || results.equals("item") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "item_result")) : ItemStack.EMPTY;
            return new Milling(ingredient1, spins, result, fluid_result);
        }

        @Override
        public Milling fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            Ingredient ingredient1 = Ingredient.fromNetwork(friendlyByteBuf);
            int spins = friendlyByteBuf.readInt();
            ItemStack stack = friendlyByteBuf.readItem();
            FluidStack fluid = FluidStack.readFromPacket(friendlyByteBuf);
            return new Milling(ingredient1, spins, stack, fluid);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Milling milling) {
            milling.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeInt(milling.spins);
            friendlyByteBuf.writeItem(milling.itemOutput);
            milling.fluidOutput.writeToPacket(friendlyByteBuf);
        }
    }

    public static class MillingItemsCache {
        private static final Set<Item> ALLOWED = new HashSet<>();
        private static final Set<Fluid> ALLOWED_FLUIDS = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Milling recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.MILLING_RECIPE_TYPE.get())) {
                    ALLOWED.add(recipe.getInputStack().getItem());
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

        public static void invalidate() {
            ALLOWED.clear();
            ALLOWED_FLUIDS.clear();
        }
    }
}
