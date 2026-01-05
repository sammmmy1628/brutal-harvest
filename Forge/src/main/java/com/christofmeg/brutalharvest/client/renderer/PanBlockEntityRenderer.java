package com.christofmeg.brutalharvest.client.renderer;

import com.christofmeg.brutalharvest.common.block.base.BaseCookingBlock;
import com.christofmeg.brutalharvest.common.blockentity.PanBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PanBlockEntityRenderer implements BlockEntityRenderer<PanBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public PanBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(@NotNull PanBlockEntity panBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        Optional<IItemHandler> optional = panBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        Optional<IFluidHandler> optional1 = panBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        BlockPos pos = panBlockEntity.getBlockPos();
        Level level = panBlockEntity.getLevel();
        if (optional.isPresent() && optional1.isPresent() && level != null) {
            ItemStack stack = optional.get().getStackInSlot(0);
            FluidStack fluid = optional1.get().getFluidInTank(0);
            float y = 0.625F;
            if (!fluid.isEmpty()) {
                ResourceLocation fluidTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture();
                TextureAtlasSprite atlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTexture);
                VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluid.getFluid().defaultFluidState()));
                poseStack.pushPose();
                vertex(builder, poseStack, 0.1875F, 0.5625F, 0.1875F, atlasSprite.getU0(), atlasSprite.getV0(), i, -1);
                vertex(builder, poseStack, 0.1875F, 0.5625F, 0.8125F, atlasSprite.getU0(), atlasSprite.getV1(), i, -1);
                vertex(builder, poseStack, 0.8125F, 0.5625F, 0.8125F, atlasSprite.getU1(), atlasSprite.getV1(), i, -1);
                vertex(builder, poseStack, 0.8125F, 0.5625F, 0.1875F, atlasSprite.getU1(), atlasSprite.getV0(), i, -1);
                poseStack.popPose();
            }
            if (stack == ItemStack.EMPTY) {
                stack = optional.get().getStackInSlot(1);
                y = 0.5625F;
            }
            if (stack != ItemStack.EMPTY) {
                int rot = (int) panBlockEntity.getBlockState().getValue(BaseCookingBlock.FACING).toYRot();
                poseStack.pushPose();
                poseStack.translate(0.5F, y, 0.5F);
                poseStack.scale(0.65F, 0.65F, 0.65F);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F).mul(Axis.ZP.rotationDegrees(rot)));
                this.context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED,
                        LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos.above()), level.getBrightness(LightLayer.SKY, pos.above())),
                        i1, poseStack, multiBufferSource, level, (int) panBlockEntity.getBlockState().getSeed(pos));
                poseStack.popPose();
            }
        }
    }

    private static void vertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int light, int tint) {
        builder.vertex(poseStack.last().pose(), x, y, z)
                .color(tint)
                .uv(u, v)
                .uv2(light)
                .normal(1.0F, 0.0F, 0.0F)
                .endVertex();
    }
}
