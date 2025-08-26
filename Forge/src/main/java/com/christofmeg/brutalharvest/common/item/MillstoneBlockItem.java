package com.christofmeg.brutalharvest.common.item;

import com.christofmeg.brutalharvest.client.model.MillstoneBlockItemModel;
import com.christofmeg.brutalharvest.common.init.BlockRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class MillstoneBlockItem extends BlockItem implements GeoItem, IClientItemExtensions {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MillstoneBlockItem(Properties pProperties) {
        super(BlockRegistry.MILLSTONE.get(), pProperties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoItemRenderer<MillstoneBlockItem> renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new GeoItemRenderer<>(new MillstoneBlockItemModel());
                }
                return this.renderer;
            }
        });
    }
}
