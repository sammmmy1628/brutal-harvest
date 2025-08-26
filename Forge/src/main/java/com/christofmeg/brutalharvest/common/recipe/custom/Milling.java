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
import java.util.Set;

public record Milling(Ingredient ingredient, int spins, ItemStack itemOutput, FluidStack fluidOutput) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return this.itemOutput != null ? this.itemOutput.copy() : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.itemOutput != null ? this.itemOutput.copy() : ItemStack.EMPTY;
    }

    public FluidStack getResultFluid() {
        return this.fluidOutput != null ? this.fluidOutput.copy() : FluidStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        if (itemOutput.getCount() == 0) {
            return new ResourceLocation(CommonConstants.MOD_ID, fluidOutput.getFluid().getFluidType().getDescriptionId() + "_from_milling");
        } else {
            return new ResourceLocation(CommonConstants.MOD_ID, itemOutput.getDescriptionId() + "_from_milling");
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

    private static Fluid fluidFromJson(String fluid) {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluid));
    }

    public record MillingRecipeSerializer() implements RecipeSerializer<Milling> {

        @Override
        public @NotNull Milling fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"), false);
            int spins = GsonHelper.getAsInt(jsonObject, "spins");
            JsonObject item_result =  GsonHelper.getAsJsonObject(jsonObject, "item_result");
            JsonObject fluid_result = GsonHelper.getAsJsonObject(jsonObject, "fluid_result");
            byte item_amount = GsonHelper.getAsByte(item_result, "amount");
            Item item = ShapedRecipe.itemFromJson(item_result);
            int fluid_amount = GsonHelper.getAsInt(fluid_result, "amount");
            Fluid fluid = fluidFromJson(GsonHelper.getAsString(fluid_result, "fluid"));
            return new Milling(ingredient1, spins, new ItemStack(item, item_amount), new FluidStack(fluid, fluid_amount));
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

    public record MillingItemsCache() {
        private static final Set<Item> ALLOWED = new HashSet<>();

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

        public static void invalidate() {
            ALLOWED.clear();
        }
    }
}
