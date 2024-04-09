package com.chyzman.roguelike.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.RotationAxis;
import org.joml.Random;

import static com.chyzman.roguelike.util.RoguelikeRegistryHelper.id;

@Environment(EnvType.CLIENT)
public class RogueAmuletItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static final RogueAmuletItemRenderer INSTANCE = new RogueAmuletItemRenderer();

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        render(stack, stack.hashCode(), false, mode, matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

    public void render(ItemStack stack, long seed, boolean neck, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var client = MinecraftClient.getInstance();
        var world = client.world;
        if (world == null) return;
        var time = System.nanoTime() / 1000000L;
        var trueSeed = (time / (1000 + seed % 1000)) + seed % 100000;
        var random = new Random(seed);

        matrices.push();
        if (neck) {
            matrices.scale(0.25f, 0.25f, 0.25f);
            matrices.translate(0, -1, 0.25);
        } else {
            matrices.scale(0.4f, 0.4f, 0.4f);
        }

        matrices.push();
        matrices.translate(-0.5f, -0.5f, -0.5f);

        client.getBlockRenderManager().renderBlockAsEntity(
                RogueAmuletItem.getDisplayState(stack),
                matrices,
                vertexConsumers,
                light,
                overlay
        );

        matrices.pop();

        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(time / 10f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time / 15f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(time / 20f));

        var overriden = false;

        var scale = RogueAmuletItem.getVisualScale(stack);
        matrices.scale((float) scale, (float) scale, (float) scale);

        if(RogueAmuletItem.getVisualState(stack) != null) {
            matrices.push();
            matrices.translate(-0.5f, -0.5f, -0.5f);
            client.getBlockRenderManager().renderBlockAsEntity(
                    RogueAmuletItem.getVisualState(stack),
                    matrices,
                    vertexConsumers,
                    light,
                    overlay
            );
            matrices.pop();
            overriden = true;
        }
        if(RogueAmuletItem.getVisualStack(stack) != null) {
            matrices.push();
            client.getItemRenderer().renderItem(
                    RogueAmuletItem.getVisualStack(stack),
                    ModelTransformationMode.NONE,
                    light,
                    overlay,
                    matrices,
                    vertexConsumers,
                    world,
                    overlay
            );
            matrices.pop();
            overriden = true;
        }
        var displayEntityType = RogueAmuletItem.getVisualEntity(stack);
        if(displayEntityType != null) {
            var displayEntity = displayEntityType.create(world);
            if (displayEntity != null) {
                displayEntity.readNbt(RogueAmuletItem.getVisualEntityNbt(stack));

                matrices.push();
                matrices.translate(0f, -displayEntity.getHeight()/2, 0f);
                client.getEntityRenderDispatcher().render(
                        displayEntity,
                        0,
                        0,
                        0,
                        0,
                        client.getTickDelta(),
                        matrices,
                        vertexConsumers,
                        light
                );
                matrices.pop();
                overriden = true;
            }
        }
        if(!overriden) {
            var passives = RogueAmuletItem.getPassives(stack, client.world.getRegistryManager());
            if (!passives.isEmpty()) {
                int index = (int) (trueSeed % passives.size());
                if (index >= 0 && index < passives.size()){
                    var displayPassive = passives.get(index);
                    var model = client.getBakedModelManager().getModel(displayPassive.getModel(client.world.getRegistryManager()));

                    client.getItemRenderer().renderItem(
                            stack,
                            ModelTransformationMode.NONE,
                            mode.getIndex() == 1 || mode.getIndex() == 3,
                            matrices,
                            vertexConsumers,
                            light,
                            overlay,
                            model != null ? model : client.getBakedModelManager().getMissingModel()
                    );
                }
            }
        }

        matrices.pop();

        if (neck) {
            client.getItemRenderer().renderItem(
                    stack,
                    ModelTransformationMode.NONE,
                    mode.getIndex() == 1 || mode.getIndex() == 3,
                    matrices,
                    vertexConsumers,
                    light,
                    overlay,
                    client.getBakedModelManager().getModel(id("item/rogue_amulet_neck"))
            );
        }
    }
}