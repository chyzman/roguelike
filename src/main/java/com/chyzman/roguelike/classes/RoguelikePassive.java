package com.chyzman.roguelike.classes;

import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.BuiltInEndecs;
import io.wispforest.owo.serialization.endec.StructEndecBuilder;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.chyzman.roguelike.Roguelike.PASSIVE_REGISTRY;

public class RoguelikePassive {
    protected String name;
    protected String description;

    protected Identifier model;

    protected List<StatModifier> stats;
    protected List<StatModifier> modifiers;
    protected List<StatModifier> multipliers;

    //region ENDEC STUFF

    public static final Endec<RoguelikePassive> ENDEC = StructEndecBuilder.of(
            Endec.STRING.optionalFieldOf("Name", RoguelikePassive::name, (String) null),
            Endec.STRING.optionalFieldOf("Description", RoguelikePassive::description, (String) null),
            BuiltInEndecs.IDENTIFIER.optionalFieldOf("Model", RoguelikePassive::model, (Identifier) null),
            StatModifier.ENDEC.listOf().optionalFieldOf("Stats", RoguelikePassive::stats, new ArrayList<>()),
            StatModifier.ENDEC.listOf().optionalFieldOf("Modifiers", RoguelikePassive::modifiers, new ArrayList<>()),
            StatModifier.ENDEC.listOf().optionalFieldOf("Multipliers", RoguelikePassive::multipliers, new ArrayList<>()),
            RoguelikePassive::new
    );

    private RoguelikePassive(
            String name,
            String description,
            Identifier model,
            List<StatModifier> stats,
            List<StatModifier> modifiers,
            List<StatModifier> multipliers
    ) {
        this.name = name;
        this.description = description;
        this.model = model;
        this.stats = stats;
        this.modifiers = modifiers;
        this.multipliers = multipliers;
    }

    //endregion

    public RoguelikePassive() {
    }

    public MutableText getName(DynamicRegistryManager drm) {
        if (name != null) return Text.translatable(name);
        return Text.translatable(getKey(drm).getValue().toTranslationKey("roguelike.passive", "name"));
    }

    public MutableText getDescription(DynamicRegistryManager drm) {
        if (description != null) return Text.translatable(description);
        return Text.translatable(getKey(drm).getValue().toTranslationKey("roguelike.passive", "description"));
    }

    public Identifier getModel(DynamicRegistryManager drm) {
        if (model != null) return model;
        return getKey(drm).getValue().withPrefixedPath("roguelike/passive/");
    }

    public RegistryKey<RoguelikePassive> getKey(DynamicRegistryManager drm) {
        return drm.get(PASSIVE_REGISTRY).getKey(this).orElse(null);
    }

    //region GETTERS AND SETTERS

    @Nullable
    private String name() {
        return name;
    }

    @Nullable
    private String description() {
        return description;
    }

    @Nullable
    private Identifier model() {
        return model;
    }

    public List<StatModifier> stats() {
        return stats;
    }

    public List<StatModifier> modifiers() {
        return modifiers;
    }

    public List<StatModifier> multipliers() {
        return multipliers;
    }

    //endregion
}