package cozmicweb.pda.common.content.raider_army;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.registry.ModGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class ArmyManager {
    private ArmyManager() {}

    private static final Map<UUID, RaiderArmy> activeArmies = new ConcurrentHashMap<>();

    public static @Nullable RaiderArmy getArmyFromId(UUID id) {
        return activeArmies.get(id);
    }

    public static @Nullable RaiderArmy getArmyAtPos(BlockPos pos, Level level) {
        return activeArmies.values().stream().filter(army -> army.level.equals(level) && army.doesContainPosition(pos)).findFirst().orElse(null);
    }

    private static void createArmyAtPos(UUID id, BlockPos center, Level level, ArmyDifficulty difficulty) {
        activeArmies.computeIfAbsent(id, _ -> new RaiderArmy(id, center, level, difficulty));
    }

    public static void cleanAllArmies() {
        activeArmies.forEach((_, army) -> army.stop());
        activeArmies.clear();
    }

    public static void spawnArmyAtPlayer(@NonNull Player player, ArmyDifficulty difficulty) {
        Level level = player.level();
        RandomSource random = level.getRandom();

        BlockPos pos = getSuitableLocation(level, player.blockPosition());
        ArmyManager.createArmyAtPos(Mth.createInsecureUUID(random), pos, level, difficulty);
    }

    public static @NonNull BlockPos getSuitableLocation(@NonNull Level level, BlockPos origin) {
        RandomSource random = level.getRandom();
        BlockPos pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, origin);

        int iteration = 0;
        boolean isSafe;

        do {
            BlockState state = level.getBlockState(pos);
            isSafe = state.getFluidState().isEmpty() && !state.is(BlockTags.FIRE);
            if (!isSafe) {
                int offsetX = random.nextInt(3) - 1;
                int offsetZ = random.nextInt(3) - 1;
                pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos.offset(offsetX, 0, offsetZ));
            }
            iteration++;
        } while (!isSafe && iteration < 20);

        return pos.immutable();
    }

    public static boolean canSpawnArmy(@NonNull Player player) {
        return ArmyManager.getArmyAtPos(player.blockPosition(), player.level()) == null
                && player.level().dimension() == Level.OVERWORLD
                && player.gameMode() != GameType.SPECTATOR
                && player.getY() > PDAConfig.RAIDER_ARMY_MIN_Y.get();
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.@NonNull Post event) {
        if (event.getEntity().level() instanceof ServerLevel level && !level.getGameRules().get(ModGameRules.SPAWN_ARMIES.get()))
            return;

        if (event.getEntity() instanceof Player player && canSpawnArmy(player))
            if (player.getRandom().nextInt(140000) == 0) // 50% to have spawned within the next 4 days (80 minutes)
                ArmyDifficulty.getRandomUnlocked(player).ifPresent(difficulty -> spawnArmyAtPlayer(player, difficulty));
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.@NonNull Post event) {
        Level tickedLevel = event.getLevel();

        for (RaiderArmy army : activeArmies.values()) {
            Level armyLevel = army.level;

            if (tickedLevel.equals(armyLevel) && tickedLevel.dimension().equals(armyLevel.dimension())) {
                if (!army.isActive()) {
                    army.stop();
                    activeArmies.remove(army.id);
                    continue;
                }

                army.tick();
            }
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        cleanAllArmies();
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        cleanAllArmies();
    }
}
