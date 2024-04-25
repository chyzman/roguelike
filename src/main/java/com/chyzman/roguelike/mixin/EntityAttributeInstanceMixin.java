package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.pond.EntityAttributeModifierDuck;
import com.chyzman.roguelike.registry.RoguelikeAttributeModifierOpperations;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Comparator;

@Mixin(EntityAttributeInstance.class)
public abstract class EntityAttributeInstanceMixin {

    @Shadow
    protected abstract Collection<EntityAttributeModifier> getModifiersByOperation(EntityAttributeModifier.Operation operation);

    @Inject(method = "computeValue", at = @At("RETURN"), cancellable = true)
    private void applyRoguelikeOpperations(CallbackInfoReturnable<Double> cir) {
        double rogueMul = 1;

        for (EntityAttributeModifier addMultiplierModifier : getModifiersByOperation(RoguelikeAttributeModifierOpperations.ADD_MULTIPLIER)) {
            rogueMul += addMultiplierModifier.getValue();
        }

        for (EntityAttributeModifier multiplyMultiplierModifier : getModifiersByOperation(RoguelikeAttributeModifierOpperations.MULTIPLY_MULTIPLIER)) {
            rogueMul *= multiplyMultiplierModifier.getValue();
        }

        cir.setReturnValue(cir.getReturnValue() * rogueMul);
    }

    @Inject(method = "getModifiersByOperation", at = @At("RETURN"), cancellable = true)
    private void sortModifiersByPriority(EntityAttributeModifier.Operation operation, CallbackInfoReturnable<Collection<EntityAttributeModifier>> cir) {
        cir.setReturnValue(cir.getReturnValue().stream().sorted(Comparator.comparingLong(EntityAttributeModifierDuck::roguelike$priority)).toList());
    }
}
