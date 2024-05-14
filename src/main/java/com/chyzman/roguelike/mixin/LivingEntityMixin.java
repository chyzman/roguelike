package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.registry.RoguelikeAttributeRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyReturnValue(method = "createLivingAttributes", at = @At("TAIL"))
    private static DefaultAttributeContainer.Builder addDefaultModifiers(
            DefaultAttributeContainer.Builder original
    ) {
        RoguelikeAttributeRegistry.ENCHANTMENT_ATTRIBUTES.forEach((enchantment, attribute) -> original.add(attribute));
        return original;
    }
}
