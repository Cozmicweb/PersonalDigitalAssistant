package cozmicweb.pda.common.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.registry.ModDataMaps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueOutput;

import java.util.Optional;

public record RareMobEntry(String id, Optional<CompoundTag> nbtMatch, Optional<String> translationKey) {
    public static final Codec<RareMobEntry> CODEC = Codec.either(
            Codec.STRING,
            RecordCodecBuilder.<RareMobEntry>create(inst -> inst.group(
                    Codec.STRING.fieldOf("id").forGetter(RareMobEntry::id),
                    CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(RareMobEntry::nbtMatch),
                    Codec.STRING.optionalFieldOf("translation_key").forGetter(RareMobEntry::translationKey)
            ).apply(inst, RareMobEntry::new))
    ).xmap(
            either -> either.map(s -> new RareMobEntry(s, Optional.empty(), Optional.empty()), e -> e),
            entry -> (entry.nbtMatch().isEmpty() && entry.translationKey().isEmpty()) ? Either.left(entry.id()) : Either.right(entry)
    );

    public boolean matches(Entity entity) {
        if (nbtMatch.isEmpty()) return true;

        try (ProblemReporter.ScopedCollector problems = new ProblemReporter.ScopedCollector(entity.problemPath(), PDACommon.LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(problems, entity.registryAccess());
            entity.saveWithoutId(output);
            CompoundTag actual = output.buildResult();

            return compareNbtLenient(nbtMatch.get(), actual, true);
        }
    }

    private static boolean compareNbtLenient(Tag expected, Tag actual, boolean compareAll) {
        if (expected == actual) return true;
        if (expected == null || actual == null) return false;
        
        if (isNumeric(expected) && isNumeric(actual)) {
            try {
                String s1 = expected.toString().replaceAll("[a-zA-Z]$", "");
                String s2 = actual.toString().replaceAll("[a-zA-Z]$", "");
                return Double.parseDouble(s1) == Double.parseDouble(s2);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (expected.getType() != actual.getType()) return false;

        if (expected instanceof CompoundTag compoundExpected && actual instanceof CompoundTag compoundActual) {
            for (String key : compoundExpected.keySet()) {
                Tag tagExpected = compoundExpected.get(key);
                Tag tagActual = compoundActual.get(key);
                if (!compareNbtLenient(tagExpected, tagActual, compareAll)) {
                    return false;
                }
            }
            return true;
        }

        return expected.equals(actual);
    }

    private static boolean isNumeric(Tag tag) {
        byte type = tag.getId();
        return type >= 1 && type <= 6; // 1: Byte, 2: Short, 3: Int, 4: Long, 5: Float, 6: Double
    }

    public Component getName(Entity entity) {
        return translationKey.<Component>map(Component::translatable).orElseGet(entity::getDisplayName);
    }

    public static Optional<RareMobEntry> getRareMobEntry(Entity entity) {
        RareMobData data = entity.typeHolder().getData(ModDataMaps.RARE_MOBS);
        if (data == null) return Optional.empty();
        return data.entries().stream()
                .filter(e -> e.matches(entity))
                .findFirst();
    }

    public static Optional<String> getRareMobId(Entity entity) {
        return getRareMobEntry(entity).map(RareMobEntry::id);
    }

}