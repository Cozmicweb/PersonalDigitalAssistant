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

public record ServerDataResponsePayload(List<Response> responses) implements CustomPacketPayload {

    public static final Type<ServerDataResponsePayload> TYPE = new Type<>(PDACommon.id("server_data_response"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerDataResponsePayload> STREAM_CODEC = StreamCodec.composite(
            Response.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ServerDataResponsePayload::responses,
            ServerDataResponsePayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record Response(Identifier handlerId, List<Object> data) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Response> STREAM_CODEC = StreamCodec.of(
                (buf, val) -> {
                    ByteBufCodecs.fromCodecWithRegistries(Identifier.CODEC).encode(buf, val.handlerId());
                    buf.writeVarInt(val.data().size());
                    for (Object obj : val.data()) {
                        writeDynamic(buf, obj);
                    }
                },
                buf -> {
                    Identifier id = ByteBufCodecs.fromCodecWithRegistries(Identifier.CODEC).decode(buf);
                    int size = buf.readVarInt();
                    List<Object> data = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        data.add(readDynamic(buf));
                    }
                    return new Response(id, data);
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
                case Float f -> {
                    buf.writeByte(4);
                    buf.writeFloat(f);
                }
                case Double d -> {
                    buf.writeByte(5);
                    buf.writeDouble(d);
                }
                case Long l -> {
                    buf.writeByte(6);
                    buf.writeLong(l);
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
                case 4 -> buf.readFloat();
                case 5 -> buf.readDouble();
                case 6 -> buf.readLong();
                default -> null;
            };
        }
    }
}
