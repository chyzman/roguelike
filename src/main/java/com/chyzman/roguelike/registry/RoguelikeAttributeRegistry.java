package com.chyzman.roguelike.registry;

import com.chyzman.roguelike.mixin.DefaultAttributeContainerAccessor;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chyzman.roguelike.util.RoguelikeRegistryHelper.id;

public class RoguelikeAttributeRegistry {
    public static List<DefaultAttributeContainer> DEFAULT_ATTRIBUTE_CONTAINERS = new ArrayList<>();

    public static Map<RegistryEntry<Enchantment>, RegistryEntry<EntityAttribute>> ENCHANTMENT_ATTRIBUTES = new HashMap<>();


    public static void init() {
        for (Enchantment enchantment : Registries.ENCHANTMENT) {
            registerEnchantmentAttribute(enchantment);
        }

        DEFAULT_ATTRIBUTE_CONTAINERS.forEach(defaultAttributeContainer ->
                ENCHANTMENT_ATTRIBUTES.forEach((enchantment, clampedEntityAttribute) ->
                        addEnchantmentAttribute(defaultAttributeContainer, enchantment, clampedEntityAttribute)
                )
        );

        RegistryEntryAddedCallback.event(Registries.ENCHANTMENT).register(((rawId, id, enchantment) -> {
            registerEnchantmentAttribute(enchantment);
            DEFAULT_ATTRIBUTE_CONTAINERS.forEach(defaultAttributeContainer ->
                    addEnchantmentAttribute(defaultAttributeContainer, enchantment, ENCHANTMENT_ATTRIBUTES.get(enchantment))
            );
        }));
    }

    @Nullable
    private static EntityAttributeInstance addEnchantmentAttribute(DefaultAttributeContainer defaultAttributeContainer, Enchantment enchantment, ClampedEntityAttribute clampedEntityAttribute) {
        return ((DefaultAttributeContainerAccessor) defaultAttributeContainer).roguelike$getInstances().put(
                Registries.ATTRIBUTE.get(getAttributeId(enchantment)),
                new EntityAttributeInstance(clampedEntityAttribute, attributeInstance -> {})
        );
    }

    public static void registerEnchantmentAttribute(Enchantment enchantment) {
        var attribute = new ClampedEntityAttribute(enchantment.getTranslationKey(), 0, 0, 127);
        var id = getAttributeId(enchantment);
        if (id == null) return;
        Registry.register(
                Registries.ATTRIBUTE,
                id,
                attribute
        );
        ENCHANTMENT_ATTRIBUTES.put(enchantment, attribute);
    }

    @Nullable
    private static Identifier getAttributeId(Enchantment enchantment) {
        var id = Registries.ENCHANTMENT.getId(enchantment);
        if (id == null) return null;
        return id("enchantment." + id.toTranslationKey());
    }
}
