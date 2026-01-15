package com.christofmeg.brutalharvest.common.compat.jei;

import com.christofmeg.brutalharvest.common.compat.jei.base.BaseBrutalRecipeCategory;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import com.christofmeg.brutalharvest.common.init.TagRegistry;
import com.christofmeg.brutalharvest.common.recipe.custom.Cutting;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class CuttingCategory extends BaseBrutalRecipeCategory<Cutting> {

    private static final List<ItemStack> KNIVES;

    protected CuttingCategory(IGuiHelper helper) {
        super(helper, JEIBrutalPlugin.CUTTING_RECIPE_TYPE, BlockRegistry.WOODEN_CUTTING_BOARD.get());
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, @NotNull Cutting cutting, @NotNull IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, this.getCentralX() - 45, this.getCentralY()).addIngredients(cutting.ingredient());
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, this.getCentralX() - 65, this.getCentralY()).addItemStacks(KNIVES);
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, this.getCentralX() + 45, this.getCentralY()).addItemStack(cutting.getResultItem(null));
    }

    static {
        KNIVES = ForgeRegistries.ITEMS.getValues().stream().flatMap(item -> Stream.of(item.getDefaultInstance()))
                .filter(itemstack -> itemstack.is(TagRegistry.Items.KNIVES)).toList();
    }
}
