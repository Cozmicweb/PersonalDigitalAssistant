package cozmicweb.pda.common;

import cozmicweb.pda.common.content.reforge_modifier.ReforgeModifierRegistry;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

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

    public static final ModConfigSpec.IntValue RAIDER_ARMY_SIZE;
    public static final ModConfigSpec.IntValue RAIDER_ARMY_MIN_Y;
    public static final ModConfigSpec.IntValue RAIDER_ARMY_ABANDON_TIMEOUT;
    public static final ModConfigSpec.IntValue RAIDER_ARMY_IDLE_TIMEOUT;
    public static final ModConfigSpec.IntValue RAIDER_ARMY_START_TIME;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> REFORGE_MODIFIER_MULTIPLIERS;
    public static final ModConfigSpec.DoubleValue REFORGE_MODIFIER_GLOBAL_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue REFORGE_CRAFTING_CHANCE;

    static {
        BUILDER.push("common_raider_army");
            RAIDER_ARMY_SIZE = BUILDER.defineInRange("raider_army_size", 8, 1, 12);
            RAIDER_ARMY_MIN_Y = BUILDER.defineInRange("raider_army_min_y", 50, -64, 256);
            RAIDER_ARMY_ABANDON_TIMEOUT = BUILDER.defineInRange("raider_army_abandon_timeout", 60, 1, 1000); // Seconds
            RAIDER_ARMY_IDLE_TIMEOUT = BUILDER.defineInRange("raider_army_idle_timeout", 1200, 1, 2400); // Seconds
            RAIDER_ARMY_START_TIME = BUILDER.defineInRange("raider_army_start_time", 10, 0, 60); // Seconds
        BUILDER.pop();

        BUILDER.push("common_items");
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
        BUILDER.pop();

        BUILDER.push("reforges");
            REFORGE_CRAFTING_CHANCE = BUILDER.defineInRange("reforge_crafting_chance", 0.2, 0.0, 1.0);
            REFORGE_MODIFIER_GLOBAL_MULTIPLIER = BUILDER.defineInRange("reforge_modifier_global_multiplier", 2.0, 0.0, 10.0);
            REFORGE_MODIFIER_MULTIPLIERS = BUILDER.defineList(
                    "reforge_modifier_multipliers",
                    List.of("minecraft:mining_efficiency = 1.0"),
                    () -> "namespace:path = multiplier",
                    ReforgeModifierRegistry::reforgeModifierMultipliersConfigValidator
            );
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
