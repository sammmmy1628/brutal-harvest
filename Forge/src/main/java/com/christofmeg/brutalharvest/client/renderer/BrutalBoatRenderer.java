package com.christofmeg.brutalharvest.client.renderer;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.entity.BrutalBoatEntity;
import com.christofmeg.brutalharvest.common.entity.BrutalChestBoatEntity;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class BrutalBoatRenderer extends BoatRenderer {

    private final Map<BrutalBoatEntity.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public BrutalBoatRenderer(EntityRendererProvider.Context pContext, boolean pChestBoat) {
        super(pContext, pChestBoat);
        this.boatResources = Stream.of(BrutalBoatEntity.Type.values()).collect(ImmutableMap.toImmutableMap(type -> type, type -> Pair.of(new ResourceLocation(CommonConstants.MOD_ID, getTextureLocation(type, pChestBoat)), this.createBrutalBoatModel(pContext, type, pChestBoat))));
    }

    private ListModel<Boat> createBrutalBoatModel(EntityRendererProvider.Context pContext, BrutalBoatEntity.Type pType, boolean pChestBoat) {
        ModelLayerLocation modellayerlocation = pChestBoat ? createChestBoatModelName(pType) : createBoatModelName(pType);
        ModelPart modelpart = pContext.bakeLayer(modellayerlocation);
        return pChestBoat ? new ChestBoatModel(modelpart) : new BoatModel(modelpart);
    }

    private static String getTextureLocation(BrutalBoatEntity.Type pType, boolean pChestBoat) {
        return pChestBoat ? "textures/entity/chest_boat/" + pType.getName() + ".png" : "textures/entity/boat/" + pType.getName() + ".png";
    }

    public static ModelLayerLocation createBoatModelName(BrutalBoatEntity.Type pType) {
        return createLocation("boat/" + pType.getName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(BrutalBoatEntity.Type pType) {
        return createLocation("chest_boat/" + pType.getName(), "main");
    }

    private static ModelLayerLocation createLocation(String pPath, String pModel) {
        return new ModelLayerLocation(new ResourceLocation(CommonConstants.MOD_ID, pPath), pModel);
    }

    @Override
    public @NotNull Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(@NotNull Boat boat) {
        if (boat instanceof BrutalChestBoatEntity) {
            return this.boatResources.get(((BrutalChestBoatEntity) boat).getBrutalVariant());
        }
        return this.boatResources.get(((BrutalBoatEntity) boat).getBrutalVariant());
    }
}
