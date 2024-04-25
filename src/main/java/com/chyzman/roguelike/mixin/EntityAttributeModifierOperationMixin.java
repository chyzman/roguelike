package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.registry.RoguelikeAttributeModifierOpperations;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAttributeModifier.Operation.class)
public abstract class EntityAttributeModifierOperationMixin {

    @Mutable
    @Shadow
    @Final
    private static EntityAttributeModifier.Operation[] field_6333;

    @Invoker("<init>")
    public static EntityAttributeModifier.Operation roguelike$invokeNew(String internalName, int ordinal, String name, int id) {
        throw new IllegalStateException("don't call this");
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;field_6333:[Lnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;", shift = At.Shift.AFTER, opcode = Opcodes.PUTSTATIC))
    private static void addNewOperations(CallbackInfo ci) {
        var opperations = new EntityAttributeModifier.Operation[field_6333.length + 2];
        System.arraycopy(field_6333, 0, opperations, 0, field_6333.length);

        opperations[opperations.length - 2] = EntityAttributeModifierOperationMixin.roguelike$invokeNew("ADD_MULTIPLIER", opperations.length - 2, "add_multiplier", opperations.length - 2);
        opperations[opperations.length - 1] = EntityAttributeModifierOperationMixin.roguelike$invokeNew("MULTIPLY_MULTIPLIER", opperations.length - 1, "multiply_multiplier", opperations.length - 1);
        RoguelikeAttributeModifierOpperations.ADD_MULTIPLIER = opperations[opperations.length - 2];
        RoguelikeAttributeModifierOpperations.MULTIPLY_MULTIPLIER = opperations[opperations.length - 1];

        field_6333 = opperations;
    }
}
