package cozmicweb.pda.common.content.reforge_modifier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.content.reforge_modifier.tracking.ReforgeDefinition;
import cozmicweb.pda.common.content.reforge_modifier.tracking.ReforgePool;
import cozmicweb.pda.common.content.reforge_modifier.tracking.ResolvedReforge;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.StreamSupport;

@EventBusSubscriber
public class ReforgeModifierRegistry {

    private static ImmutableList<ReforgePool> pools = ImmutableList.of();
    private static ImmutableSet<String> allReforgeIds = ImmutableSet.of();

    private static final Map<Item, List<ReforgePool>> itemPoolCache = new IdentityHashMap<>();
    private static final Map<Holder<Attribute>, Double> attributeMultipliers = new HashMap<>();

    private ReforgeModifierRegistry() {}

    public static void load(@NonNull Map<Identifier, JsonObject> files) {
        List<Map.Entry<Identifier, JsonObject>> sortedFiles = new ArrayList<>(files.entrySet());
        sortedFiles.sort(Map.Entry.comparingByKey());

        ImmutableList.Builder<ReforgePool> poolsBuilder = ImmutableList.builder();
        ImmutableSet.Builder<String> idsBuilder = ImmutableSet.builder();

        for (Map.Entry<Identifier, JsonObject> fileEntry : sortedFiles) {
            JsonObject fileObj = fileEntry.getValue();

            List<TagKey<Item>> itemTags = parseItems(fileObj.get("items"));
            JsonObject reforgesObj = fileObj.getAsJsonObject("reforges");

            ImmutableMap.Builder<String, ReforgeDefinition> defsBuilder = ImmutableMap.builder();

            for (Map.Entry<String, JsonElement> modEntry : reforgesObj.entrySet()) {
                String reforgeId = normalizeId(modEntry.getKey());
                JsonObject reforgeObj = modEntry.getValue().getAsJsonObject();

                int tier = reforgeObj.has("tier") ? reforgeObj.get("tier").getAsInt() : 0;

                ImmutableMap.Builder<Holder<Attribute>, Double> attrsBuilder = ImmutableMap.builder();
                JsonObject attributesObj = reforgeObj.getAsJsonObject("attributes");

                for (Map.Entry<String, JsonElement> attrEntry : attributesObj.entrySet()) {
                    JsonElement value = attrEntry.getValue();
                    if (value.isJsonNull()) continue;

                    BuiltInRegistries.ATTRIBUTE.getOptional(Identifier.tryParse(attrEntry.getKey())).ifPresent(
                            attr -> attrsBuilder.put(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attr), value.getAsDouble()));
                }

                defsBuilder.put(reforgeId, new ReforgeDefinition(tier, attrsBuilder.build()));
                idsBuilder.add(reforgeId);
            }

            poolsBuilder.add(new ReforgePool(itemTags, defsBuilder.build()));
        }

        pools = poolsBuilder.build();
        allReforgeIds = idsBuilder.build();
        itemPoolCache.clear();
    }

    private static @NonNull String normalizeId(@NonNull String raw) {
        int colon = raw.indexOf(':');
        return (colon >= 0 ? raw.substring(colon + 1) : raw).toLowerCase(Locale.ROOT);
    }

    private static @Unmodifiable List<TagKey<Item>> parseItems(@Nullable JsonElement el) {
        if (el == null || el.isJsonNull()) return List.of();
        return StreamSupport.stream(el.getAsJsonArray().spliterator(), false)
                .map(JsonElement::getAsString)
                .map(str -> str.startsWith("#") ? str.substring(1) : str)
                .map(Identifier::parse)
                .map(id -> TagKey.create(Registries.ITEM, id))
                .toList();
    }

    private static List<ReforgePool> getPools(@NonNull Item item) {
        return itemPoolCache.computeIfAbsent(item, i -> {
            Holder<Item> holder = BuiltInRegistries.ITEM.wrapAsHolder(i);
            return pools.stream()
                    .filter(pool -> pool.items().stream().anyMatch(holder::is))
                    .toList();
        });
    }

    public static boolean canSupport(@NonNull ItemStack stack) {
        return !stack.isEmpty() && !getPools(stack.getItem()).isEmpty();
    }

    public static @Unmodifiable Set<String> getAllReforgeIds() {
        return allReforgeIds;
    }

    public static @Unmodifiable @NonNull Set<String> getValidReforgeIds(@NonNull ItemStack stack) {
        if (stack.isEmpty()) return Set.of();

        Set<String> ids = new LinkedHashSet<>();
        for (ReforgePool pool : getPools(stack.getItem())) ids.addAll(pool.reforges().keySet());
        return ids;
    }

    public static @Nullable ResolvedReforge resolve(@NonNull ItemStack stack, @NonNull String reforgeId) {
        if (stack.isEmpty()) return null;

        List<ReforgePool> applicable = getPools(stack.getItem());
        if (applicable.isEmpty()) return null;

        boolean found = false;
        int tier = 0;
        Map<Holder<Attribute>, Double> merged = new LinkedHashMap<>();

        for (ReforgePool pool : applicable) {
            ReforgeDefinition def = pool.reforges().get(reforgeId);
            if (def == null) continue;

            if (!found) tier = def.tier();
            found = true;

            for (Map.Entry<Holder<Attribute>, Double> attr : def.attributes().entrySet())
                merged.putIfAbsent(attr.getKey(), attr.getValue());
        }

        return found ? new ResolvedReforge(reforgeId, tier, merged) : null;
    }

    public static Map<Holder<Attribute>, Double> getAttributeMultipliers() {
        return attributeMultipliers;
    }

    public static boolean reforgeModifierMultipliersConfigValidator(Object obj) {
        if (!(obj instanceof String string)) return false;
        string = string.replace(" ", "").strip();

        String[] segments = string.split("[:=]");
        if (segments.length != 3) return false;

        String namespace = segments[0];
        String path = segments[1];
        String valueString = segments[2];
        if (namespace.isEmpty() || path.isEmpty() || valueString.isEmpty()) return false;

        return tryParseDouble(valueString).isPresent();
    }

    private static Optional<Double> tryParseDouble(String string) {
        try {
            return Optional.of(Double.parseDouble(string));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SubscribeEvent
    public static void parseMultiplierConfig(ModConfigEvent.Reloading event) {
        attributeMultipliers.clear();

        for (String string : PDAConfig.REFORGE_MODIFIER_MULTIPLIERS.get()) {
            string = string.replace(" ", "").strip();

            String[] segments = string.split("[:=]");
            if (segments.length != 3) continue;

            String namespace = segments[0];
            String path = segments[1];
            String valueString = segments[2];
            if (namespace.isEmpty() || path.isEmpty() || valueString.isEmpty()) continue;

            Identifier id = Identifier.tryParse(namespace + ":" + path);
            if (id == null) continue;

            Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(id).map(Holder.Reference::value).orElse(null);
            if (attribute == null) continue;

            double value = tryParseDouble(valueString).orElse(1.0);
            attributeMultipliers.putIfAbsent(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute), value);
        }
    }

}
