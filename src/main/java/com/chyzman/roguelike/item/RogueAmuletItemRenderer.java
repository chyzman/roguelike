package com.chyzman.roguelike.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class RogueAmuletItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer{
    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var client = MinecraftClient.getInstance();
        var world = client.world;
        var random = world.random;
        var time = world.getTime();

        var item = Registries.ITEM.get(random.nextInt((int) (time/100)));

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(time / 50f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time / 50f + 100));


    }
}