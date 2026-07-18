package cozmicweb.pda.common.registry;

import com.mojang.serialization.Codec;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.attachments.DamageTracker;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PDACommon.MOD_ID);

    public static final Supplier<AttachmentType<String>> LAST_ENTITY_TYPE_KILLED = ATTACHMENT_TYPES.register("last_entity_type_killed", () -> AttachmentType.builder(() -> "minecraft:zombie").serialize(Codec.STRING.fieldOf("last_entity_type_killed")).build());
    public static final Supplier<AttachmentType<DamageTracker>> DAMAGE_TRACKER = ATTACHMENT_TYPES.register("damage_tracker", () -> AttachmentType.builder(DamageTracker::new).serialize(DamageTracker.CODEC.fieldOf("data")).copyOnDeath().build());
    public static final Supplier<AttachmentType<String>> OVERRIDE_LOOT = ATTACHMENT_TYPES.register("override_loot", () -> AttachmentType.builder(() -> "").serialize(Codec.STRING.fieldOf("override_loot")).build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }

}
