package cozmicweb.pda.common.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TallyAnimState(int targetTotal, boolean forward, int ticksUntilNext) {
    public static final Codec<TallyAnimState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("target").forGetter(TallyAnimState::targetTotal),
            Codec.BOOL.fieldOf("forward").forGetter(TallyAnimState::forward),
            Codec.INT.fieldOf("ticks").forGetter(TallyAnimState::ticksUntilNext)
    ).apply(inst, TallyAnimState::new));

    public static final StreamCodec<ByteBuf, TallyAnimState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, TallyAnimState::targetTotal,
            ByteBufCodecs.BOOL, TallyAnimState::forward,
            ByteBufCodecs.VAR_INT, TallyAnimState::ticksUntilNext,
            TallyAnimState::new
    );
}
