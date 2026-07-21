package cozmicweb.pda.common.item;

import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.content.raider_army.ArmyDifficulty;
import cozmicweb.pda.common.content.raider_army.ArmyManager;
import cozmicweb.pda.common.content.raider_army.BattleStandardType;
import cozmicweb.pda.common.content.raider_army.RaiderArmy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class BattleStandardItem extends Item {

    private final BattleStandardType type;

    public BattleStandardItem(Properties properties, BattleStandardType type) {
        super(properties);
        if (type == BattleStandardType.STANDARD)
            throw new IllegalArgumentException("Empty battle standard items cannot be created with this class");
        this.type = type;
    }

    public BattleStandardType getType() {
        return type;
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        BlockPos pos = player.blockPosition();
        InteractionResult result = InteractionResult.PASS;

        if (type == BattleStandardType.OMINOUS) {
            RaiderArmy army = ArmyManager.getArmyAtPos(pos, level);
            if (army != null) {
                int nextDiff = army.difficulty.ordinal() + 1;
                if (nextDiff >= ArmyDifficulty.values().length) {
                    if (level instanceof ServerLevel)
                        player.sendSystemMessage(Component.translatable("text.pda.standard.fail.max").withColor(TextColor.RED));
                    result = InteractionResult.FAIL;
                } else {
                    army.stop();
                    ArmyDifficulty difficulty = ArmyDifficulty.values()[nextDiff];
                    ArmyManager.spawnArmyAtPlayer(player, difficulty);
                    if (level instanceof ServerLevel)
                        player.sendSystemMessage(Component.translatable("text.pda.standard.success.increase", Component.translatable(difficulty.translation)).withColor(TextColor.GREEN));
                    result = InteractionResult.SUCCESS;
                }
            } else {
                if (ArmyManager.canSpawnArmy(player)) {
                    ArmyManager.spawnArmyAtPlayer(player, ArmyDifficulty.EASY);
                    if (level instanceof ServerLevel)
                        player.sendSystemMessage(Component.translatable("text.pda.standard.success.start").withColor(TextColor.GREEN));
                    result = InteractionResult.SUCCESS;
                } else {
                    if (level instanceof ServerLevel)
                        player.sendSystemMessage(Component.translatable("text.pda.standard.fail.bad_spot", PDAConfig.RAIDER_ARMY_MIN_Y.get()).withColor(TextColor.RED));
                    result = InteractionResult.FAIL;
                }
            }
        } else if (type == BattleStandardType.WARDING) {
            RaiderArmy army = ArmyManager.getArmyAtPos(pos, level);
            if (army != null) {
                army.stop();
                if (level instanceof ServerLevel)
                    player.sendSystemMessage(Component.translatable("text.pda.standard.success.stop").withColor(TextColor.GREEN));
                result = InteractionResult.SUCCESS;
            } else {
                if (level instanceof ServerLevel)
                    player.sendSystemMessage(Component.translatable("text.pda.standard.fail.inactive_here").withColor(TextColor.RED));
                result = InteractionResult.FAIL;
            }
        }

        if (result == InteractionResult.SUCCESS) {
            player.swing(hand);
            if (!player.isCreative())
                player.getInventory().removeItem(player.getItemInHand(hand));
            RandomSource random = player.getRandom();
            for (int i = 0; i < 5; i++) {
                double xa = random.nextGaussian() * 0.02;
                double ya = random.nextGaussian() * 0.02;
                double za = random.nextGaussian() * 0.02;
                level.addParticle(ParticleTypes.WITCH, player.getRandomX(1.0), player.getRandomY() + 1.0, player.getRandomZ(1.0), xa, ya, za);
            }
            if (level instanceof ServerLevel serverLevel)
                serverLevel.playSound(null, pos, SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 0.5f, 1.0f);
        } else if (result == InteractionResult.FAIL) {
            if (level instanceof ServerLevel serverLevel)
                serverLevel.playSound(null, pos, SoundEvents.SHIELD_BLOCK.value(), SoundSource.PLAYERS, 0.4f, 1.0f);
        }

        return result;
    }

}
