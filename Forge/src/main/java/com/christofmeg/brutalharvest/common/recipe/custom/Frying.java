package com.christofmeg.brutalharvest.common.recipe.custom;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.init.FluidRegistry;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record Frying(Ingredient ingredient, ResourceLocation id, FluidStack fluidIngredient, int ticks, ItemStack result, BrutalContainers container) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return this.ingredient.test(container.getItem(0)) || (this.ingredient.isEmpty() && !this.fluidIngredient.isEmpty());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        ItemStack stack = result.copy();
        stack.getOrCreateTag();
        stack.addTagElement("container", StringTag.valueOf(this.container.getName()));
        if (container.getItem(0).getCount() != 0) {
            int multiplier = container.getItem(0).getCount() / ingredient.getItems()[0].getCount();
            stack.setCount(result.getCount() * multiplier);
            container.getItem(0).shrink(ingredient.getItems()[0].getCount() * multiplier);
        }
        container.setChanged();
        return stack;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
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
            Ingredient input = jsonObject.has("ingredient") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"), true) : Ingredient.EMPTY;
            FluidStack fluidStack = BrutalRecipeUtils.fluidFromJson(GsonHelper.getAsJsonObject(jsonObject, "fluid_ingredient", null));
            int i = GsonHelper.getAsInt(jsonObject, "time");
            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            BrutalContainers container = BrutalContainers.valueOf(GsonHelper.getAsString(jsonObject, "container").toUpperCase());
            return new Frying(input, resourceLocation, fluidStack, i, stack, container);
        }

        @Override
        public @NotNull Frying fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            Ingredient input = Ingredient.fromNetwork(friendlyByteBuf);
            FluidStack fluidStack = friendlyByteBuf.readFluidStack();
            int i = friendlyByteBuf.readInt();
            ItemStack itemStack = friendlyByteBuf.readItem();
            BrutalContainers container = friendlyByteBuf.readEnum(BrutalContainers.class);
            return new Frying(input, resourceLocation, fluidStack, i, itemStack, container);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Frying frying) {
            friendlyByteBuf.writeItemStack(frying.ingredient.getItems()[0], false);
            friendlyByteBuf.writeFluidStack(frying.fluidIngredient);
            friendlyByteBuf.writeInt(frying.ticks);
            friendlyByteBuf.writeItemStack(frying.result, false);
            friendlyByteBuf.writeEnum(frying.container);
        }
    }

    public static class FryingItemsCache {
        private static final Set<Item> ALLOWED = new HashSet<>();
        private static final Set<Item> RESULTS = new HashSet<>();
        private static final Set<Fluid> FLUIDS = new HashSet<>();
        private static final Set<Item> REQUIRES_CONTAINER = new HashSet<>();

        public static boolean isValid(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (ALLOWED.isEmpty()) {
                for (Frying recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get())) {
                    if (!recipe.ingredient.isEmpty()) {
                        ALLOWED.add(recipe.ingredient.getItems()[0].getItem());
                    }
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

        public static boolean isValidFluid(Level level, Fluid fluid) {
            if (level == null) {
                return false;
            }
            if (FLUIDS.isEmpty()) {
                FLUIDS.add(FluidRegistry.SOURCE_RAPESEED_OIL.get());
                for (Frying frying : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get())) {
                    if (!frying.fluidIngredient.isEmpty()) {
                        FLUIDS.add(frying.fluidIngredient.getFluid());
                    }
                }
            }
            return FLUIDS.contains(fluid);
        }

        public static boolean requiresContainer(Level level, Item item) {
            if (level == null) {
                return false;
            }
            if (REQUIRES_CONTAINER.isEmpty()) {
                for (Frying recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FRYING_RECIPE_TYPE.get())) {
                    if (recipe.container != BrutalContainers.NONE) {
                        REQUIRES_CONTAINER.add(recipe.result.getItem());
                    }
                }
            }
            return REQUIRES_CONTAINER.contains(item);
        }

        public static void invalidate() {
            ALLOWED.clear();
            RESULTS.clear();
            FLUIDS.clear();
            REQUIRES_CONTAINER.clear();
        }
    }
}
