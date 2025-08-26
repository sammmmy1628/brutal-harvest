package com.christofmeg.brutalharvest.client.model;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.item.MillstoneBlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class MillstoneBlockItemModel extends GeoModel<MillstoneBlockItem> {

    @Override
    public ResourceLocation getModelResource(MillstoneBlockItem millstoneBlockItem) {
        return new ResourceLocation(CommonConstants.MOD_ID, "geo/item/millstone.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MillstoneBlockItem millstoneBlockItem) {
        return new ResourceLocation(CommonConstants.MOD_ID, "textures/block/millstone.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MillstoneBlockItem millstoneBlockItem) {
        return null;
    }
}
