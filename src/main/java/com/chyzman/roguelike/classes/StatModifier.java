package com.chyzman.roguelike.classes;

import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.BuiltInEndecs;
import io.wispforest.owo.serialization.endec.StructEndecBuilder;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;

public class StatModifier {
    protected EntityAttribute attribute;
    protected double value;

    //region ENDEC STUFF

    public static final Endec<StatModifier> ENDEC = StructEndecBuilder.of(
            BuiltInEndecs.ofRegistry(Registries.ATTRIBUTE).fieldOf("Attribute", StatModifier::attribute),
            Endec.DOUBLE.fieldOf("Value", StatModifier::value),
            StatModifier::new
    );

    //endregion

    public StatModifier(EntityAttribute attribute, double value) {
        this.attribute = attribute;
        this.value = value;
    }

    //region GETTERS AND SETTERS

    public EntityAttribute attribute() {
        return attribute;
    }

    public StatModifier attribute(EntityAttribute attribute) {
        this.attribute = attribute;
        return this;
    }

    public double value() {
        return value;
    }

    public StatModifier value(double value) {
        this.value = value;
        return this;
    }

    //endregion


    @Override
    public String toString() {
        return "StatModifier{attribute=" + attribute + ", value=" + value + '}';
    }
}