package com.christofmeg.brutalharvest.client.entity.armor;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.item.GardenersHatItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

@OnlyIn(Dist.CLIENT)
public class GardenersHatRenderer extends AbstractRenderer<GardenersHatItem> {

    public GardenersHatRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(CommonConstants.MOD_ID, "textures/models/armor/gardeners_hat_layer_1.png")) {
            @Override
            public ResourceLocation getModelResource(GardenersHatItem object) {
                return new ResourceLocation(CommonConstants.MOD_ID, "geo/gardeners_hat.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(GardenersHatItem object) {
                return new ResourceLocation(CommonConstants.MOD_ID, "textures/models/armor/gardeners_hat_layer_1.png");
            }

            @Override
            public ResourceLocation getAnimationResource(GardenersHatItem animatable) {
                return new ResourceLocation(CommonConstants.MOD_ID, "animations/armor.animation.json");
            }
        });
    }

}