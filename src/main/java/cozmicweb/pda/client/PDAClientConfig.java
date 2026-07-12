package cozmicweb.pda.client;

import cozmicweb.pda.common.display.handlers.TimeDisplayHandler;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PDAClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Stopwatch
    public static final ModConfigSpec.ConfigValue<String> STOPWATCH_FORMAT;

    // Position
    public static final ModConfigSpec.ConfigValue<String> POSITION_FORMAT;
    public static final ModConfigSpec.ConfigValue<String> DEPTH_FORMAT;
    public static final ModConfigSpec.ConfigValue<String> COMBINED_POSITION_FORMAT;

    // Clock
    public static final ModConfigSpec.EnumValue<TimeDisplayHandler.TimeFormat> CLOCK_FORMAT;
    public static final ModConfigSpec.BooleanValue CLOCK_MERIDIEMS;
    public static final ModConfigSpec.BooleanValue CLOCK_ZEROS;

    // Lifeform Analyzer
    public static final ModConfigSpec.BooleanValue LIFEFORM_ANALYZER_SOUND;

    static {
        // Stopwatch
        BUILDER.push("Stopwatch");
        STOPWATCH_FORMAT = BUILDER.comment("The first Java format specifier will be replaced with your current velocity as a float.").define("stopwatch_format", "%.2f ᴍ/ꜱ");
        BUILDER.pop();

        // Position
        BUILDER.push("Position");
        POSITION_FORMAT = BUILDER.comment("The first two Java format specifiers will be replaced with your current X and Z coordinates as floats.").define("position_format", "%.1f, %.1f");
        DEPTH_FORMAT = BUILDER.comment("The first Java format specifier will be replaced with your current Y coordinate as a float.").define("depth_format", "%.1f");
        COMBINED_POSITION_FORMAT = BUILDER.comment("The first three Java format specifiers will be replaced with your current X, Y, and Z coordinates as floats.").define("combined_position_format", "%.1f, %.1f, %.1f");
        BUILDER.pop();

        // Clock
        BUILDER.push("Clock");
        CLOCK_FORMAT = BUILDER.defineEnum("clock_format", TimeDisplayHandler.TimeFormat.HOUR12);
        CLOCK_MERIDIEMS = BUILDER.comment("Whether to display AM/PM on 12-hour time.").define("clock_meridiems", true);
        CLOCK_ZEROS = BUILDER.comment("Whether to display zeros in place of absent tens digits.").define("clock_zeros", true);
        BUILDER.pop();

        // Lifeform Analyzer
        BUILDER.push("Lifeform Analyzer");
        LIFEFORM_ANALYZER_SOUND = BUILDER.comment("Whether to play a sound when a new lifeform is detected.").define("lifeform_analyzer_sound", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
