package com.christofmeg.brutalharvest.client.entity.armor;

import com.christofmeg.brutalharvest.common.item.CosmeticItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractRenderer<T extends CosmeticItem> extends GeoArmorRenderer<T> {

    public AbstractRenderer(GeoModel<T> model) {
        super(model);
    }

}
