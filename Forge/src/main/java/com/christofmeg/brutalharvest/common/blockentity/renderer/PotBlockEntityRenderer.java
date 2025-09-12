package com.christofmeg.brutalharvest.common.blockentity.renderer;

import com.christofmeg.brutalharvest.common.blockentity.PanBlockEntity;
import com.christofmeg.brutalharvest.common.blockentity.PotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PotBlockEntityRenderer implements BlockEntityRenderer<PotBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public PotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(@NotNull PotBlockEntity potBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        Optional<IItemHandler> optional = potBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        Level level = potBlockEntity.getLevel();
        if (optional.isPresent() && level != null) {
            ItemStack stack = optional.get().getStackInSlot(0);
            if (stack == ItemStack.EMPTY) {
                stack = optional.get().getStackInSlot(1);
            }
            if (stack != ItemStack.EMPTY) {
                BlockPos pos = potBlockEntity.getBlockPos();
                poseStack.pushPose();
                poseStack.translate(0.5F, 0.5625F, 0.5F);
                poseStack.scale(0.75F, 0.75F, 0.75F);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                this.context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED,
                        LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos.above()), level.getBrightness(LightLayer.SKY, pos.above())),
                        i1, poseStack, multiBufferSource, level, (int) potBlockEntity.getBlockState().getSeed(pos));
                poseStack.popPose();
            }
        }
    }
}
