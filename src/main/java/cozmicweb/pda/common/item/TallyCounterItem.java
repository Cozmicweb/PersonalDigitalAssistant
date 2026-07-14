package cozmicweb.pda.common.item;

import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.attachments.TallyAnimState;
import cozmicweb.pda.common.registry.ModComponents;
import cozmicweb.pda.common.registry.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TallyCounterItem extends Item implements IClickReactive {

    public static final int CASCADE_DELAY_TICKS = 3;
    public static final int IDLE_REFRESH_TICKS = 10;

    public static int getMaxPlaces() {
        return PDAConfig.TALLY_COUNTER_LIMIT.get();
    }

    public static int getMaxTally() {
        return (int) Math.pow(10, getMaxPlaces()) - 1;
    }

    public static int[] getPow10() {
        int maxPlaces = getMaxPlaces();
        int[] arr = new int[maxPlaces];
        for (int i = 0; i < maxPlaces; i++) {
            arr[i] = (int) Math.pow(10, maxPlaces - 1 - i);
        }
        return arr;
    }

    public TallyCounterItem(@NonNull Properties properties) {
        super(properties
                .component(ModComponents.TALLY_COUNT.get(), 0)
                .component(ModComponents.TALLY_DISPLAY.get(), 0));
    }

    static void setPressed(@NonNull ItemStack stack, boolean state) {
        stack.set(ModComponents.STOPWATCH_PRESSED.get(), state);
    }

    @Contract(pure = true)
    private static int @NonNull [] digits(int total) {
        int maxPlaces = getMaxPlaces();
        int[] d = new int[maxPlaces];
        int remaining = total;
        for (int i = maxPlaces - 1; i >= 0; i--) {
            d[i] = remaining % 10;
            remaining /= 10;
        }
        return d;
    }

    private static @NonNull Component format(int total) {
        MutableComponent builder = Component.literal("[ ").withColor(0xE4D690);
        int[] d = digits(total);
        for (int i = 0; i < d.length; i++) {
            if (i > 0) builder.append(Component.literal(" : ").withColor(0x8A8E92));
            builder.append(Component.literal(Integer.toString(d[i])).withColor(TextColor.WHITE));
        }
        return builder.append(Component.literal(" ]").withColor(0xE4D690));
    }

    private static void sendDisplay(@NonNull ServerPlayer player, int total) {
        player.sendSystemMessage(format(total), true);
    }

    public static void tick(@NonNull ServerPlayer player) {
        player.level().playSound(null, player.blockPosition(), ModSounds.TALLY_TICK.value(), player.getSoundSource(), 0.5F, 1.0F);
    }

    @Override
    public void onLeftClickDown(ServerPlayer player, ItemStack stack) {
        setPressed(stack, true);
        nudgeOnes(player, stack, true);
    }

    @Override
    public void onRightClickDown(ServerPlayer player, ItemStack stack) {
        setPressed(stack, true);
        nudgeOnes(player, stack, false);
    }

    @Override
    public void onLeftClickUp(ServerPlayer player, ItemStack stack) {
        setPressed(stack, false);
        commit(player, stack, true);
        player.level().playSound(null, player.blockPosition(), ModSounds.TALLY_FORWARD.value(), player.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public void onRightClickUp(ServerPlayer player, ItemStack stack) {
        setPressed(stack, false);
        commit(player, stack, false);
        player.level().playSound(null, player.blockPosition(), ModSounds.TALLY_BACKWARD.value(), player.getSoundSource(), 1.0F, 1.0F);
    }

    private static void nudgeOnes(ServerPlayer player, @NonNull ItemStack stack, boolean forward) {
        TallyAnimState anim = stack.get(ModComponents.TALLY_ANIM);
        if (anim != null) {
            stack.set(ModComponents.TALLY_DISPLAY, anim.targetTotal());
            stack.remove(ModComponents.TALLY_ANIM);
        }

        int displayed = stack.getOrDefault(ModComponents.TALLY_DISPLAY, 0);
        int ones = displayed % 10;
        int newOnes = Math.floorMod(ones + (forward ? 1 : -1), 10);
        int distorted = (displayed - ones) + newOnes;

        stack.set(ModComponents.TALLY_DISPLAY, distorted);
        tick(player);
        sendDisplay(player, distorted);
    }

    private static void commit(ServerPlayer player, @NonNull ItemStack stack, boolean forward) {
        int current = stack.getOrDefault(ModComponents.TALLY_COUNT, 0);
        int newTotal = Math.floorMod(current + (forward ? 1 : -1), getMaxTally() + 1);
        stack.set(ModComponents.TALLY_COUNT, newTotal);

        int[] have = digits(stack.getOrDefault(ModComponents.TALLY_DISPLAY, 0));
        int[] want = digits(newTotal);

        int maxPlaces = getMaxPlaces();
        boolean higherPlacesMatch = true;
        for (int i = 0; i < maxPlaces - 1; i++) {
            if (have[i] != want[i]) { higherPlacesMatch = false; break; }
        }

        if (higherPlacesMatch) {
            stack.set(ModComponents.TALLY_DISPLAY, newTotal);
            return;
        }

        stack.set(ModComponents.TALLY_ANIM, new TallyAnimState(newTotal, forward, CASCADE_DELAY_TICKS));
    }

    @Override
    public void inventoryTick(ItemStack stack, @NonNull ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (level.isClientSide() || !(owner instanceof ServerPlayer player)) return;

        TallyAnimState anim = stack.get(ModComponents.TALLY_ANIM);

        if (anim == null) {
            if (level.getGameTime() % IDLE_REFRESH_TICKS == 0 && player.getItemInHand(InteractionHand.MAIN_HAND).equals(stack)) {
                sendDisplay(player, stack.getOrDefault(ModComponents.TALLY_DISPLAY, 0));
            }
            return;
        }

        if (anim.ticksUntilNext() > 0) {
            stack.set(ModComponents.TALLY_ANIM, new TallyAnimState(anim.targetTotal(), anim.forward(), anim.ticksUntilNext() - 1));
            return;
        }

        int displayed = stack.getOrDefault(ModComponents.TALLY_DISPLAY, 0);
        int[] have = digits(displayed);
        int[] want = digits(anim.targetTotal());

        int maxPlaces = getMaxPlaces();
        int idx = -1;
        for (int i = maxPlaces - 1; i >= 0; i--) {
            if (have[i] != want[i]) { idx = i; break; }
        }

        if (idx == -1) {
            stack.remove(ModComponents.TALLY_ANIM);
            return;
        }

        int digit = have[idx];
        int newDigit = Math.floorMod(digit + (anim.forward() ? 1 : -1), 10);
        int newDisplayed = displayed + (newDigit - digit) * getPow10()[idx];

        stack.set(ModComponents.TALLY_DISPLAY, newDisplayed);
        tick(player);
        sendDisplay(player, newDisplayed);

        if (java.util.Arrays.equals(digits(newDisplayed), want)) {
            stack.remove(ModComponents.TALLY_ANIM);
        } else {
            stack.set(ModComponents.TALLY_ANIM, new TallyAnimState(anim.targetTotal(), anim.forward(), CASCADE_DELAY_TICKS));
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return true;
    }
}