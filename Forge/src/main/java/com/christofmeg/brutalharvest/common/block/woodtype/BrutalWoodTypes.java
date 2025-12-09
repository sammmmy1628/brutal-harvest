package com.christofmeg.brutalharvest.common.block.woodtype;

import com.christofmeg.brutalharvest.CommonConstants;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BrutalWoodTypes {

    public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(CommonConstants.MOD_ID + ":rubber"));
    public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(CommonConstants.MOD_ID + ":rubber", RUBBER_SET_TYPE));
}
