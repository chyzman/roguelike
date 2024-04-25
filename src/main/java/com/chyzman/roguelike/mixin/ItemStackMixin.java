package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.registry.RoguelikeAttributeModifierOpperations;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @ModifyArg(method = "getTooltip",
            at = @At(value = "INVOKE",
                    target = "Ljava/text/DecimalFormat;format(D)Ljava/lang/String;"
            )
    )
    private double makeNewModifiersDisplayCorrectly(
            double original,
            @Local EntityAttributeModifier entityAttributeModifier
    ) {
        if (entityAttributeModifier.getOperation().equals(RoguelikeAttributeModifierOpperations.ADD_MULTIPLIER) ||
                entityAttributeModifier.getOperation().equals(RoguelikeAttributeModifierOpperations.MULTIPLY_MULTIPLIER)) {
            return original * 100d;
        }
        return original;
    }
}
