package com.christofmeg.brutalharvest.common.config;

import com.christofmeg.brutalharvest.CommonConstants;
import net.minecraftforge.common.ForgeConfigSpec;

public class BrutalConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue CAN_OVERRIPE;

    static {
        BUILDER.comment(CommonConstants.MOD_NAME + " Config File");
        BUILDER.push("Crop Settings");
        CAN_OVERRIPE = BUILDER.define("allow_crop_overripe", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
