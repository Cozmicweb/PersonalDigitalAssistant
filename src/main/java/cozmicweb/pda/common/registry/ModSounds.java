package cozmicweb.pda.common.registry;

import cozmicweb.pda.common.PDACommon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PDACommon.MOD_ID);

    public static final Holder<SoundEvent> TALLY_FORWARD = SOUND_EVENTS.register("tally_forward", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> TALLY_BACKWARD = SOUND_EVENTS.register("tally_backward", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> TALLY_TICK = SOUND_EVENTS.register("tally_tick", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> STOPWATCH_TICK_0 = SOUND_EVENTS.register("stopwatch_tick_0", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> STOPWATCH_TICK_1 = SOUND_EVENTS.register("stopwatch_tick_1", SoundEvent::createVariableRangeEvent);

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }

}
