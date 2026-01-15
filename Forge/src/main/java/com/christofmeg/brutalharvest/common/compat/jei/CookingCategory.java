package com.christofmeg.brutalharvest.common.compat.jei;

import com.christofmeg.brutalharvest.common.compat.jei.base.BaseBrutalRecipeCategory;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Cooking;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CookingCategory extends BaseBrutalRecipeCategory<Cooking> {

    protected CookingCategory(IGuiHelper helper) {
        super(helper, JEIBrutalPlugin.COOKING_RECIPE_TYPE, BlockRegistry.POT.get());
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, @NotNull Cooking cooking, @NotNull IFocusGroup iFocusGroup) {
        drawMultipleIngredients(iRecipeLayoutBuilder, cooking.ingredient());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 45, this.getCentralY()).addItemStack(cooking.getResultItem(null));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 65, this.getCentralY()).addFluidStack(cooking.fluidResult().getFluid(), cooking.fluidResult().getAmount());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 85, this.getCentralY()).addItemStack(cooking.remainder());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.RENDER_ONLY, this.getCentralX(), this.getCentralY() - 20).addItemLike(cooking.container().getItem());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.RENDER_ONLY, this.getCentralX(), this.getCentralY() + 20).addItemStacks(List.of(Items.CAMPFIRE.getDefaultInstance(), Items.SOUL_CAMPFIRE.getDefaultInstance()));
    }
}
