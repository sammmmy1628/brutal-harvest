package com.christofmeg.brutalharvest.client.renderer;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.client.model.ThrownScytheModel;
import com.christofmeg.brutalharvest.common.entity.ThrownScytheEntity;
import com.christofmeg.brutalharvest.common.init.ItemRegistry;
import com.christofmeg.brutalharvest.common.item.ScytheItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class ThrownScytheRenderer extends EntityRenderer<ThrownScytheEntity> {
    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(CommonConstants.MOD_ID, "textures/entity/stone_scythe.png");

    public static final Map<Item, ResourceLocation> TEXTURES = new ConcurrentHashMap<>();

    static {
        ItemRegistry.ITEMS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(item -> item instanceof ScytheItem)
                .forEach(item -> TEXTURES.put(item,
                        new ResourceLocation(CommonConstants.MOD_ID,
                                "textures/entity/" + item.getDescriptionId().replace("item." + CommonConstants.MOD_ID + ".", "") + ".png")));
    }

    private final ThrownScytheModel model;

    public ThrownScytheRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ThrownScytheModel(pContext.bakeLayer(RenderLayers.register("scythe")));
    }

    @Override
    public void render(ThrownScytheEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, false);
        this.model.renderToBuffer(pPoseStack, $$6, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(ThrownScytheEntity entity) {
        return TEXTURES.getOrDefault(entity.getItem().getItem(), MODEL_LOCATION);
    }
}
