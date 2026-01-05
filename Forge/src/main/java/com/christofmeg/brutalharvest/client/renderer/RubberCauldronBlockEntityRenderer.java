package com.christofmeg.brutalharvest.client.renderer;

import com.christofmeg.brutalharvest.common.blockentity.RubberCauldronBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RubberCauldronBlockEntityRenderer implements BlockEntityRenderer<RubberCauldronBlockEntity> {

    public RubberCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull RubberCauldronBlockEntity rubberCauldronBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        Optional<IFluidHandler> optional = rubberCauldronBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        if (optional.isPresent()) {
            FluidStack fluidStack = ((FluidTank) optional.get()).getFluid();
            if (!fluidStack.isEmpty()) {
                ResourceLocation fluidTexture = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getStillTexture();
                TextureAtlasSprite atlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTexture);
                float y = 0.25F + 0.00075F * fluidStack.getAmount();
                VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidStack.getFluid().defaultFluidState()));
                poseStack.pushPose();
                PotBlockEntityRenderer.vertex(builder, poseStack, 0.125F, y, 0.125F, atlasSprite.getU0(), atlasSprite.getV0(), i, -1);
                PotBlockEntityRenderer.vertex(builder, poseStack, 0.125F, y, 0.875F, atlasSprite.getU0(), atlasSprite.getV1(), i, -1);
                PotBlockEntityRenderer.vertex(builder, poseStack, 0.875F, y, 0.875F, atlasSprite.getU1(), atlasSprite.getV1(), i, -1);
                PotBlockEntityRenderer.vertex(builder, poseStack, 0.875F, y, 0.125F, atlasSprite.getU1(), atlasSprite.getV0(), i, -1);
                poseStack.popPose();
            }
        }
    }
}
