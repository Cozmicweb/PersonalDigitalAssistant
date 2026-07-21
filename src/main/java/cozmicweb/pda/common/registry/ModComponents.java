package cozmicweb.pda.common.registry;

import com.mojang.serialization.Codec;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.component.TallyAnimState;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PDACommon.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> TALLY_COUNT =DATA_COMPONENTS.register("tally_count",
            () -> DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build());

    public static final Supplier<DataComponentType<Integer>> TALLY_DISPLAY = DATA_COMPONENTS.register("tally_display",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static final Supplier<DataComponentType<TallyAnimState>> TALLY_ANIM = DATA_COMPONENTS.register("tally_anim",
            () -> DataComponentType.<TallyAnimState>builder()
                    .persistent(TallyAnimState.CODEC)
                    .networkSynchronized(TallyAnimState.STREAM_CODEC)
                    .build());

    public static final Supplier<DataComponentType<Long>> STOPWATCH_START_TIME = DATA_COMPONENTS.register("stopwatch_start_time",
            () -> DataComponentType.<Long>builder()
                    .persistent(Codec.LONG)
                    .networkSynchronized(ByteBufCodecs.LONG)
                    .build());

    public static final Supplier<DataComponentType<Long>> STOPWATCH_PAUSE_TIME = DATA_COMPONENTS.register("stopwatch_pause_time",
            () -> DataComponentType.<Long>builder()
                    .persistent(Codec.LONG)
                    .networkSynchronized(ByteBufCodecs.LONG)
                    .build());

    public static final Supplier<DataComponentType<Boolean>> STOPWATCH_PAUSED = DATA_COMPONENTS.register("stopwatch_paused",
            () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build());

    public static final Supplier<DataComponentType<Boolean>> STOPWATCH_PRESSED = DATA_COMPONENTS.register("stopwatch_pressed",
            () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build());

    public static final Supplier<DataComponentType<Integer>> STOPWATCH_ANIM = DATA_COMPONENTS.register("stopwatch_anim",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build());

    public static final Supplier<DataComponentType<Identifier>> REFORGE_MODIFIER = DATA_COMPONENTS.register("reforge_modifier",
            () -> DataComponentType.<Identifier>builder()
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC)
                    .build());

    public static final Supplier<DataComponentType<Identifier>> REFORGE_GROUP = DATA_COMPONENTS.register("reforge_group",
            () -> DataComponentType.<Identifier>builder()
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC)
                    .build());

    public static final Supplier<DataComponentType<Component>> ORIGINAL_NAME = DATA_COMPONENTS.register("original_name",
            () -> DataComponentType.<Component>builder()
                    .persistent(ComponentSerialization.CODEC)
                    .networkSynchronized(ComponentSerialization.STREAM_CODEC)
                    .build());

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }

}
