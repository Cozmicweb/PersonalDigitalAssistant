package cozmicweb.pda.common.network;

import cozmicweb.pda.common.PDACommon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record ServerDataRequestPayload(List<Request> requests) implements CustomPacketPayload {

    public static final Type<ServerDataRequestPayload> TYPE = new Type<>(PDACommon.id("server_data_request"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerDataRequestPayload> STREAM_CODEC = StreamCodec.composite(
            Request.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ServerDataRequestPayload::requests,
            ServerDataRequestPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record Request(Identifier handlerId, List<Object> params) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Request> STREAM_CODEC = StreamCodec.of(
                (buf, val) -> {
                    ByteBufCodecs.fromCodecWithRegistries(Identifier.CODEC).encode(buf, val.handlerId());
                    buf.writeVarInt(val.params().size());
                    for (Object obj : val.params()) {
                        writeDynamic(buf, obj);
                    }
                },
                buf -> {
                    Identifier id = ByteBufCodecs.fromCodecWithRegistries(Identifier.CODEC).decode(buf);
                    int size = buf.readVarInt();
                    List<Object> params = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        params.add(readDynamic(buf));
                    }
                    return new Request(id, params);
                }
        );

        private static void writeDynamic(RegistryFriendlyByteBuf buf, Object obj) {
            switch (obj) {
                case Integer i -> {
                    buf.writeByte(0);
                    buf.writeVarInt(i);
                }
                case String s -> {
                    buf.writeByte(1);
                    buf.writeUtf(s);
                }
                case Boolean b -> {
                    buf.writeByte(2);
                    buf.writeBoolean(b);
                }
                case Identifier id -> {
                    buf.writeByte(3);
                    ByteBufCodecs.fromCodecWithRegistries(Identifier.CODEC).encode(buf, id);
                }
                case null, default -> buf.writeByte(-1);
            }
        }

        private static Object readDynamic(RegistryFriendlyByteBuf buf) {
            byte type = buf.readByte();
            return switch (type) {
                case 0 -> buf.readVarInt();
                case 1 -> buf.readUtf();
                case 2 -> buf.readBoolean();
                case 3 -> ByteBufCodecs.fromCodecWithRegistries(Identifier.CODEC).decode(buf);
                default -> null;
            };
        }
    }
}
