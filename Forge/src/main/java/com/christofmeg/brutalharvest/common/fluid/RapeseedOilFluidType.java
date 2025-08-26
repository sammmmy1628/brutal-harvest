package com.christofmeg.brutalharvest.common.fluid;

import com.christofmeg.brutalharvest.CommonConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class RapeseedOilFluidType extends FluidType {

    public RapeseedOilFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return new ResourceLocation(CommonConstants.MOD_ID,  "block/rapeseed_oil_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return new ResourceLocation(CommonConstants.MOD_ID, "block/rapeseed_oil_flowing");
            }
        });
    }
}
