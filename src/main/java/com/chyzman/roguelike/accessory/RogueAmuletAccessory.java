package com.chyzman.roguelike.accessory;

import com.chyzman.roguelike.item.RogueAmuletItem;
import com.google.common.collect.Multimap;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class RogueAmuletAccessory implements Accessory {
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference reference, UUID uuid) {
        var modifiers = Accessory.super.getModifiers(stack, reference, uuid);

        RogueAmuletItem.getPassives(stack).forEach((passive, count) -> modifiers.putAll(passive.attributes()));

        return modifiers;
    }
}