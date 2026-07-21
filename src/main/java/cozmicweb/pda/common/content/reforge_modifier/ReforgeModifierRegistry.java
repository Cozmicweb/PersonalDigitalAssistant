package cozmicweb.pda.common.content.reforge_modifier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cozmicweb.pda.common.PDAConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
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

    private static ImmutableMap<Identifier, ReforgeGroup> groups = ImmutableMap.of();
    private static ImmutableTable<Identifier, Identifier, ItemReforge> table = ImmutableTable.of(); // groupId, modifierId -> ItemReforge
    private static ImmutableSet<ItemReforge> allReforges = ImmutableSet.of();

    private static final Map<Item, Optional<Identifier>> itemGroupCache = new IdentityHashMap<>();
    private static final Map<Holder<Attribute>, Double> attributeMultipliers = new HashMap<>();

    private ReforgeModifierRegistry() {}

    public static @NonNull ReforgeGroup expectGroup(Identifier groupId) {
        ReforgeGroup group = groups.get(groupId);
        if (group == null) throw new IllegalStateException("Reforge groupId " + groupId + " does not exist");
        return group;
    }

    public static @Nullable ReforgeGroup getGroup(Identifier groupId) {
        return groups.get(groupId);
    }

    public static @Nullable ItemReforge getReforge(Identifier groupId, Identifier reforgeId) {
        return table.get(groupId, reforgeId);
    }

    public static Optional<Identifier> getGroupId(Item item) {
        return itemGroupCache.computeIfAbsent(item, i -> {
            for (ReforgeGroup group : groups.values())
                for (TagKey<Item> tag : group.items())
                    if (BuiltInRegistries.ITEM.wrapAsHolder(i).is(tag))
                        return Optional.of(group.id());
            return Optional.empty();
        });
    }

    public static void load(@NonNull Map<Identifier, JsonObject> groupJsons) {
        ImmutableMap.Builder<Identifier, ReforgeGroup> groupsBuilder = ImmutableMap.builder();
        ImmutableTable.Builder<Identifier, Identifier, ItemReforge> tableBuilder = ImmutableTable.builder();

        for (Map.Entry<Identifier, JsonObject> entry : groupJsons.entrySet()) {
            Identifier groupId = entry.getKey();
            JsonObject groupObj = entry.getValue();

            JsonObject modifiersObj = groupObj.getAsJsonObject("reforges");
            List<TagKey<Item>> itemTags = parseItems(groupObj.get("items"));

            ImmutableList.Builder<ItemReforge> reforgesBuilder = ImmutableList.builder();

            for (Map.Entry<String, JsonElement> modEntry : modifiersObj.entrySet()) {
                Identifier reforgeId = Identifier.parse(modEntry.getKey());
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

                ItemReforge reforge = new ItemReforge(reforgeId, tier, groupId, attrsBuilder.build());
                reforgesBuilder.add(reforge);
                tableBuilder.put(groupId, reforgeId, reforge);
            }

            ReforgeGroup group = new ReforgeGroup(groupId, itemTags, reforgesBuilder.build());
            groupsBuilder.put(groupId, group);
        }

        groups = groupsBuilder.build();
        table = tableBuilder.build();
        allReforges = ImmutableSet.copyOf(table.values());
        itemGroupCache.clear();
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

    public static @Unmodifiable Set<ItemReforge> getAllReforges() {
        return allReforges;
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