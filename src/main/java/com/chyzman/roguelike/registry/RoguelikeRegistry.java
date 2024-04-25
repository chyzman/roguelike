package com.chyzman.roguelike.registry;

import com.chyzman.roguelike.accessory.RogueAmuletAccessory;
import com.chyzman.roguelike.item.RogueAmuletItem;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

import static com.chyzman.roguelike.util.RoguelikeRegistryHelper.id;

public class RoguelikeRegistry {

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    public static final Item ROGUE_AMULET = registerItem("rogue_amulet", new RogueAmuletItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));

    public static void init() {
        AccessoriesAPI.registerAccessory(ROGUE_AMULET,new RogueAmuletAccessory());
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        AccessoriesRendererRegistry.registerRenderer(ROGUE_AMULET, RogueAmuletAccessory.Renderer::new);
    }
}
