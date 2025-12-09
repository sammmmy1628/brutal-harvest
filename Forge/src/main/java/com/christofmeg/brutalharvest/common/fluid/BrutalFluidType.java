package com.christofmeg.brutalharvest.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class BrutalFluidType extends FluidType {

    private final ResourceLocation still;
    private final ResourceLocation flowing;

    public BrutalFluidType(ResourceLocation still, ResourceLocation flowing) {
        super(FluidType.Properties.create());
        this.still = still;
        this.flowing = flowing;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return BrutalFluidType.this.still;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return BrutalFluidType.this.flowing;
            }
        });
    }
}
