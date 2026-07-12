package cozmicweb.pda.common.network;

import cozmicweb.pda.common.PDACommon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record ClickStatePayload(boolean isAttack, boolean isPressed) implements CustomPacketPayload {
    public static final Type<ClickStatePayload> TYPE = new Type<>(PDACommon.id("click_state"));

    public static final StreamCodec<ByteBuf, ClickStatePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ClickStatePayload::isAttack,
            ByteBufCodecs.BOOL, ClickStatePayload::isPressed,
            ClickStatePayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
