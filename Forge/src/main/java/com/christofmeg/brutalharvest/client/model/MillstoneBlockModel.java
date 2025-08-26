package com.christofmeg.brutalharvest.client.model;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.blockentity.MillstoneBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class MillstoneBlockModel extends GeoModel<MillstoneBlockEntity> {

    @Override
    public ResourceLocation getModelResource(MillstoneBlockEntity millstoneBlockEntity) {
        return new ResourceLocation(CommonConstants.MOD_ID, "geo/block/millstone.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MillstoneBlockEntity millstoneBlockEntity) {
        return new ResourceLocation(CommonConstants.MOD_ID, "textures/block/millstone.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MillstoneBlockEntity millstoneBlockEntity) {
        return new ResourceLocation(CommonConstants.MOD_ID, "animations/millstone.animation.json");
    }
}
