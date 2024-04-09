package com.chyzman.roguelike;

import com.chyzman.roguelike.classes.RoguelikePassive;
import com.chyzman.roguelike.command.RogueItemCommand;
import com.chyzman.roguelike.registry.RoguelikeRegistry;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.wispforest.owo.serialization.SerializationAttribute;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.chyzman.roguelike.util.RoguelikeRegistryHelper.id;

public class Roguelike implements ModInitializer {
    public static final String MODID = "roguelike";

    private static final Table<EntityAttribute, EntityAttributeModifier.Operation, UUID> CACHTRIBUTE = HashBasedTable.create();

    public static final RegistryKey<Registry<RoguelikePassive>> PASSIVE_REGISTRY = RegistryKey.ofRegistry(id("passive"));

    @Override
    public void onInitialize() {
        DynamicRegistries.registerSynced(
                PASSIVE_REGISTRY,
                RoguelikePassive.ENDEC.codec(SerializationAttribute.HUMAN_READABLE),
                DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
        RoguelikeRegistry.init();
        RogueItemCommand.register();
    }

    public static UUID getAttributeUUID(EntityAttribute attribute, EntityAttributeModifier.Operation operation) {
        UUID uuid = CACHTRIBUTE.get(attribute, operation);
        if (uuid == null) {
            uuid = UUID.randomUUID();
            CACHTRIBUTE.put(attribute, operation, uuid);
        }
        return uuid;
    }
}