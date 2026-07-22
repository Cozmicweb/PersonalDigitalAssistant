package cozmicweb.pda.common.content.reforge_modifier;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.content.reforge_modifier.tracking.ResolvedReforge;
import cozmicweb.pda.common.registry.ModComponents;
import cozmicweb.pda.common.registry.ModRarities;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@EventBusSubscriber
public class ReforgeManager {
    public static final String ATTRIBUTE_MODIFIER_PREFIX = "pda_reforge_";

    private static final Predicate<ItemAttributeModifiers.Entry> IS_REFORGE_MODIFIER = e -> e.modifier().id().getPath().startsWith(ATTRIBUTE_MODIFIER_PREFIX);
    public static final List<Rarity> DEFAULT_RARITIES = List.of(Rarity.COMMON, Rarity.UNCOMMON, Rarity.RARE, Rarity.EPIC);

    private ReforgeManager() {}

    public static void removeReforge(@NotNull ItemStack stack) {
        ItemAttributeModifiers attributes = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);

        if (attributes != null) {
            List<ItemAttributeModifiers.Entry> remainingModifiers = attributes.modifiers().stream()
                    .filter(IS_REFORGE_MODIFIER.negate())
                    .toList();

            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(remainingModifiers));
        }

        if (stack.has(ModComponents.ORIGINAL_NAME)) {
            stack.set(DataComponents.ITEM_NAME, stack.get(ModComponents.ORIGINAL_NAME));
        }

        stack.remove(ModComponents.ORIGINAL_NAME);
        stack.remove(ModComponents.REFORGE_MODIFIER);
        stack.set(DataComponents.RARITY, stack.getItem().getDefaultInstance().getRarity());
    }

    public static boolean addReforge(@NotNull ItemStack stack, @NonNull String reforgeId) {
        if (stack.isEmpty()) return false;

        ResolvedReforge reforge = ReforgeModifierRegistry.resolve(stack, reforgeId);
        if (reforge == null) return false;

        applyReforge(stack, reforge);
        return true;
    }

    private static void applyReforge(ItemStack stack, @NonNull ResolvedReforge reforge) {
        removeReforge(stack);

        Map<Holder<Attribute>, Double> multipliers = ReforgeModifierRegistry.getAttributeMultipliers();
        ItemAttributeModifiers attributes = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        for (Map.Entry<Holder<Attribute>, Double> entry : reforge.attributes().entrySet()) {
            Holder<Attribute> attribute = entry.getKey();
            double finalValue = entry.getValue() * multipliers.getOrDefault(attribute, PDAConfig.REFORGE_MODIFIER_GLOBAL_MULTIPLIER.get());
            AttributeModifier modifier = new AttributeModifier(attributeModifierId(reforge.id()), finalValue, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            attributes = attributes.withModifierAdded(attribute, modifier, EquipmentSlotGroup.MAINHAND);
        }

        Component name = Component.translatable(translationKey(reforge.id())).append(" ").append(stack.getItemName());

        stack.set(DataComponents.RARITY, getReforgedRarity(stack, reforge.tier()));
        stack.set(ModComponents.ORIGINAL_NAME, stack.getItemName());
        stack.set(ModComponents.REFORGE_MODIFIER, reforge.id());
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
        stack.set(DataComponents.ITEM_NAME, name);
    }

    public static void addRandomReforge(ItemStack stack, RandomSource random) {
        List<String> validIds = List.copyOf(ReforgeModifierRegistry.getValidReforgeIds(stack));
        if (validIds.isEmpty()) return;

        addReforge(stack, validIds.get(random.nextInt(validIds.size())));
    }

    public static boolean removeReforge(@NotNull ItemStack stack, @NonNull String reforgeId) {
        String currentReforgeId = getReforgeId(stack);

        if (reforgeId.equals(currentReforgeId)) {
            removeReforge(stack);
            return true;
        }

        return false;
    }

    public static @Nullable ResolvedReforge getReforge(@NotNull ItemStack stack) {
        String reforgeId = getReforgeId(stack);
        if (reforgeId == null) return null;

        return ReforgeModifierRegistry.resolve(stack, reforgeId);
    }

    public static @Nullable String getReforgeId(@NonNull ItemStack stack) {
        return stack.get(ModComponents.REFORGE_MODIFIER);
    }

    @Contract("_ -> new")
    private static @NonNull Identifier attributeModifierId(@NonNull String reforgeId) {
        return PDACommon.id(ATTRIBUTE_MODIFIER_PREFIX + reforgeId);
    }

    @Contract(pure = true)
    private static @NonNull String translationKey(@NonNull String reforgeId) {
        return "reforge." + PDACommon.MOD_ID + "." + reforgeId;
    }

    public static Rarity getReforgedRarity(@NonNull ItemStack stack, int tier) {
        int ordinal = DEFAULT_RARITIES.indexOf(stack.getRarity());
        int index = Math.clamp(ordinal * 3L + tier, 0, 12);
        return ModRarities.ALL_RARITIES.get(index).getValue();
    }

    @SubscribeEvent
    public static void onPlayerItemCrafted(PlayerEvent.@NonNull ItemCraftedEvent event) {
        if (event.getEntity().level().isClientSide())
            return;

        RandomSource random = event.getEntity().getRandom();
        if (random.nextDouble() < PDAConfig.REFORGE_RANDOM_CHANCE.get())
            addRandomReforge(event.getCrafting(), random);
    }

}
