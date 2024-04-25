package com.chyzman.roguelike.pond;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface EntityAttributeModifierDuck {
    default EntityAttributeModifier roguelike$priority(long priority) {
        throw new UnsupportedOperationException();
    }

    default long roguelike$priority() {
        throw new UnsupportedOperationException();
    }
}
