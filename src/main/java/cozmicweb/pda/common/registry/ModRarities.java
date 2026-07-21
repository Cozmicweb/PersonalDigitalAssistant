package cozmicweb.pda.common.registry;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModRarities {
    public static final EnumProxy<Rarity> GRAY = new EnumProxy<>(Rarity.class, -1, "pda:gray", (UnaryOperator<Style>) s -> s.withColor(TextColor.GRAY));
    public static final EnumProxy<Rarity> WHITE = new EnumProxy<>(Rarity.class, -1, "pda:white", (UnaryOperator<Style>) s -> s.withColor(TextColor.WHITE));
    public static final EnumProxy<Rarity> BLUE = new EnumProxy<>(Rarity.class, -1, "pda:blue", (UnaryOperator<Style>) s -> s.withColor(0x8181DC));
    public static final EnumProxy<Rarity> GREEN = new EnumProxy<>(Rarity.class, -1, "pda:green", (UnaryOperator<Style>) s -> s.withColor(0x92F892));
    public static final EnumProxy<Rarity> ORANGE = new EnumProxy<>(Rarity.class, -1, "pda:orange", (UnaryOperator<Style>) s -> s.withColor(0xE9B688));
    public static final EnumProxy<Rarity> LIGHT_RED = new EnumProxy<>(Rarity.class, -1, "pda:light_red", (UnaryOperator<Style>) s -> s.withColor(0xCE7A7A));
    public static final EnumProxy<Rarity> PINK = new EnumProxy<>(Rarity.class, -1, "pda:pink", (UnaryOperator<Style>) s -> s.withColor(0xF590F5));
    public static final EnumProxy<Rarity> LIGHT_PURPLE = new EnumProxy<>(Rarity.class, -1, "pda:light_purple", (UnaryOperator<Style>) s -> s.withColor(0xBE90E5));
    public static final EnumProxy<Rarity> LIME = new EnumProxy<>(Rarity.class, -1, "pda:lime", (UnaryOperator<Style>) s -> s.withColor(0x7BD30A));
    public static final EnumProxy<Rarity> YELLOW = new EnumProxy<>(Rarity.class, -1, "pda:yellow", (UnaryOperator<Style>) s -> s.withColor(0xF9F909));
    public static final EnumProxy<Rarity> CYAN = new EnumProxy<>(Rarity.class, -1, "pda:cyan", (UnaryOperator<Style>) s -> s.withColor(0x04C3F9));
    public static final EnumProxy<Rarity> RED = new EnumProxy<>(Rarity.class, -1, "pda:red", (UnaryOperator<Style>) s -> s.withColor(0xB50638));
    public static final EnumProxy<Rarity> PURPLE = new EnumProxy<>(Rarity.class, -1, "pda:purple", (UnaryOperator<Style>) s -> s.withColor(0xB227FD));

    public static final List<EnumProxy<Rarity>> ALL_RARITIES = List.of(GRAY, WHITE, BLUE, GREEN, ORANGE, LIGHT_RED, PINK, LIGHT_PURPLE, LIME, YELLOW, CYAN, RED, PURPLE);
}