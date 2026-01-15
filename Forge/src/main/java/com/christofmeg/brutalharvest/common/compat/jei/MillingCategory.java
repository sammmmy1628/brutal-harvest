package com.christofmeg.brutalharvest.common.compat.jei;

import com.christofmeg.brutalharvest.common.compat.jei.base.BaseBrutalRecipeCategory;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Milling;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class MillingCategory extends BaseBrutalRecipeCategory<Milling> {

    protected MillingCategory(IGuiHelper helper) {
        super(helper, JEIBrutalPlugin.MILLING_RECIPE_TYPE, BlockRegistry.MILLSTONE.get());
    }

    @Override
    public void draw(@NotNull Milling recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(recipe.spins()), this.getCentralX() + 15, this.getCentralY() - 25, 0x111111, false);
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull Milling recipe, @NotNull IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipe, focuses);
        builder.addDrawable(this.RMBIcon, this.getCentralX() + 25, this.getCentralY() - 30);
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
