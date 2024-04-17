package com.chyzman.roguelike.item;

import io.wispforest.owo.ui.base.BaseOwoTooltipComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@SuppressWarnings("UnstableApiUsage")
public class RogueAmuletTooltipComponent extends BaseOwoTooltipComponent<FlowLayout> {
    public RogueAmuletTooltipComponent(ItemStack stack, DynamicRegistryManager drm) {
        super(() -> {
            var flow = Containers.verticalFlow(Sizing.content(), Sizing.content());
            if (stack.getItem() instanceof RogueAmuletItem) {
                var passives = RogueAmuletItem.getPassives(stack, drm);
                if (!passives.isEmpty()) {
                    flow.child(Components.label(Text.translatable("item.roguelike.rogue_amulet.passives").formatted(Formatting.GRAY)));
                    flow.child(
                            Components.list(
                                    passives,
                                    flowLayout -> {},
                                    roguelikePassive -> roguelikePassive.getTooltipComponent(drm),
                                    true
                            ).margins(Insets.left(2))
                    );
                }
            }
            return flow;
        });
    }
}