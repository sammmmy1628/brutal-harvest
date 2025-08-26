package com.christofmeg.brutalharvest.client.renderer;

import com.christofmeg.brutalharvest.client.model.MillstoneBlockModel;
import com.christofmeg.brutalharvest.common.blockentity.MillstoneBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@OnlyIn(Dist.CLIENT)
public class MillstoneBlockEntityRenderer extends GeoBlockRenderer<MillstoneBlockEntity> {

    public MillstoneBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new MillstoneBlockModel());
    }
}
