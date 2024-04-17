package com.chyzman.roguelike.client;

import com.chyzman.roguelike.item.RogueAmuletItem;
import com.chyzman.roguelike.item.RogueAmuletTooltipComponent;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.MinecraftClient;

public class ClientEventListeners {
    public static void init() {
        TooltipComponentCallback.EVENT.register(
                data -> (data instanceof RogueAmuletItem.TooltipData tooltipData && MinecraftClient.getInstance().world != null)
                        ? new RogueAmuletTooltipComponent(tooltipData.stack(), MinecraftClient.getInstance().world.getRegistryManager())
                        : null
        );
    }
}