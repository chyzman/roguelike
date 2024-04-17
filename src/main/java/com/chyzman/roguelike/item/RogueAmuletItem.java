package com.chyzman.roguelike.item;

import com.chyzman.roguelike.classes.RoguelikePassive;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.BuiltInEndecs;
import io.wispforest.owo.serialization.endec.KeyedEndec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.chyzman.roguelike.Roguelike.PASSIVE_REGISTRY;

public class RogueAmuletItem extends Item {
    public static final KeyedEndec<List<Identifier>> PASSIVES = BuiltInEndecs.IDENTIFIER.listOf().keyed("Passives", ArrayList::new);

    public static final KeyedEndec<BlockState> DISPLAY_STATE = Endec.ofCodec(BlockState.CODEC).keyed("DisplayState", Blocks.TINTED_GLASS.getDefaultState());

    public static final KeyedEndec<BlockState> VISUAL_STATE = Endec.ofCodec(BlockState.CODEC).keyed("VisualState", (BlockState) null);
    public static final KeyedEndec<ItemStack> VISUAL_STACK = BuiltInEndecs.ITEM_STACK.keyed("VisualStack", (ItemStack) null);
    public static final KeyedEndec<EntityType<?>> VISUAL_ENTITY = BuiltInEndecs.ofRegistry(Registries.ENTITY_TYPE).keyed("VisualEntity", (EntityType<?>) null);
    public static final KeyedEndec<NbtCompound> VISUAL_ENITITY_NBT = Endec.ofCodec(NbtCompound.CODEC).keyed("VisualEntityNbt", new NbtCompound());

    public static final KeyedEndec<Double> VISUAL_SCALE = Endec.DOUBLE.keyed("VisualScale", 1.0);

    public RogueAmuletItem(Settings settings) {
        super(settings);
    }

    public static List<RoguelikePassive> getPassives(ItemStack stack, DynamicRegistryManager drm) {
        var registry = drm.get(PASSIVE_REGISTRY);
        var passives = stack.get(PASSIVES);
        return passives.stream().map(registry::get).filter(Objects::nonNull).toList();
    }

    public static void setPassives(ItemStack stack, List<RoguelikePassive> passives, DynamicRegistryManager drm) {
        var registry = drm.get(PASSIVE_REGISTRY);
        stack.put(PASSIVES, passives.stream().map(registry::getId).toList());
    }

    public static void addPassive(ItemStack stack, int index, RoguelikePassive passive, DynamicRegistryManager drm) {
        var registry = drm.get(PASSIVE_REGISTRY);
        var passives = stack.get(PASSIVES);
        passives.add(index, registry.getId(passive));
        setPassives(stack, passives.stream().map(registry::get).toList(), drm);
    }

    public static void removePassive(ItemStack stack, int index, DynamicRegistryManager drm) {
        var passives = stack.get(PASSIVES);
        passives.remove(index);
        setPassives(stack, passives.stream().map(drm.get(PASSIVE_REGISTRY)::get).toList(), drm);
    }

    @Nullable
    public static BlockState getDisplayState(ItemStack stack) {
        return stack.get(DISPLAY_STATE);
    }

    public static void setDisplayState(ItemStack stack, BlockState state) {
        stack.put(DISPLAY_STATE, state);
    }

    @Nullable
    public static BlockState getVisualState(ItemStack stack) {
        return stack.get(VISUAL_STATE);
    }

    public static void setVisualState(ItemStack stack, BlockState state) {
        stack.put(VISUAL_STATE, state);
    }

    @Nullable
    public static ItemStack getVisualStack(ItemStack stack) {
        return stack.get(VISUAL_STACK);
    }

    public static void setVisualStack(ItemStack stack, ItemStack visualStack) {
        stack.put(VISUAL_STACK, visualStack);
    }

    @Nullable
    public static EntityType<?> getVisualEntity(ItemStack stack) {
        return stack.get(VISUAL_ENTITY);
    }

    public static void setVisualEntity(ItemStack stack, EntityType<?> visualEntity) {
        stack.put(VISUAL_ENTITY, visualEntity);
    }

    @Nullable
    public static NbtCompound getVisualEntityNbt(ItemStack stack) {
        return stack.get(VISUAL_ENITITY_NBT);
    }

    public static void setVisualEntityNbt(ItemStack stack, NbtCompound visualEntityNbt) {
        stack.put(VISUAL_ENITITY_NBT, visualEntityNbt);
    }

    public static double getVisualScale(ItemStack stack) {
        return stack.get(VISUAL_SCALE);
    }

    public static void setVisualScale(ItemStack stack, double visualScale) {
        stack.put(VISUAL_SCALE, visualScale);
    }

    @Override
    public Optional<net.minecraft.client.item.TooltipData> getTooltipData(ItemStack stack) {
        return Optional.of(new TooltipData(stack));
    }

    public record TooltipData(ItemStack stack) implements net.minecraft.client.item.TooltipData {
    }
}