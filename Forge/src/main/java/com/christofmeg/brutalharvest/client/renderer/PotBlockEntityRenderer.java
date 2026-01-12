package com.christofmeg.brutalharvest.client.renderer;

import com.christofmeg.brutalharvest.common.blockentity.PotBlockEntity;
import com.christofmeg.brutalharvest.common.util.BrutalRendererUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class PotBlockEntityRenderer implements BlockEntityRenderer<PotBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public PotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(@NotNull PotBlockEntity potBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        Optional<IItemHandler> optional = potBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        Optional<IFluidHandler> optional1 = potBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        BlockPos pos = potBlockEntity.getBlockPos();
        Level level = potBlockEntity.getLevel();
        if (optional.isPresent() && optional1.isPresent() && level != null) {
            ItemStack stack = optional.get().getStackInSlot(0);
            FluidStack fluid = optional1.get().getFluidInTank(0);
            if (!fluid.isEmpty()) {
                ResourceLocation fluidTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture();
                TextureAtlasSprite atlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTexture);
                int fluidTint = fluid.getFluid().isSame(Fluids.WATER) ? BiomeColors.getAverageWaterColor(level, pos) + 0xC0000000: -1;
                float y = 0.5F + 0.0004375F * fluid.getAmount();
                VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluid.getFluid().defaultFluidState()));
                poseStack.pushPose();
                BrutalRendererUtils.vertex(builder, poseStack, 0.1875F, y, 0.1875F, atlasSprite.getU0(), atlasSprite.getV0(), i, fluidTint);
                BrutalRendererUtils.vertex(builder, poseStack, 0.1875F, y, 0.8125F, atlasSprite.getU0(), atlasSprite.getV1(), i, fluidTint);
                BrutalRendererUtils.vertex(builder, poseStack, 0.8125F, y, 0.8125F, atlasSprite.getU1(), atlasSprite.getV1(), i, fluidTint);
                BrutalRendererUtils.vertex(builder, poseStack, 0.8125F, y, 0.1875F, atlasSprite.getU1(), atlasSprite.getV0(), i, fluidTint);
                poseStack.popPose();
            }
            if (stack != ItemStack.EMPTY) {
                poseStack.pushPose();
                poseStack.translate(0.5F, 0.5625F, 0.5F);
                poseStack.scale(0.65F, 0.65F, 0.65F);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                this.context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED,
                        LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos.above()), level.getBrightness(LightLayer.SKY, pos.above())),
                        i1, poseStack, multiBufferSource, level, (int) potBlockEntity.getBlockState().getSeed(pos));
                poseStack.popPose();
            }
        }
    }
}
