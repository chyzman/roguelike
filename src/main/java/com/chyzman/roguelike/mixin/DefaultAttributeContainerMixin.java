package com.chyzman.roguelike.mixin;

import com.chyzman.roguelike.registry.RoguelikeAttributeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(DefaultAttributeContainer.class)
public abstract class DefaultAttributeContainerMixin {
    @Mutable
    @Shadow @Final private Map<EntityAttribute, EntityAttributeInstance> instances;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void saveDefaultAttributeContainerForLater(Map instances, CallbackInfo ci) {
        this.instances = new HashMap<>(instances);
        RoguelikeAttributeRegistry.DEFAULT_ATTRIBUTE_CONTAINERS.add((DefaultAttributeContainer) (Object) this);
    }
}
