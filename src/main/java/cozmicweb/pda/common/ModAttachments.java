package cozmicweb.pda.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PDACommon.MOD_ID);

    public static final Supplier<AttachmentType<String>> LAST_ENTITY_TYPE_KILLED = ATTACHMENT_TYPES.register(
            "last_entity_type_killed", () -> AttachmentType.builder(() -> "minecraft:zombie").serialize(Codec.STRING.fieldOf("last_entity_type_killed")).build()
    );

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }

}
