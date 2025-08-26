package com.christofmeg.brutalharvest.common.init;

import com.christofmeg.brutalharvest.CommonConstants;
import com.christofmeg.brutalharvest.common.menu.SeedSatchelMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, CommonConstants.MOD_ID);

    public static final RegistryObject<MenuType<SeedSatchelMenu>> SEED_SATCHEL_MENU_TYPE;

    public static void init(@Nonnull IEventBus modEventBus) {
        MENUS.register(modEventBus);
    }

    static {
        SEED_SATCHEL_MENU_TYPE = MENUS.register("seed_satchel", () -> new MenuType<>(SeedSatchelMenu::new, FeatureFlags.DEFAULT_FLAGS));
    }
}
