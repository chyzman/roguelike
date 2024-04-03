package com.chyzman.roguelike.classes;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class RoguelikePassive {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public RoguelikePassive(Settings settings) {
        this.attributes = settings.attributes;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> attributes() {
        return attributes;
    }

    public static class Settings {
        Multimap<EntityAttribute, EntityAttributeModifier> attributes = ArrayListMultimap.create();

        public RoguelikePassive.Settings attribute(EntityAttribute attribute, EntityAttributeModifier modifier) {
            this.attributes.put(attribute, modifier);
            return this;
        }
    }
}