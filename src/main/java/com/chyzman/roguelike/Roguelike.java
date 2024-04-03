package com.chyzman.roguelike;

import com.chyzman.roguelike.classes.RoguelikePassive;
import com.chyzman.roguelike.registry.RogueLikePassiveRegistry;
import com.chyzman.roguelike.registry.RoguelikeRegistry;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import static com.chyzman.roguelike.util.RoguelikeRegistryHelper.id;

public class Roguelike implements ModInitializer {
    public static final String MODID = "roguelike";

    public static final Registry<RoguelikePassive> ROGUELIKE_PASSIVE_REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<RoguelikePassive>ofRegistry( id("passives"))).buildAndRegister();


    @Override
    public void onInitialize() {
        RoguelikeRegistry.init();
        AutoRegistryContainer.register(RogueLikePassiveRegistry.class, MODID, false);
    }
}