package com.chyzman.roguelike.client;

import com.chyzman.roguelike.item.RogueAmuletItemRenderer;
import com.chyzman.roguelike.registry.RoguelikeRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static com.chyzman.roguelike.util.RoguelikeRegistryHelper.id;

public class RoguelikeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RoguelikeRegistry.clientInit();
        ClientEventListeners.init();

        BuiltinItemRendererRegistry.INSTANCE.register(RoguelikeRegistry.ROGUE_AMULET, RogueAmuletItemRenderer.INSTANCE);

        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(
                id("item/rogue_amulet_base"),
                id("item/rogue_amulet_neck")
        ));

        PreparableModelLoadingPlugin.register(
                (resourceManager, executor) -> CompletableFuture.supplyAsync(
                        () -> ResourceFinder.json("models/roguelike/passive")
                                .findResources(resourceManager)
                                .keySet()
                                .stream()
                                .map(identifier -> new Identifier(identifier.getNamespace(), identifier.getPath().substring(7, identifier.getPath().length() - 5)))
                                .toList()
                ),
                (data, pluginContext) -> pluginContext.addModels(data)
        );
    }
}