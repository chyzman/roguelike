package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.registry.RoguelikeAttributeModifierOpperations;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.wispforest.accessories.impl.AccessoriesEventHandler;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AccessoriesEventHandler.class)
public abstract class AccessoriesEventHandlerMixin {
    @ModifyExpressionValue(method = "addAttributeTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/attribute/EntityAttributeModifier;getOperation()Lnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;",
                    ordinal = 0
            )
    )
    private static EntityAttributeModifier.Operation makeNewModifiersDisplayCorrectlyinAccessories(
            EntityAttributeModifier.Operation original, @Local EntityAttributeModifier entityAttributeModifier
    ) {
        if (entityAttributeModifier.getOperation().equals(RoguelikeAttributeModifierOpperations.ADD_MULTIPLIER) ||
                entityAttributeModifier.getOperation().equals(RoguelikeAttributeModifierOpperations.MULTIPLY_MULTIPLIER)) {
            return EntityAttributeModifier.Operation.MULTIPLY_BASE;
        }
        return original;
    }
}
