package cozmicweb.pda.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import cozmicweb.pda.common.content.reforge_modifier.ItemReforge;
import cozmicweb.pda.common.content.reforge_modifier.ReforgeManager;
import cozmicweb.pda.common.content.reforge_modifier.ReforgeModifierRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Collection;

public class ReforgeCommand {
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(target -> Component.literal("%s is not a valid entity for this command".formatted(target)));
    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(target -> Component.literal("%s is not holding any item".formatted(target)));
    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(item -> Component.literal("%s cannot support that reforge".formatted(item)));
    private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.literal("Nothing changed. Targets either have no item in their hands or the reforge could not be applied"));

    public static void register(@NonNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("reforge")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.entities())
                    .then(Commands.literal("add")
                        .then(Commands.argument("reforge", IdentifierArgument.id())
                            .suggests((_, b) -> {
                                String remaining = b.getRemainingLowerCase();
                                ReforgeModifierRegistry.getAllReforges().stream()
                                        .map(ItemReforge::id)
                                        .filter(id -> id.getPath().toLowerCase().contains(remaining))
                                        .map(Identifier::toString)
                                        .forEach(b::suggest);
                                return b.buildFuture();
                            })
                            .executes(c -> reforge(
                                    c.getSource(),
                                    EntityArgument.getEntities(c, "targets"),
                                    IdentifierArgument.getId(c, "reforge"))
                            )
                        )
                    )
                    .then(Commands.literal("remove")
                            .executes(c -> remove(c.getSource(), EntityArgument.getEntities(c, "targets")))
                    )
                )
        );
    }

    private static int reforge(CommandSourceStack source, @NonNull Collection<? extends Entity> targets, Identifier reforge) throws CommandSyntaxException {
        int success = 0;

        if (reforge.getNamespace().equals("minecraft")) {
            reforge = Identifier.parse("pda:" + reforge.getPath());
        }

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity target) {
                ItemStack item = target.getMainHandItem();
                if (!item.isEmpty()) {
                    if (ReforgeManager.addReforge(item, reforge)) {
                        success++;
                    } else {
                        throw ERROR_INCOMPATIBLE.create(item.getHoverName().getString());
                    }
                } else if (targets.size() == 1) {
                    throw ERROR_NO_ITEM.create(target.getName().getString());
                }
            } else if (targets.size() == 1) {
                throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
            }
        }

        if (success == 0) {
            throw ERROR_NOTHING_HAPPENED.create();
        }

        Identifier finalReforge = reforge;
        if (targets.size() == 1) {
            Entity target = targets.iterator().next();
            source.sendSuccess(() -> Component.literal("Applied reforge ")
                    .append(Component.literal(finalReforge.toString()))
                    .append(Component.literal(" to "))
                    .append(target.getDisplayName())
                    .append(Component.literal("'s item")), true);
        } else {
            source.sendSuccess(() -> Component.literal("Applied reforge ")
                    .append(Component.literal(finalReforge.toString()))
                    .append(Component.literal(" to "))
                    .append(Component.literal(String.valueOf(targets.size())))
                    .append(Component.literal(" entities")), true);
        }

        return success;
    }

    private static int remove(@NonNull CommandSourceStack source, @NonNull Collection<? extends Entity> targets) throws CommandSyntaxException {
        int success = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity target) {
                ItemStack item = target.getMainHandItem();
                if (!item.isEmpty()) {
                    ReforgeManager.removeReforge(item);
                    success++;
                } else if (targets.size() == 1) {
                    throw ERROR_NO_ITEM.create(target.getName().getString());
                }
            } else if (targets.size() == 1) {
                throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
            }
        }

        if (success == 0) {
            throw ERROR_NOTHING_HAPPENED.create();
        }

        if (targets.size() == 1) {
            Entity target = targets.iterator().next();
            source.sendSuccess(() -> Component.literal("Removed reforge from ")
                    .append(target.getDisplayName())
                    .append(Component.literal("'s item")), true);
        } else {
            source.sendSuccess(() -> Component.literal("Removed reforge from ")
                    .append(Component.literal(String.valueOf(targets.size())))
                    .append(Component.literal(" entities")), true);
        }

        return success;
    }

}