package cozmicweb.pda.common.content.raider_army;

import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.content.raider_army.raiders.RaiderGroup;
import cozmicweb.pda.common.content.raider_army.shop.RaiderShop;
import cozmicweb.pda.common.content.raider_army.shop.ReceiptEntry;
import cozmicweb.pda.common.content.raider_army.shop.WaveReceipt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class RaiderArmy {

    private static final Direction[] DIRECTIONS = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private final List<LivingEntity> activeMembers;
    private final ServerBossEvent armyEvent;
    private final AABB armyArea;
    private final int maxWave;
    private final Map<Integer, WaveReceipt> receipts;

    public final UUID id;
    public final BlockPos center;
    public final Level level;
    public final ArmyDifficulty difficulty;
    public final int size;
    public final RandomSource random;

    private Direction lastDirection;
    private int wave;
    private int tick;
    private boolean active;
    private float waveMaxHealth;
    private Integer emptyTick;
    private Integer pauseStartTick;

    public RaiderArmy(UUID id, BlockPos center, @NonNull Level level, @NonNull ArmyDifficulty difficulty) {
        this.id = id;
        this.center = center;
        this.level = level;
        this.difficulty = difficulty;
        this.size = PDAConfig.RAIDER_ARMY_SIZE.get() * 16;
        this.random = level.getRandom();

        this.lastDirection = Direction.NORTH;
        this.wave = 0;
        this.tick = 0;
        this.active = true;
        this.waveMaxHealth = 1;
        this.emptyTick = null;
        this.pauseStartTick = 0;

        this.activeMembers = new ArrayList<>();
        this.receipts = new LinkedHashMap<>();
        this.armyArea = AABB.ofSize(Vec3.atCenterOf(center), this.size, this.size, this.size);
        this.maxWave = difficulty.maxWave;
        this.armyEvent = new ServerBossEvent(this.id, buildActiveTitle(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_12);
        this.armyEvent.setVisible(true);
    }

    public void tick() {
        if (activeMembers.isEmpty()) {
            if (wave >= maxWave) {
                active = false;
                armyEvent.getPlayers().forEach(p -> p.sendSystemMessage(Component.translatable("event.pda.army.win").withColor(TextColor.GREEN)));
            } else {
                handleWavePause();
            }
        } else {
            updateEventProgress();
        }

        updateEventPlayers();

        if (tick % 100 == 0)
            recallFarMembers();

        if (tick % 400 == 0)
            peekActiveMembers(400);

        cleanActiveMembers();
        checkForAbandonment();

        tick++;
    }

    public void stop() {
        activeMembers.forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
        activeMembers.clear();
        armyEvent.removeAllPlayers();
        armyEvent.setVisible(false);
        active = false;
    }

    public boolean isActive() {
        boolean shouldTimeout = tick > (PDAConfig.RAIDER_ARMY_IDLE_TIMEOUT.get() * 20);
        boolean shouldAbandon = emptyTick != null && tick - emptyTick > (PDAConfig.RAIDER_ARMY_ABANDON_TIMEOUT.get() * 20);
        boolean isRunning = wave <= maxWave;
        return active && isRunning && !shouldTimeout && !shouldAbandon;
    }

    private void spawnNextWave() {
        wave++;
        activeMembers.addAll(spawnMembers());
        waveMaxHealth = calculateWaveHealth(true);
    }

    private void handleWavePause() {
        if (pauseStartTick == null) {
            pauseStartTick = tick;
            generateSpawnDirection();
        }

        updateEventStart();

        if (tick - pauseStartTick >= getStartTime()) {
            pauseStartTick = null;
            spawnNextWave();
        }
    }

    public int getStartTime() {
        return PDAConfig.RAIDER_ARMY_START_TIME.get() * 20;
    }

    public void updateEventStart() {
        armyEvent.setName(buildStartTitle());
        int elapsed = tick - pauseStartTick;
        int time = getStartTime();

        if (time == 0)
            armyEvent.setProgress(1);
        else
            armyEvent.setProgress(Math.clamp((float) elapsed / time, 0f, 1f));
    }

    public void updateEventProgress() {
        armyEvent.setName(buildActiveTitle());
        armyEvent.setProgress(Math.clamp(calculateWaveHealth(false) / waveMaxHealth, 0, 1));
    }

    private void updateEventPlayers() {
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, armyArea);
        nearbyPlayers.forEach(this::addEventPlayer);

        List<Player> toRemove = new ArrayList<>();
        armyEvent.getPlayers().stream().filter(p -> !nearbyPlayers.contains(p)).forEach(toRemove::add);
        toRemove.forEach(this::removeEventPlayer);
    }

    private void cleanActiveMembers() {
        activeMembers.removeIf(e -> e.isDeadOrDying() || !e.isAlive() || e.isRemoved());
    }

    private void checkForAbandonment() {
        if (emptyTick == null && armyEvent.getPlayers().isEmpty())
            emptyTick = tick;
        else if (emptyTick != null && !armyEvent.getPlayers().isEmpty())
            emptyTick = null;
    }

    private void recallFarMembers() {
        AtomicBoolean recalled = new AtomicBoolean(false);

        activeMembers.stream().filter(p -> !doesContainPosition(p.blockPosition())).forEach(e -> {
            e.setPos(Vec3.atCenterOf(center));
            recalled.set(true);
        });

        if (recalled.get())
            peekActiveMembers(200);
    }

    public float calculateWaveHealth(boolean max) {
        Function<LivingEntity, Float> method = max ? LivingEntity::getMaxHealth : LivingEntity::getHealth;
        return activeMembers == null ? 1 : activeMembers.stream().map(method).reduce(0.0f, Float::sum);
    }

    public void peekActiveMembers(int duration) {
        MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, duration, 3, true, false);
        activeMembers.forEach(e -> e.addEffect(effect));
    }

    private void addEventPlayer(Player player) {
        if (player instanceof ServerPlayer serverPlayer)
            armyEvent.addPlayer(serverPlayer);
    }

    private void removeEventPlayer(Player player) {
        if (player instanceof ServerPlayer serverPlayer)
            armyEvent.removePlayer(serverPlayer);
    }

    public boolean doesContainPosition(@NonNull BlockPos pos) {
        return pos.closerThan(center, this.size * 0.5);
    }

    public boolean doesRadiusOverlap(@NonNull BlockPos pos, int radius) {
        return pos.closerThan(center, radius);
    }

    private @NonNull Component buildStartTitle() {
        return Component.translatable("event.pda.army.warning", lastDirection.getName()).withColor(TextColor.RED);
    }

    private @NonNull Component buildActiveTitle() {
        MutableComponent nameComponent = Component.translatable("event.pda.army.title.name", Component.translatable(difficulty.translation).withColor(TextColor.RED)).withColor(TextColor.WHITE);
        MutableComponent waveComponent = Component.translatable("event.pda.army.title.wave", Component.literal(String.valueOf(wave)).withColor(TextColor.RED), Component.literal(String.valueOf(maxWave)).withColor(TextColor.RED));
        MutableComponent leftComponent = Component.translatable("event.pda.army.title.left", Component.literal(String.valueOf(activeMembers.size())).withColor(TextColor.RED));
        MutableComponent timeComponent = Component.translatable("event.pda.army.title.time", Component.literal(formatTime()).withColor(TextColor.RED));

        Component separator = Component.literal(" | ").withColor(TextColor.GRAY);

        return nameComponent.append(separator)
                .append(waveComponent).append(separator)
                .append(leftComponent).append(separator)
                .append(timeComponent);
    }

    private @NonNull String formatTime() {
        int totalSeconds = tick / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void generateSpawnDirection() {
        lastDirection = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
    }

    private List<RaiderGroup<?>> shopForWave() {
        int playerCount = Math.max(armyEvent.getPlayers().size(), 1);
        float budget = difficulty.computeBudget(wave, playerCount);
        List<RaiderGroup<?>> composition = RaiderShop.shop(difficulty.pool, budget, random);

        receipts.put(wave, buildReceipt(budget, composition));

        return composition;
    }

    private @NonNull WaveReceipt buildReceipt(float budget, @NonNull List<RaiderGroup<?>> composition) {
        Map<RaiderGroup<?>, Integer> counts = new LinkedHashMap<>();
        composition.forEach(group -> counts.merge(group, 1, Integer::sum));

        List<ReceiptEntry> entries = counts.entrySet().stream()
                .map(e -> new ReceiptEntry(e.getKey(), e.getValue()))
                .toList();

        return new WaveReceipt(difficulty, wave, budget, entries);
    }

    private List<BlockPos> computeSpawnPositions(@NonNull ServerLevel serverLevel, int count) {
        List<BlockPos> positions = new ArrayList<>(count);
        if (count == 0)
            return positions;

        BlockPos basePos = center.relative(lastDirection, (int) (size * 0.4));
        Direction perpendicular = lastDirection.getClockWise();

        Vec3 perpVec = Vec3.atLowerCornerOf(perpendicular.getUnitVec3i());
        Vec3 dirVec = Vec3.atLowerCornerOf(lastDirection.getUnitVec3i());

        double spacing = 4;
        double radius = Math.max(Math.sqrt((count * spacing * spacing) / (Math.PI / 2.0)), spacing);
        double angle = Math.PI * (3 - Math.sqrt(5));

        for (int index = 0; index < count; index++) {
            double r = radius * Math.sqrt((index + 0.5) / count);
            double theta = -Math.PI / 2 + ((index * angle) % Math.PI);

            double sin = Math.sin(theta);
            double cos = Math.cos(theta);

            double x = basePos.getX() + 0.5 + r * sin * perpVec.x - r * cos * dirVec.x;
            double z = basePos.getZ() + 0.5 + r * sin * perpVec.z - r * cos * dirVec.z;

            BlockPos linePos = BlockPos.containing(x, basePos.getY(), z);
            positions.add(ArmyManager.getSuitableLocation(serverLevel, linePos));
        }

        return positions;
    }

    public List<Mob> spawnMembers() {
        List<Mob> createdMembers = new ArrayList<>();
        if (!(level instanceof ServerLevel serverLevel))
            return createdMembers;

        List<RaiderGroup<?>> composition = shopForWave();
        List<BlockPos> positions = computeSpawnPositions(serverLevel, composition.size());

        for (int i = 0; i < composition.size(); i++) {
            Mob entity = composition.get(i).create(serverLevel);
            if (entity == null)
                continue;

            entity.setPos(Vec3.atCenterOf(positions.get(i)));
            entity.setPersistenceRequired();
            entity.setHealth(entity.getMaxHealth());
            entity.getPersistentData().putBoolean("starjam_army_mob", true);
            entity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(entity, Player.class, false));
            createdMembers.add(entity);

            if (entity instanceof Phantom)
                entity.setPos(entity.getX(), entity.getY() + 20, entity.getZ());

            serverLevel.sendParticles(
                    ParticleTypes.POOF,
                    true, true,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    (int) (random.nextInt(3, 12) * entity.getBoundingBox().getSize()),
                    entity.getBoundingBox().getXsize() / 2, entity.getBoundingBox().getYsize(), entity.getBoundingBox().getZsize() / 2,
                    0);

            serverLevel.addFreshEntity(entity);
        }

        return createdMembers;
    }

    public int getWave() {
        return wave;
    }

    public int getMaxWave() {
        return maxWave;
    }

    public List<LivingEntity> getActiveMembers() {
        return Collections.unmodifiableList(activeMembers);
    }

    public int getMemberCount() {
        return activeMembers.size();
    }

    public float getWaveMaxHealth() {
        return waveMaxHealth;
    }

    public @Nullable WaveReceipt getReceipt(int wave) {
        return receipts.get(wave);
    }

    public @NonNull @Unmodifiable List<WaveReceipt> getReceipts() {
        return List.copyOf(receipts.values());
    }

    public @NonNull @Unmodifiable Set<Integer> getRecordedWaves() {
        return Collections.unmodifiableSet(receipts.keySet());
    }

}