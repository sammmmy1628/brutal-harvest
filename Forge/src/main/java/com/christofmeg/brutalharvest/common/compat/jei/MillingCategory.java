package com.christofmeg.brutalharvest.common.compat.jei;

import com.christofmeg.brutalharvest.common.compat.jei.base.BaseBrutalRecipeCategory;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Milling;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.jetbrains.annotations.NotNull;

public class MillingCategory extends BaseBrutalRecipeCategory<Milling> {

    protected MillingCategory(IGuiHelper helper) {
        super(helper, JEIBrutalPlugin.MILLING_RECIPE_TYPE, BlockRegistry.MILLSTONE.get());
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, @NotNull Milling milling, @NotNull IFocusGroup iFocusGroup) {
        drawMultipleIngredients(iRecipeLayoutBuilder, milling.ingredient());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 45, this.getCentralY()).addItemStack(milling.getResultItem(null));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 65, this.getCentralY()).addFluidStack(milling.fluidOutput().getFluid(), milling.fluidOutput().getAmount());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 85, this.getCentralY()).addItemStack(milling.remainder());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.RENDER_ONLY, this.getCentralX(), this.getCentralY() - 20).addItemLike(milling.container().getItem());
    }
}
