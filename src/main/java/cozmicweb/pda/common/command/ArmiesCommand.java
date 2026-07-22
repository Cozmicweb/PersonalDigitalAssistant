package cozmicweb.pda.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import cozmicweb.pda.common.content.raider_army.ArmyDifficulty;
import cozmicweb.pda.common.content.raider_army.ArmyManager;
import cozmicweb.pda.common.content.raider_army.RaiderArmy;
import cozmicweb.pda.common.content.raider_army.shop.WaveReceipt;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.server.command.EnumArgument;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class ArmiesCommand {

    public static void register(@NonNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("army")
                        .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                        .then(
                                Commands.literal("start")
                                        .then(
                                                Commands.argument("difficulty", EnumArgument.enumArgument(ArmyDifficulty.class))
                                                        .executes(c -> start(c.getSource(), c.getArgument("difficulty", ArmyDifficulty.class)))
                                        )
                        )
                        .then(
                                Commands.literal("stop")
                                        .executes(c -> stop(c.getSource(), false))
                                        .then(
                                                Commands.literal("all")
                                                        .executes(c -> stop(c.getSource(), true))
                                        )

                        )
                        .then(
                                Commands.literal("skip")
                                        .executes(c -> skip(c.getSource()))
                        )
                        .then(
                                Commands.literal("query")
                                        .executes(c -> query(c.getSource(), null))
                                        .then(
                                                Commands.argument("wave", IntegerArgumentType.integer(1))
                                                        .executes(c -> query(c.getSource(), IntegerArgumentType.getInteger(c, "wave")))
                                        )
                        )
                        .then(Commands.literal("peek").executes(c -> peek(c.getSource())))
        );
    }

    private static int start(@NonNull CommandSourceStack source, ArmyDifficulty difficulty) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BlockPos pos = player.blockPosition();

        if (ArmyManager.getArmyAtPos(pos, player.level()) != null) {
            source.sendFailure(Component.literal("Army already active close by"));
            return -1;
        }

        ArmyManager.spawnArmyAtPlayer(player, difficulty);
        source.sendSuccess(() -> Component.literal("Spawned a pillager army"), false);
        return 1;
    }

    private static int stop(@NonNull CommandSourceStack source, boolean all) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BlockPos pos = player.blockPosition();

        if (all) {
            ArmyManager.cleanAllArmies();
            source.sendSuccess(() -> Component.literal("Stopped all armies"), false);
            return 1;
        } else {
            RaiderArmy army = ArmyManager.getArmyAtPos(pos, player.level());
            if (army != null) {
                army.stop();
                source.sendSuccess(() -> Component.literal("Stopped army"), false);
                return 1;
            } else {
                source.sendFailure(Component.literal("No army here"));
                return -1;
            }
        }
    }

    private static int skip(@NonNull CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BlockPos pos = player.blockPosition();
        RaiderArmy army = ArmyManager.getArmyAtPos(pos, player.level());

        if (army != null) {
            source.sendSuccess(() -> Component.literal("Skipped wave " + army.getWave()), false);
            army.getActiveMembers().forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
            return 1;
        } else {
            source.sendFailure(Component.literal("No army here"));
            return -1;
        }
    }

    private static int query(@NonNull CommandSourceStack source, @Nullable Integer wave) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BlockPos pos = player.blockPosition();
        RaiderArmy army = ArmyManager.getArmyAtPos(pos, player.level());

        if (army == null) {
            source.sendFailure(Component.literal("Found no army nearby"));
            return 0;
        }

        if (wave != null) {
            WaveReceipt receipt = army.getReceipt(wave);
            if (receipt == null) {
                source.sendFailure(Component.literal("No receipt recorded for wave " + wave));
                return 0;
            }

            receipt.getReceipt().forEach(line -> source.sendSuccess(() -> line, false));
            return 1;
        }

        sendOverview(source, army);
        return 1;
    }

    private static void sendOverview(@NonNull CommandSourceStack source, @NonNull RaiderArmy army) {
        source.sendSuccess(ArmiesCommand::divider, false);

        source.sendSuccess(() -> {
            MutableComponent line = Component.empty();
            line.append(Component.literal("Army ").withStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.RED)));
            line.append(Component.literal("(" + army.id + ")").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
            return line;
        }, false);

        source.sendSuccess(() -> labeled("Difficulty", Component.translatable(army.difficulty.translation).withStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.RED))), false);
        source.sendSuccess(() -> labeled("Wave", Component.literal(army.getWave() + "/" + army.getMaxWave()).withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE))), false);
        source.sendSuccess(() -> labeled("Members", Component.literal(String.valueOf(army.getMemberCount())).withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))), false);
        source.sendSuccess(() -> labeled("Health", Component.literal("%.0f / %.0f".formatted(army.calculateWaveHealth(false), army.getWaveMaxHealth())).withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))), false);

        List<Integer> recordedWaves = army.getRecordedWaves().stream().sorted().toList();
        if (!recordedWaves.isEmpty()) {
            source.sendSuccess(Component::empty, false);
            String waves = recordedWaves.stream().map(String::valueOf).collect(Collectors.joining(", "));
            source.sendSuccess(() -> labeled("Recorded waves", Component.literal(waves).withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA))), false);
            source.sendSuccess(() -> Component.literal("Use /army query <wave> to see that wave's shop receipt").withStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)), false);
        }

        source.sendSuccess(ArmiesCommand::divider, false);
    }

    private static @NonNull Component labeled(@NonNull String label, @NonNull Component value) {
        return Component.literal(label + ": ").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)).append(value);
    }

    private static @NonNull Component divider() {
        return Component.literal(" ".repeat(58)).withStyle(Style.EMPTY.withStrikethrough(true).withColor(ChatFormatting.DARK_GRAY));
    }

    private static int peek(@NonNull CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BlockPos pos = player.blockPosition();
        RaiderArmy army = ArmyManager.getArmyAtPos(pos, player.level());
        if (army != null) {
            army.peekActiveMembers(500);
            source.sendSuccess(() -> Component.literal("Peeked at " + army.getActiveMembers().size() + " members"), false);
        } else {
            source.sendFailure(Component.literal("No army nearby").withStyle(ChatFormatting.RED));
        }
        return 1;
    }

}