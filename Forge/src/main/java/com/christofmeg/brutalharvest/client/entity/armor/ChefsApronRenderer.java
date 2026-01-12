package com.christofmeg.brutalharvest.client.entity.armor;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.item.ChefsApronItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@OnlyIn(Dist.CLIENT)
public class ChefsApronRenderer extends GeoArmorRenderer<ChefsApronItem> {

    public ChefsApronRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(CommonConstants.MOD_ID, "textures/models/armor/chefs_apron_layer_1.png")) {
            @Override
            public ResourceLocation getModelResource(ChefsApronItem object) {
                return new ResourceLocation(CommonConstants.MOD_ID, "geo/chefs_apron.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ChefsApronItem object) {
                return new ResourceLocation(CommonConstants.MOD_ID, "textures/models/armor/chefs_apron_layer_1.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ChefsApronItem animatable) {
                return new ResourceLocation(CommonConstants.MOD_ID, "animations/armor.animation.json");
            }
        });
    }

}