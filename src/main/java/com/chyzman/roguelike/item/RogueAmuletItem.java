package com.chyzman.roguelike.item;

import com.chyzman.roguelike.Roguelike;
import com.chyzman.roguelike.classes.RoguelikePassive;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.BuiltInEndecs;
import io.wispforest.owo.serialization.endec.KeyedEndec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.chyzman.roguelike.Roguelike.ROGUELIKE_PASSIVE_REGISTRY;

public class RogueAmuletItem extends Item {
    public static final KeyedEndec<Map<RoguelikePassive, Integer>> PASSIVES =
            Endec.map(BuiltInEndecs.ofRegistry(ROGUELIKE_PASSIVE_REGISTRY), Endec.INT).keyed("Passives",HashMap::new);

    public RogueAmuletItem(Settings settings) {
        super(settings);
    }

    public static Map<RoguelikePassive, Integer> getPassives(ItemStack stack) {
        return stack.get(PASSIVES);
    }
}