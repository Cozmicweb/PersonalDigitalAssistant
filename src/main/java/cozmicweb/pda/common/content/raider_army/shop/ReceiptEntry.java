package cozmicweb.pda.common.content.raider_army.shop;

import cozmicweb.pda.common.content.raider_army.raiders.RaiderGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record ReceiptEntry(RaiderGroup<?> group, int count) {

    public float total() {
        return group.price() * count;
    }

    public boolean withinBounds() {
        return count >= group.min() && count <= group.max();
    }

    public @NonNull @Unmodifiable List<Component> getEntry() {
        TextColor boundsColor = withinBounds() ? TextColor.DARK_GRAY : TextColor.RED;

        MutableComponent line = Component.empty();
        line.append(Component.literal("  » ").withStyle(Style.EMPTY.withColor(TextColor.DARK_GRAY)));
        line.append(Component.translatable(group.translation()).withStyle(Style.EMPTY.withBold(true).withColor(TextColor.AQUA)));
        line.append(Component.literal("  $" + group.price()).withStyle(Style.EMPTY.withColor(TextColor.WHITE)));
        line.append(Component.literal(" x " + count).withStyle(Style.EMPTY.withBold(true).withColor(TextColor.YELLOW)));
        line.append(Component.literal(" = $" + formatMoney(total())).withStyle(Style.EMPTY.withColor(TextColor.GOLD)));
        line.append(Component.literal("  [%s-%s]".formatted(group.min(), group.max())).withStyle(Style.EMPTY.withColor(boundsColor)));

        return List.of(line);
    }

    public static @NonNull String formatMoney(float value) {
        return value == (long) value ? String.valueOf((long) value) : String.valueOf(value);
    }

}