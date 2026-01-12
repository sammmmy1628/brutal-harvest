package com.christofmeg.brutalharvest.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class BrutalRendererUtils {

    public static void vertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int light, int tint) {
        builder.vertex(poseStack.last().pose(), x, y, z)
                .color(tint)
                .uv(u, v)
                .uv2(light)
                .normal(1.0F, 0.0F, 0.0F)
                .endVertex();
    }
}
