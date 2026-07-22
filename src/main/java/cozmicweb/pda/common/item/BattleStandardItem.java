package cozmicweb.pda.common.item;

import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.content.raider_army.ArmyDifficulty;
import cozmicweb.pda.common.content.raider_army.ArmyManager;
import cozmicweb.pda.common.content.raider_army.BattleStandardType;
import cozmicweb.pda.common.content.raider_army.RaiderArmy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class BattleStandardItem extends Item {

    private final BattleStandardType type;

    public BattleStandardItem(Properties properties, BattleStandardType type) {
        super(properties);
        if (type == BattleStandardType.STANDARD)
            throw new IllegalArgumentException("Empty battle standard items cannot be created with this class");
        this.type = type;
    }

    public String getDescription() {
        return switch (type) {
            case STANDARD -> "";
            case OMINOUS -> "Summons an army or increases the difficulty of the current one";
            case WARDING -> "Cancels current army event";
            case REVEALING -> "Reveals all raiders in the current army wave";
            case GATHERING -> "Brings random raiders to the center of the attack";
        };
    }

    public BattleStandardType getType() {
        return type;
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        BlockPos pos = player.blockPosition();
        InteractionResult result = switch (type) {
            case STANDARD -> InteractionResult.PASS;
            case OMINOUS -> ominous(level, player, pos);
            case WARDING -> warding(level, player, pos);
            case REVEALING -> revealing(level, player, pos);
            case GATHERING -> gathering(level, player, pos);
        };

        if (result == InteractionResult.SUCCESS) {
            player.swing(hand);
            if (!player.isCreative()) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isDamageableItem())
                    stack.hurtAndBreak(1, player, hand);
                else
                    player.getInventory().removeItem(stack);
            }
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

    private static InteractionResult ominous(Level level, Player player, BlockPos pos) {
        RaiderArmy army = ArmyManager.getArmyAtPos(pos, level);
        if (army != null) {
            int nextDiff = army.difficulty.ordinal() + 1;
            if (nextDiff >= ArmyDifficulty.values().length) {
                if (level instanceof ServerLevel)
                    player.sendSystemMessage(Component.translatable("text.pda.standard.fail.max").withStyle(ChatFormatting.RED));
                return InteractionResult.FAIL;
            } else {
                army.stop();
                ArmyDifficulty difficulty = ArmyDifficulty.values()[nextDiff];
                ArmyManager.spawnArmyAtPlayer(player, difficulty);
                if (level instanceof ServerLevel)
                    player.sendSystemMessage(Component.translatable("text.pda.standard.success.increase", Component.translatable(difficulty.translation)).withStyle(ChatFormatting.GREEN));
                return InteractionResult.SUCCESS;
            }
        } else {
            if (ArmyManager.canSpawnArmy(player)) {
                ArmyManager.spawnArmyAtPlayer(player, ArmyDifficulty.EASY);
                if (level instanceof ServerLevel)
                    player.sendSystemMessage(Component.translatable("text.pda.standard.success.start").withStyle(ChatFormatting.GREEN));
                return InteractionResult.SUCCESS;
            } else {
                if (level instanceof ServerLevel)
                    player.sendSystemMessage(Component.translatable("text.pda.standard.fail.bad_spot", PDAConfig.RAIDER_ARMY_MIN_Y.get()).withStyle(ChatFormatting.RED));
                return InteractionResult.FAIL;
            }
        }
    }

    private static InteractionResult warding(Level level, Player player, BlockPos pos) {
        RaiderArmy army = ArmyManager.getArmyAtPos(pos, level);
        if (army != null) {
            army.stop();
            if (level instanceof ServerLevel)
                player.sendSystemMessage(Component.translatable("text.pda.standard.success.stop").withStyle(ChatFormatting.GREEN));
            return InteractionResult.SUCCESS;
        } else {
            if (level instanceof ServerLevel)
                player.sendSystemMessage(Component.translatable("text.pda.standard.fail.inactive_here").withStyle(ChatFormatting.RED));
            return InteractionResult.FAIL;
        }
    }

    private static InteractionResult revealing(Level level, Player player, BlockPos pos) {
        RaiderArmy army = ArmyManager.getArmyAtPos(pos, level);
        if (army != null) {
            army.peekActiveMembers(999999);
            if (level instanceof ServerLevel)
                player.sendSystemMessage(Component.translatable("text.pda.standard.success.revealed").withStyle(ChatFormatting.GOLD));
            return InteractionResult.SUCCESS;
        } else {
            if (level instanceof ServerLevel)
                player.sendSystemMessage(Component.translatable("text.pda.standard.fail.inactive_here").withStyle(ChatFormatting.RED));
            return InteractionResult.FAIL;
        }
    }

    private static InteractionResult gathering(Level level, Player player, BlockPos pos) {
        RaiderArmy army = ArmyManager.getArmyAtPos(pos, level);
        if (army != null) {
            RandomSource random = player.getRandom();
            List<LivingEntity> members = army.getActiveMembers();
            int targetCount = Math.min(members.size(), random.nextInt(2, 6));
            for (int i = 0; i < targetCount; i++)
                members.get(random.nextInt(members.size() - 1)).setPos(Vec3.atCenterOf(army.center));
            if (level instanceof ServerLevel)
                player.sendSystemMessage(Component.translatable("text.pda.standard.success.gathered", targetCount).withStyle(ChatFormatting.LIGHT_PURPLE));
            return InteractionResult.SUCCESS;
        } else {
            if (level instanceof ServerLevel)
                player.sendSystemMessage(Component.translatable("text.pda.standard.fail.inactive_here").withStyle(ChatFormatting.RED));
            return InteractionResult.FAIL;
        }
    }

}
