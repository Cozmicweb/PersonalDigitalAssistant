package cozmicweb.pda.common;

import net.neoforged.neoforge.common.ModConfigSpec;

public class PDAConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue TALLY_COUNTER_KILL_SOUND;
    public static final ModConfigSpec.IntValue TALLY_COUNTER_LIMIT;

    public static final ModConfigSpec.BooleanValue STOPWATCH_PLAY_SOUND;

    public static final ModConfigSpec.IntValue RADAR_SIZE;
    public static final ModConfigSpec.IntValue RADAR_UPDATE_RATE;

    public static final ModConfigSpec.IntValue LIFEFORM_ANALYZER_SIZE;
    public static final ModConfigSpec.IntValue LIFEFORM_ANALYZER_UPDATE_RATE;

    public static final ModConfigSpec.IntValue DPS_METER_UPDATE_RATE;

    public static final ModConfigSpec.IntValue METAL_DETECTOR_SIZE;
    public static final ModConfigSpec.IntValue METAL_DETECTOR_UPDATE_RATE;

    static {
        BUILDER.push("item_dps_meter");
            DPS_METER_UPDATE_RATE = BUILDER.defineInRange("item_dps_meter_update_rate", 3, 1, 15);
        BUILDER.pop();

        BUILDER.push("item_lifeform_analyzer");
        LIFEFORM_ANALYZER_SIZE = BUILDER.defineInRange("item_lifeform_analyzer_size", 16, 8, 128);
        LIFEFORM_ANALYZER_UPDATE_RATE = BUILDER.defineInRange("item_lifeform_analyzer_update_rate", 5, 1, 30);
        BUILDER.pop();

        BUILDER.push("item_metal_detector");
            METAL_DETECTOR_SIZE = BUILDER.defineInRange("metal_detector_size", 16, 8, 128);
            METAL_DETECTOR_UPDATE_RATE = BUILDER.defineInRange("metal_detector_update_rate", 5, 1, 30);
        BUILDER.pop();

        BUILDER.push("item_radar");
            RADAR_SIZE = BUILDER.defineInRange("item_radar_size", 16, 8, 128);
            RADAR_UPDATE_RATE = BUILDER.defineInRange("item_radar_update_rate", 5, 1, 30);
        BUILDER.pop();

        BUILDER.push("item_stopwatch");
            STOPWATCH_PLAY_SOUND = BUILDER.define("item_stopwatch_play_sound", true);
        BUILDER.pop();

        BUILDER.push("item_tally_counter");
            TALLY_COUNTER_KILL_SOUND = BUILDER.define("item_tally_kill_sound", true);
            TALLY_COUNTER_LIMIT = BUILDER.defineInRange("item_tally_limit", 5, 1, 9);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
