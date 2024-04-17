package com.chyzman.roguelike.accessory;

import com.chyzman.roguelike.Roguelike;
import com.chyzman.roguelike.item.RogueAmuletItem;
import com.chyzman.roguelike.item.RogueAmuletItemRenderer;
import com.google.common.collect.Multimap;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.*;

import static com.chyzman.roguelike.Roguelike.PASSIVE_REGISTRY;

public class RogueAmuletAccessory implements Accessory {
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference reference, UUID uuid) {
        var modifiers = Accessory.super.getModifiers(stack, reference, uuid);
        var world = MinecraftClient.getInstance().world;
        if (world == null) return modifiers;

        Map<EntityAttribute, Double> tempStats = new HashMap<>();
        Map<EntityAttribute, Double> tempMultipliers = new HashMap<>();

        RogueAmuletItem.getPassives(stack, world.getRegistryManager()).forEach(passive -> {
            if (passive != null) {
                passive.stats().forEach(stat -> tempStats.merge(stat.attribute(), stat.value(), Double::sum));
                passive.modifiers().forEach(modifier -> tempMultipliers.merge(modifier.attribute(), modifier.value(), Double::sum));
                passive.multipliers().forEach(multiplier -> tempMultipliers.merge(multiplier.attribute(), multiplier.value(), (a, b) -> a * b));
            }
        });

        tempStats.forEach((attribute, value) -> modifiers.put(attribute, new EntityAttributeModifier(Roguelike.getAttributeUUID(attribute, EntityAttributeModifier.Operation.ADDITION),"Rogue Amulet Stat", value, EntityAttributeModifier.Operation.ADDITION)));
        tempMultipliers.forEach((attribute, value) -> modifiers.put(attribute, new EntityAttributeModifier(Roguelike.getAttributeUUID(attribute, EntityAttributeModifier.Operation.MULTIPLY_TOTAL),"Rogue Amulet Multiplier", value, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)));

        return modifiers;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void getExtraTooltip(ItemStack stack, List<Text> tooltips) {
        Accessory.super.getExtraTooltip(stack, tooltips);
//        var world = MinecraftClient.getInstance().world;
//        if (world == null) return;
//        var passives = RogueAmuletItem.getPassives(stack, world.getRegistryManager());
//
//        var registry = world.getRegistryManager().get(PASSIVE_REGISTRY);
//        if (passives.isEmpty()) return;
//        tooltips.add(Text.literal("Passives:"));
//        passives.forEach(passive -> {
//            var id = registry.getId(passive);
//            if (id == null) return;
//            tooltips.add(Text.literal("  ").append(passive.getName(world.getRegistryManager())));
//            tooltips.add(Text.literal("    ").append(passive.getDescription(world.getRegistryManager())));
//        });
    }

    @Environment(EnvType.CLIENT)
    public static class Renderer implements AccessoryRenderer {

        @Override
        public <M extends LivingEntity> void render(
                ItemStack stack,
                SlotReference reference,
                MatrixStack matrices,
                EntityModel<M> model,
                VertexConsumerProvider multiBufferSource,
                int light,
                float limbSwing,
                float limbSwingAmount,
                float partialTicks,
                float ageInTicks,
                float netHeadYaw,
                float headPitch
        ) {
            if (!(model instanceof BipedEntityModel<? extends LivingEntity> humanoidModel)) return;
            AccessoryRenderer.transformToModelPart(matrices,humanoidModel.body, null, 1, 1);

            long seed = reference.slotName().hashCode()
                    + reference.slot()
                    + reference.entity().getUuid().getMostSignificantBits()
                    + reference.entity().getUuid().getLeastSignificantBits();

            RogueAmuletItemRenderer.INSTANCE.render(
                    stack,
                    seed,
                    true,
                    ModelTransformationMode.FIXED,
                    matrices,
                    multiBufferSource,
                    light,
                    OverlayTexture.DEFAULT_UV
            );
        }
    }
}