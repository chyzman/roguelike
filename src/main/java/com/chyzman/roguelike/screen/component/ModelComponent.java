package com.chyzman.roguelike.screen.component;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.Items;
import org.joml.Matrix4f;

public class ModelComponent extends BaseComponent {

    protected static final Matrix4f ITEM_SCALING = new Matrix4f().scaling(16, -16, 16);

    protected final VertexConsumerProvider.Immediate entityBuffers;
    protected final ItemRenderer itemRenderer;
    protected BakedModel model;

    public ModelComponent(BakedModel model) {
        this.entityBuffers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        this.itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        this.model = model;
    }

    @Override
    public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        final boolean notSideLit = !model.isSideLit();
        if (notSideLit) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        var matrices = context.getMatrices();
        matrices.push();

        // Translate to the root of the component
        matrices.translate(this.x, this.y, 100);

        // Scale according to component size and translate to the center
        matrices.scale(this.width / 16f, this.height / 16f, 1);
        matrices.translate(8.0, 8.0, 0.0);

        // Vanilla scaling and y inversion
        if (notSideLit) {
            matrices.scale(16, -16, 16);
        } else {
            matrices.multiplyPositionMatrix(ITEM_SCALING);
        }

        this.itemRenderer.renderItem(
                Items.POTATO.getDefaultStack(),
                ModelTransformationMode.GUI,
                false,
                matrices,
                entityBuffers,
                LightmapTextureManager.MAX_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV,
                model
        );

        this.entityBuffers.draw();

        // Clean up
        matrices.pop();

        if (notSideLit) {
            DiffuseLighting.enableGuiDepthLighting();
        }
    }
}