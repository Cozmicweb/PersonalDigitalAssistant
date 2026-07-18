package cozmicweb.pda.common.content.raider_army.shop;

import com.google.common.collect.ImmutableList;
import cozmicweb.pda.common.content.raider_army.ArmyDifficulty;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record WaveReceipt(ArmyDifficulty difficulty, int wave, float budget, List<ReceiptEntry> entries) {

    public int totalSpawned() {
        return entries.stream().mapToInt(ReceiptEntry::count).sum();
    }

    public float totalSpent() {
        float sum = 0;
        for (ReceiptEntry entry : entries)
            sum += entry.total();
        return sum;
    }

    public @NonNull @Unmodifiable List<Component> getReceipt() {
        List<Component> receipt = new ArrayList<>();

        receipt.add(divider());

        MutableComponent title = Component.empty();
        title.append(Component.translatable(difficulty.translation).withStyle(Style.EMPTY.withBold(true).withColor(TextColor.RED)));
        title.append(Component.literal("  -  Wave " + wave + "/" + difficulty.maxWave).withStyle(Style.EMPTY.withColor(TextColor.LIGHT_PURPLE)));
        receipt.add(title);

        MutableComponent budgetLine = Component.empty();
        budgetLine.append(Component.literal("Budget: ").withStyle(Style.EMPTY.withColor(TextColor.GRAY)));
        budgetLine.append(Component.literal("$" + ReceiptEntry.formatMoney(budget)).withStyle(Style.EMPTY.withBold(true).withColor(TextColor.GOLD)));
        receipt.add(budgetLine);

        receipt.add(Component.empty());

        if (entries.isEmpty()) {
            receipt.add(Component.literal("  (no raiders purchased)").withStyle(Style.EMPTY.withItalic(true).withColor(TextColor.GRAY)));
        } else {
            for (ReceiptEntry entry : entries)
                receipt.addAll(entry.getEntry());
        }

        receipt.add(Component.empty());

        MutableComponent totals = Component.empty();
        totals.append(Component.literal("Total: ").withStyle(Style.EMPTY.withBold(true).withColor(TextColor.WHITE)));
        totals.append(Component.literal(totalSpawned() + " raiders").withStyle(Style.EMPTY.withColor(TextColor.YELLOW)));
        totals.append(Component.literal("   spent   ").withStyle(Style.EMPTY.withColor(TextColor.DARK_GRAY)));
        totals.append(Component.literal("$" + ReceiptEntry.formatMoney(totalSpent())).withStyle(Style.EMPTY.withBold(true).withColor(TextColor.GOLD)));
        receipt.add(totals);

        receipt.add(divider());

        return ImmutableList.copyOf(receipt);
    }

    private static @NonNull Component divider() {
        return Component.literal(" ".repeat(58)).withStyle(Style.EMPTY.withStrikethrough(true).withColor(TextColor.DARK_GRAY));
    }

}