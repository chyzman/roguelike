package com.chyzman.roguelike.registry;

import com.chyzman.roguelike.classes.RoguelikePassive;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registry;

import static com.chyzman.roguelike.Roguelike.ROGUELIKE_PASSIVE_REGISTRY;

public class RogueLikePassiveRegistry implements AutoRegistryContainer<RoguelikePassive> {
    public static final RoguelikePassive TEST = new RoguelikePassive(
            new RoguelikePassive.Settings()
                    .attribute(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE,
                            new EntityAttributeModifier("test", 1.8, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                    )
    );

    @Override
    public Registry<RoguelikePassive> getRegistry() {
        return ROGUELIKE_PASSIVE_REGISTRY;
    }

    @Override
    public Class<RoguelikePassive> getTargetFieldType() {
        return RoguelikePassive.class;
    }
}