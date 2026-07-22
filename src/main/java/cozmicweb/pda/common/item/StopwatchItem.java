package cozmicweb.pda.common.item;

import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.registry.ModComponents;
import cozmicweb.pda.common.registry.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class StopwatchItem extends Item implements IClickReactive {

    public StopwatchItem(@NonNull Properties properties) {
        super(properties
                .component(ModComponents.STOPWATCH_PAUSED.get(), true)
                .component(ModComponents.STOPWATCH_START_TIME.get(), -1L)
        );
    }

    private static long now(@NonNull ServerPlayer player) {
        return now(player.level());
    }

    private static long now(@NonNull ServerLevel level) {
        return level.getGameTime();
    }

    public static void tick(@NonNull ServerPlayer player, float elapsed, float volume) {
        if (PDAConfig.STOPWATCH_PLAY_SOUND.get()) {
            if (elapsed % 6 == 0)
                player.level().playSound(null, player.blockPosition(), ModSounds.STOPWATCH_TICK_0.value(), player.getSoundSource(), volume, 2.0F);
            else if (elapsed % 3 == 0)
                player.level().playSound(null, player.blockPosition(), ModSounds.STOPWATCH_TICK_1.value(), player.getSoundSource(), volume, 2.0F);
        }
    }

    public static void incrementAnim(ItemStack stack) {
        stack.set(ModComponents.STOPWATCH_ANIM, (stack.getOrDefault(ModComponents.STOPWATCH_ANIM, 0) + 1) % 16);
    }

    public static void setPressed(@NonNull ItemStack stack, boolean state) {
        stack.set(ModComponents.STOPWATCH_PRESSED.get(), state);
    }

    @Override
    public void onLeftClickDown(ServerPlayer player, @NonNull ItemStack stack) {
        setPressed(stack, true);
        TallyCounterItem.tick(player);

        boolean paused = stack.getOrDefault(ModComponents.STOPWATCH_PAUSED.get(), true);
        long now = now(player);

        if (paused) {
            long pauseStart = stack.getOrDefault(ModComponents.STOPWATCH_PAUSE_TIME.get(), now);
            long start = stack.getOrDefault(ModComponents.STOPWATCH_START_TIME.get(), now);
            stack.set(ModComponents.STOPWATCH_START_TIME.get(), start + (now - pauseStart));
        } else {
            stack.set(ModComponents.STOPWATCH_PAUSE_TIME.get(), now);
        }

        stack.set(ModComponents.STOPWATCH_PAUSED.get(), !paused);
    }

    @Override
    public void onLeftClickUp(@NonNull ServerPlayer player, ItemStack stack) {
        setPressed(stack, false);
        player.level().playSound(null, player.blockPosition(), ModSounds.TALLY_FORWARD.value(), player.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public void onRightClickDown(ServerPlayer player, @NonNull ItemStack stack) {
        setPressed(stack, true);
        TallyCounterItem.tick(player);
        long now = now(player);
        stack.set(ModComponents.STOPWATCH_PAUSED.get(), true);
        stack.set(ModComponents.STOPWATCH_PAUSE_TIME.get(), now);
        stack.set(ModComponents.STOPWATCH_START_TIME.get(), now);
    }

    @Override
    public void onRightClickUp(@NonNull ServerPlayer player, ItemStack stack) {
        setPressed(stack, false);
        player.level().playSound(null, player.blockPosition(), ModSounds.TALLY_BACKWARD.value(), player.getSoundSource(), 1.0F, 1.0F);

        long now = now(player);
        stack.set(ModComponents.STOPWATCH_START_TIME.get(), now);
        stack.set(ModComponents.STOPWATCH_PAUSED.get(), false);
    }

    @Override
    public void inventoryTick(@NonNull ItemStack stack, @NonNull ServerLevel level, @NonNull Entity owner, @Nullable EquipmentSlot slot) {
        if (!(owner instanceof ServerPlayer player)) return;

        boolean paused = stack.getOrDefault(ModComponents.STOPWATCH_PAUSED.get(), true);
        long start = stack.getOrDefault(ModComponents.STOPWATCH_START_TIME.get(), 0L);
        long end = paused ? stack.getOrDefault(ModComponents.STOPWATCH_PAUSE_TIME.get(), now(player)) : now(player);
        long elapsed = end - start;

        boolean mainHand = player.getItemInHand(InteractionHand.MAIN_HAND).equals(stack);
        if (mainHand && start != -1) {
            float elapsedSeconds = (float) elapsed / 20f;
            long minutes = (long) (elapsedSeconds / 60);
            long seconds = (long) (elapsedSeconds % 60);
            long millis = (long) ((elapsedSeconds * 100) % 100);

            MutableComponent builder = Component.literal("[ ").withColor(0xE4D690);
            builder.append(Component.literal(String.format("%02d", minutes)).withStyle(ChatFormatting.WHITE));
            builder.append(Component.literal(":").withColor(0x8A8E92));
            builder.append(Component.literal(String.format("%02d", seconds)).withStyle(ChatFormatting.WHITE));
            builder.append(Component.literal(".").withColor(0x8A8E92));
            builder.append(Component.literal(String.format("%02d", millis)).withStyle(ChatFormatting.WHITE));
            builder.append(Component.literal(" ]").withColor(0xE4D690));
            player.sendSystemMessage(builder, true);
        }

        if (!paused) {
            tick(player, elapsed, mainHand ? 0.6f : 0.2f);
            incrementAnim(stack);
        }
    }

    @Override
    public boolean onEntitySwing(@NonNull ItemStack stack, @NonNull LivingEntity entity, @NonNull InteractionHand hand) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

}
