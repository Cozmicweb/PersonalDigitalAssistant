package cozmicweb.pda.common;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PDAConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Tally Counter
    public static final ModConfigSpec.BooleanValue TALLY_KILL_SOUND;
    public static final ModConfigSpec.IntValue TALLY_COUNT_LIMIT;

    // Stopwatch
    public static final ModConfigSpec.BooleanValue STOPWATCH_PLAY_SOUND;

    // Radar
    public static final ModConfigSpec.IntValue RADAR_SIZE;
    public static final ModConfigSpec.IntValue RADAR_UPDATE_RATE;

    // DPS Meter
    public static final ModConfigSpec.IntValue DPS_METER_UPDATE_RATE;

    // Metal Detector
    public static final ModConfigSpec.IntValue METAL_DETECTOR_SIZE;
    public static final ModConfigSpec.IntValue METAL_DETECTOR_UPDATE_RATE;

    static {
        // DPS Meter
        BUILDER.push("dps_meter");
        DPS_METER_UPDATE_RATE = BUILDER.comment("How often the DPS meter should update in seconds.").defineInRange("dps_meter_update_rate", 3, 1, 15);
        BUILDER.pop();

        // Metal Detector
        BUILDER.push("metal_detector");
        METAL_DETECTOR_SIZE = BUILDER.comment("Size of the metal detector's detection zone.").defineInRange("metal_detector_size", 16, 8, 128);
        METAL_DETECTOR_UPDATE_RATE = BUILDER.comment("How often the metal detector should scan for monsters in seconds.").defineInRange("metal_detector_update_rate", 5, 1, 30);
        BUILDER.pop();

        // Radar
        BUILDER.push("radar_and_lifeform_analyzer");
        RADAR_SIZE = BUILDER.comment("Size of the radar's detection zone.").defineInRange("radar_size", 16, 8, 128);
        RADAR_UPDATE_RATE = BUILDER.comment("How often the radar should scan for monsters in seconds.").defineInRange("radar_update_rate", 5, 1, 30);
        BUILDER.pop();

        // Stopwatch
        BUILDER.push("stopwatch");
        STOPWATCH_PLAY_SOUND = BUILDER.comment("When a stopwatch is in your inventory, it will play a tick sound while running.").define("stopwatch_play_sound", true);
        BUILDER.pop();

        // Tally Counter
        BUILDER.push("tally_counter");
        TALLY_KILL_SOUND = BUILDER.comment("When a tally counter is in your inventory, it will play a tick sound when a mob is killed.").define("tally_kill_sound", true);
        TALLY_COUNT_LIMIT = BUILDER.comment("How many places should the tally counter have? Anything over 9 places will not work as intended.").defineInRange("tally_count_limit", 5, 1, 9);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
