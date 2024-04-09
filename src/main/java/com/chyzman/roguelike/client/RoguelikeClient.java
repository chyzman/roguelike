package com.chyzman.roguelike.client;

import com.chyzman.roguelike.Roguelike;
import com.chyzman.roguelike.item.RogueAmuletItemRenderer;
import com.chyzman.roguelike.registry.RoguelikeRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.registry.RegistryKeys;

import static com.chyzman.roguelike.util.RoguelikeRegistryHelper.id;

public class RoguelikeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RoguelikeRegistry.clientInit();

        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(id("item/rogue_amulet_base")));
        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(id("item/rogue_amulet_neck")));

        BuiltinItemRendererRegistry.INSTANCE.register(RoguelikeRegistry.ROGUE_AMULET, RogueAmuletItemRenderer.INSTANCE);

        DynamicRegistrySetupCallback.EVENT.register(registryView ->
                registryView.registerEntryAdded(Roguelike.PASSIVE_REGISTRY, (rawId, id, object) ->
                        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(object.getModel(registryView.asDynamicRegistryManager())))));
    }
}