package cozmicweb.pda.datagen.sound;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.registry.ModSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

    public ModSoundProvider(PackOutput output) {
        super(output, PDACommon.MOD_ID);
    }

    @Override
    public void registerSounds() {
        this.add(ModSounds.TALLY_FORWARD.unwrapKey().orElseThrow().identifier(),
                definition().subtitle("subtitles.item.tally_counter.forward").with(
                        sound("pda:item/tally_counter/tally_forward_0"),
                        sound("pda:item/tally_counter/tally_forward_1"),
                        sound("pda:item/tally_counter/tally_forward_2")));

        this.add(ModSounds.TALLY_BACKWARD.unwrapKey().orElseThrow().identifier(),
                definition().subtitle("subtitles.item.tally_counter.backward").with(
                        sound("pda:item/tally_counter/tally_backward_0"),
                        sound("pda:item/tally_counter/tally_backward_1"),
                        sound("pda:item/tally_counter/tally_backward_2")));

        this.add(ModSounds.TALLY_TICK.unwrapKey().orElseThrow().identifier(),
                definition().subtitle("subtitles.item.tally_counter.tick").with(
                        sound("pda:item/tally_counter/tally_tick_0"),
                        sound("pda:item/tally_counter/tally_tick_1"),
                        sound("pda:item/tally_counter/tally_tick_2"),
                        sound("pda:item/tally_counter/tally_tick_3"),
                        sound("pda:item/tally_counter/tally_tick_4"),
                        sound("pda:item/tally_counter/tally_tick_5")));

        this.add(ModSounds.STOPWATCH_TICK_0.unwrapKey().orElseThrow().identifier(),
                definition().subtitle("subtitles.item.stopwatch.tick0").with(
                        sound("pda:item/stopwatch/stopwatch_tick_0_0"),
                        sound("pda:item/stopwatch/stopwatch_tick_0_1"),
                        sound("pda:item/stopwatch/stopwatch_tick_0_2"),
                        sound("pda:item/stopwatch/stopwatch_tick_0_3"),
                        sound("pda:item/stopwatch/stopwatch_tick_0_4"),
                        sound("pda:item/stopwatch/stopwatch_tick_0_5")));

        this.add(ModSounds.STOPWATCH_TICK_1.unwrapKey().orElseThrow().identifier(),
                definition().subtitle("subtitles.item.stopwatch.tick1").with(
                        sound("pda:item/stopwatch/stopwatch_tick_1_0"),
                        sound("pda:item/stopwatch/stopwatch_tick_1_1"),
                        sound("pda:item/stopwatch/stopwatch_tick_1_2"),
                        sound("pda:item/stopwatch/stopwatch_tick_1_3"),
                        sound("pda:item/stopwatch/stopwatch_tick_1_4"),
                        sound("pda:item/stopwatch/stopwatch_tick_1_5"),
                        sound("pda:item/stopwatch/stopwatch_tick_1_6")));

        this.add(ModSounds.RADAR_BEEP.unwrapKey().orElseThrow().identifier(),
                definition().subtitle("subtitles.item.radar.beep").with(
                        sound("pda:item/radar/radar_beep")));

    }

}
