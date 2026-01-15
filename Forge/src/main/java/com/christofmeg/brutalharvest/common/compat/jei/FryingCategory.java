package com.christofmeg.brutalharvest.common.compat.jei;

import com.christofmeg.brutalharvest.common.compat.jei.base.BaseBrutalRecipeCategory;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Frying;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FryingCategory extends BaseBrutalRecipeCategory<Frying> {

    protected FryingCategory(IGuiHelper helper) {
        super(helper, JEIBrutalPlugin.FRYING_RECIPE_TYPE, BlockRegistry.PAN.get());
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, @NotNull Frying frying, @NotNull IFocusGroup iFocusGroup) {
        if (frying.fluidIngredient().isEmpty()) {
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, this.getCentralX() - 45, this.getCentralY()).addItemStack(ItemRegistry.RAPESEED_OIL_BOTTLE.get().getDefaultInstance());
        } else {
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, this.getCentralX() - 45, this.getCentralY()).addFluidStack(frying.fluidIngredient().getFluid(), frying.fluidIngredient().getAmount());
        }
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, this.getCentralX() - 65, this.getCentralY()).addIngredients(frying.ingredient());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 45, this.getCentralY()).addItemStack(frying.getResultItem(null));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.RENDER_ONLY, this.getCentralX(), this.getCentralY() - 20).addItemStack(frying.container().getItem().getDefaultInstance());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.RENDER_ONLY, this.getCentralX(), this.getCentralY() + 20).addItemStacks(List.of(Items.CAMPFIRE.getDefaultInstance(), Items.SOUL_CAMPFIRE.getDefaultInstance()));
    }
}
