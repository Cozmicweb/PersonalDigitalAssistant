package cozmicweb.pda.common.content.raider_army;

import com.google.common.collect.ImmutableList;
import cozmicweb.pda.common.content.raider_army.raiders.RaiderGroup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public enum ArmyDifficulty {
    SUPER_EASY("event.pda.army.super_easy", Identifier.withDefaultNamespace("story/smelt_iron"), 3, (w, p) -> wSc(w, 31.84f) * pSc(p, 0.1f)),
    EASY("event.pda.army.easy", Identifier.withDefaultNamespace("story/mine_diamond"), 5, (w, p) -> wSc(w, 38.21f) * pSc(p, 0.2f)),
    NORMAL("event.pda.army.normal", Identifier.withDefaultNamespace("nether/obtain_blaze_rod"), 10, (w, p) -> wSc(w, 53.71f) * pSc(p, 0.3f)),
    HARD("event.pda.army.hard", Identifier.withDefaultNamespace("nether/obtain_ancient_debris"), 15, (w, p) -> wSc(w, 77.65f) * pSc(p, 0.3f)),
    IMPOSSIBLE("event.pda.army.impossible", null, 20, (w, p) -> wSc(w, 106.58f) * pSc(p, 0.2f));

    public final String translation;
    public final @Nullable Identifier advancement;
    public final int maxWave;
    public final BiFunction<Integer, Integer, Float> budget; /* <Wave, Player Count, Budget> */
    public final ImmutableList<RaiderGroup<?>> pool;

    ArmyDifficulty(String translation, @Nullable Identifier advancement, int maxWave, BiFunction<Integer, Integer, Float> budget, List<RaiderGroup<?>> pool) {
        this.translation = translation;
        this.advancement = advancement;
        this.maxWave = maxWave;
        this.budget = budget;
        this.pool = ImmutableList.copyOf(pool);
    }

    ArmyDifficulty(String translation, Identifier advancement, int maxWave, BiFunction<Integer, Integer, Float> budget) {
        this(translation, advancement, maxWave, budget, RaiderGroup.ALL_RAIDERS);
    }

    public float computeBudget(int wave, int playerCount) {
        return budget.apply(wave, playerCount);
    }

    public static @NonNull List<ArmyDifficulty> getUnlocked(Player player) {
        List<ArmyDifficulty> unlocked = new ArrayList<>();

        if (player instanceof ServerPlayer serverPlayer) {
            ServerAdvancementManager manager = serverPlayer.level().getServer().getAdvancements();

            Arrays.stream(ArmyDifficulty.values()).forEach(v -> {
                Optional.ofNullable(v.advancement).ifPresent(a -> {
                    boolean has = Optional.ofNullable(manager.get(a))
                            .map(holder -> serverPlayer.getAdvancements().getOrStartProgress(holder).isDone())
                            .orElse(false);
                    if (has)
                        unlocked.add(v);
                });
            });
        }

        return unlocked;
    }

    public static List<ArmyDifficulty> getUnlockedWithAdvancement(Player player) {
        return getUnlocked(player).stream().filter(a -> a.advancement != null).toList();
    }

    public static <ArmyDifficulty> ArmyDifficulty weightedRandom(@NonNull List<ArmyDifficulty> list, @NonNull RandomSource random) {
        int totalWeight = list.size() * (list.size() + 1) / 2;

        int value = random.nextInt(totalWeight);

        for (int i = 0; i < list.size(); i++) {
            value -= (i + 1);
            if (value < 0) {
                return list.get(i);
            }
        }

        return list.getLast();
    }

    public static Optional<ArmyDifficulty> getRandomUnlocked(Player player) {
        List<ArmyDifficulty> unlocked = getUnlockedWithAdvancement(player);
        return unlocked.isEmpty() ? Optional.empty() : Optional.of(weightedRandom(unlocked, player.getRandom()));
    }

    private static float wSc(int wave, float scale) {
        return (float) (wave * Math.pow(scale, 1.1f));
    }

    private static float pSc(int playerCount, float perPlayerBonus) {
        return 1f + Math.max(0, playerCount - 1) * perPlayerBonus;
    }

}