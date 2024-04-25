package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.pond.EntityAttributeModifierDuck;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityAttributeModifier.class)
public abstract class EntityAttributeModifierMixin implements EntityAttributeModifierDuck {
    @Unique
    private long priority = 0;

    @Override
    public EntityAttributeModifier roguelike$priority(long priority) {
        this.priority = priority;
        return (EntityAttributeModifier) (Object) this;
    }

    @Override
    public long roguelike$priority() {
        return priority;
    }
}
