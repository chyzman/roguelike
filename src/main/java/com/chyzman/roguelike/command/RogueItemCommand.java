package com.chyzman.roguelike.command;

import com.chyzman.roguelike.Roguelike;
import com.chyzman.roguelike.item.RogueAmuletItem;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RogueItemCommand {
    public static final SimpleCommandExceptionType NO_AMULET = new SimpleCommandExceptionType(Text.translatable("command.rogueitem.fail.no_amulet"));

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("rogueitem")
                            .then(literal("passive")
                                    .then(literal("add")
                                            .then(argument(
                                                            "passive",
                                                            IdentifierArgumentType.identifier()
                                                    )
                                                            .executes(context -> addPassive(
                                                                            context.getSource(),
                                                                            IdentifierArgumentType.getIdentifier(context, "passive"),
                                                                            -1
                                                                    )
                                                            )
                                                            .suggests((context, builder) -> {
                                                                var registry = context.getSource().getServer().getRegistryManager().get(Roguelike.PASSIVE_REGISTRY);
                                                                return CommandSource.suggestIdentifiers(registry.getIds(), builder);
                                                            })
                                                            .then(argument(
                                                                            "index",
                                                                            IntegerArgumentType.integer()
                                                                    )
                                                                            .executes(context -> addPassive(
                                                                                            context.getSource(),
                                                                                            IdentifierArgumentType.getIdentifier(context, "passive"),
                                                                                            IntegerArgumentType.getInteger(context, "index")
                                                                                    )
                                                                            )
                                                            )
                                            )
                                    )
                            )
            );
        });
    }

    private static int addPassive(ServerCommandSource source, Identifier passiveId, int index) throws CommandSyntaxException {
        Entity entity = source.getEntityOrThrow();
        var stack = findAmulet(entity);
        if (stack == null) throw NO_AMULET.create();
        var drm = source.getServer().getRegistryManager();
        var passive = drm.get(Roguelike.PASSIVE_REGISTRY).get(passiveId);
        var passivesSize = RogueAmuletItem.getPassives(stack.stackSupplier.get(), drm).size();
        var realIndex = index < 0 ? passivesSize + index + 1 : index;
        String indexString = index < 0 ? "" + index : index + "(" + realIndex + ")";
        if (realIndex < - passivesSize - 1) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().create(indexString, passivesSize);
        if (realIndex > passivesSize) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().create(indexString, passivesSize);
        var currentStack = stack.stackSupplier.get();
        RogueAmuletItem.addPassive(currentStack, realIndex, passive, source.getRegistryManager());
        stack.stackConsumer.accept(currentStack);
        source.sendFeedback(
                () -> {
                    //TODO make this use name or whatever idk
                    return Text.translatable("command.rogueitem.passive.add.success", passiveId.toTranslationKey(), indexString, entity.getDisplayName(), currentStack.toHoverableText());
                },
                false
        );
        return 1;
    }

    @Nullable
    public static AmuletReference findAmulet(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return null;
        var stack = living.getMainHandStack();
        if (stack.getItem() instanceof RogueAmuletItem) {
            return new AmuletReference(stack::copy, itemStack -> living.setStackInHand(Hand.MAIN_HAND, itemStack));
        }
        stack = living.getOffHandStack();
        if (stack.getItem() instanceof RogueAmuletItem) {
            return new AmuletReference(stack::copy, itemStack -> living.setStackInHand(Hand.OFF_HAND, itemStack));
        }
        var accessories = AccessoriesCapability.get(living);
        if (accessories == null) return null;
        return null;
        //TODO make this work with accessories
//        return accessories.getFirstEquipped(itemStack -> itemStack.getItem() instanceof RogueAmuletItem)
//                .map(slotEntryReference -> new AmuletReference(
//                        slotEntryReference::stack,
//                        itemStack -> accessories.
//
//                )).orElse(null);
    }

    public record AmuletReference(Supplier<ItemStack> stackSupplier, Consumer<ItemStack> stackConsumer) {
    }
}