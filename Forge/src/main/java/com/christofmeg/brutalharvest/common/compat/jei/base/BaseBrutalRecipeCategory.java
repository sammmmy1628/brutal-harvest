package com.christofmeg.brutalharvest.common.compat.jei.base;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.util.BrutalRecipeUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseBrutalRecipeCategory<T extends Recipe<Container>> extends AbstractRecipeCategory<T> {

    protected final IDrawable arrowEmptyIcon;
    protected final IDrawable arrowFullIcon;
    protected final IDrawable RMBIcon;

    protected BaseBrutalRecipeCategory(IGuiHelper helper, RecipeType<T> recipeType, Block craftingStation) {
        super(recipeType, Component.translatable("recipe." + CommonConstants.MOD_ID + "." + recipeType.getUid().getPath()), helper.createDrawableItemLike(craftingStation), 216, 81);
        this.arrowEmptyIcon = helper.getRecipeArrow();
        this.arrowFullIcon = helper.getRecipeArrowFilled();
        this.RMBIcon = helper.createDrawable(new ResourceLocation("minecraft", "textures/gui/toasts.png"), 241, 21, 9, 18);
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull T recipe, @NotNull IFocusGroup focuses) {
        IDrawable icon = this.getIcon();
        if (icon != null) {
            builder.addDrawable(icon, this.getCentralX(), this.getCentralY());
        }
        builder.addDrawable(this.arrowEmptyIcon, this.getCentralX() - 25, this.getCentralY());
        builder.addDrawable(this.arrowFullIcon, this.getCentralX() + 20, this.getCentralY());
    }

    protected final int getCentralX() {
        IDrawable icon = this.getIcon();
        if (icon != null) {
            return this.getWidth() / 2 - this.getIcon().getWidth() / 2;
        }
        return 0;
    }

    protected final int getCentralY() {
        IDrawable icon = this.getIcon();
        if (icon != null) {
            return this.getHeight() / 2 - this.getIcon().getHeight() / 2;
        }
        return 0;
    }

    protected final void drawMultipleIngredients(IRecipeLayoutBuilder builder, Ingredient ingredient) {
        List<List<ItemStack>> ingredients = BrutalRecipeUtils.getSortedIngredients(ingredient);
        for (short iter = 0; iter < ingredients.size(); iter++) {
            int x = this.getCentralX() - 45;
            int y = this.getCentralY();
            if (iter % 3 == 0) {
                y += 20 * (iter / 3);
            } else {
                x -= 20 * iter;
            }
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStacks(ingredients.get(iter));
        }
    }
}
