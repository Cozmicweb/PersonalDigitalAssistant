package cozmicweb.pda.client;

import cozmicweb.pda.common.display.handlers.TimeDisplayHandler;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PDAClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Compass
    public static final ModConfigSpec.ConfigValue<String> COMPASS_FORMAT;

    // Stopwatch
    public static final ModConfigSpec.ConfigValue<String> STOPWATCH_FORMAT;

    // Clock
    public static final ModConfigSpec.EnumValue<TimeDisplayHandler.TimeFormat> CLOCK_FORMAT;
    public static final ModConfigSpec.BooleanValue CLOCK_MERIDIEMS;
    public static final ModConfigSpec.BooleanValue CLOCK_ZEROS;

    // Lifeform Analyzer
    public static final ModConfigSpec.BooleanValue LIFEFORM_ANALYZER_SOUND;

    static {
        // Compass
        BUILDER.push("Compass");
        COMPASS_FORMAT = BUILDER.comment("The specifiers \"%x\", \"%y\", and \"%z\" will be replaced by their corresponding coordinate.").define("compass_format", "%x %y %z");
        BUILDER.pop();

        // Stopwatch
        BUILDER.push("Stopwatch");
        STOPWATCH_FORMAT = BUILDER.comment("The first Java format specifier will be replaced with your current velocity as a float.").define("stopwatch_format", "%.2f ᴍ/ꜱ");
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
