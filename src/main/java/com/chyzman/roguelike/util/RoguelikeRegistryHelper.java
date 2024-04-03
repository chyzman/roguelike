package com.chyzman.roguelike.util;

import com.chyzman.roguelike.Roguelike;
import net.minecraft.util.Identifier;

public class RoguelikeRegistryHelper {

    public static Identifier id(String path) {
        return new Identifier(Roguelike.MODID, path);
    }

}