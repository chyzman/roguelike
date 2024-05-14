package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.registry.RoguelikeAttributeModifierOpperations;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @ModifyExpressionValue(method = "getTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/attribute/EntityAttributeModifier;getOperation()Lnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;",
                    ordinal = 0
            )
    )
    private EntityAttributeModifier.Operation makeNewModifiersDisplayCorrectly(
            EntityAttributeModifier.Operation original, @Local EntityAttributeModifier entityAttributeModifier
    ) {
        if (entityAttributeModifier.getOperation().equals(RoguelikeAttributeModifierOpperations.ADD_MULTIPLIER) ||
                entityAttributeModifier.getOperation().equals(RoguelikeAttributeModifierOpperations.MULTIPLY_MULTIPLIER)) {
            return EntityAttributeModifier.Operation.MULTIPLY_TOTAL;
        }
        return original;
    }
}
