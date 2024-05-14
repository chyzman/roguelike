package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.registry.RoguelikeAttributeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "getEquipmentLevel", at = @At(value = "RETURN"), cancellable = true)
    private static void makeEnchantmentAttributesWork$getEquipmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (!RoguelikeAttributeRegistry.ENCHANTMENT_ATTRIBUTES.containsKey(enchantment)) return;
        cir.setReturnValue((int) (cir.getReturnValue() + entity.getAttributeValue(RoguelikeAttributeRegistry.ENCHANTMENT_ATTRIBUTES.get(enchantment))));
    }

    @Inject(method = "getLevel", at = @At(value = "RETURN"), cancellable = true)
    private static void makeEnchantmentAttributesWork$getLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getHolder() == null || !(stack.getHolder() instanceof LivingEntity living)) return;
        if (!RoguelikeAttributeRegistry.ENCHANTMENT_ATTRIBUTES.containsKey(enchantment)) return;
        cir.setReturnValue((int) (cir.getReturnValue() + living.getAttributeValue(RoguelikeAttributeRegistry.ENCHANTMENT_ATTRIBUTES.get(enchantment))));
    }
}
