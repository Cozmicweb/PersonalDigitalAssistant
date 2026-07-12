package cozmicweb.pda.common.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

import java.util.List;

public record RareMobData(List<RareMobEntry> entries) {
    public static final Codec<RareMobData> CODEC = Codec.either(RareMobEntry.CODEC.listOf(), RareMobEntry.CODEC).xmap(
            either -> either.map(RareMobData::new, entry -> new RareMobData(List.of(entry))),
            data -> data.entries().size() == 1 ? Either.right(data.entries().getFirst()) : Either.left(data.entries())
    );
}
