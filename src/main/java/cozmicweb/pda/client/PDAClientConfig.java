package cozmicweb.pda.client;

import cozmicweb.pda.client.gui.RenderTextHorizontalAlignment;
import cozmicweb.pda.client.gui.RenderTextVerticalAlignment;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.display.handlers.TimeDisplayHandler;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;

public class PDAClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    private static final HashMap<Identifier, ModConfigSpec.ConfigValue<Integer>> PRIORITY_MAP = new HashMap<>();
    private static final HashMap<Identifier, ModConfigSpec.ConfigValue<Boolean>> VISIBILITY_MAP = new HashMap<>();

    // Clock
    public static final ModConfigSpec.EnumValue<TimeDisplayHandler.TimeFormat> CLOCK_FORMAT;
    public static final ModConfigSpec.BooleanValue CLOCK_MERIDIEMS;
    public static final ModConfigSpec.BooleanValue CLOCK_ZEROS;

    // Lifeform Analyzer
    public static final ModConfigSpec.BooleanValue LIFEFORM_ANALYZER_SOUND;

    // Rendering
    public static final ModConfigSpec.ConfigValue<Integer> RENDER_POS_X;
    public static final ModConfigSpec.ConfigValue<Integer> RENDER_POS_Y;
    public static final ModConfigSpec.IntValue RENDER_ALPHA;
    public static final ModConfigSpec.BooleanValue RENDER_DROP_SHADOW;
    public static final ModConfigSpec.IntValue RENDER_SIZE;
    public static final ModConfigSpec.ConfigValue<String> RENDER_COLOR;
    public static final ModConfigSpec.EnumValue<RenderTextVerticalAlignment> RENDER_TEXT_VERTICAL_ALIGNMENT;
    public static final ModConfigSpec.EnumValue<RenderTextHorizontalAlignment> RENDER_TEXT_HORIZONTAL_ALIGNMENT;

    static {
        // Rendering
        BUILDER.push("rendering");
        RENDER_POS_X = BUILDER.comment("The X position of the info display.").define("render_pos_x", 5);
        RENDER_POS_Y = BUILDER.comment("The Y position of the info display.").define("render_pos_y", 5);
        RENDER_DROP_SHADOW = BUILDER.comment("Whether to render a drop shadow behind the info display.").define("render_drop_shadow", true);
        RENDER_ALPHA = BUILDER.comment("The alpha value of the info display.").defineInRange("render_alpha", 255, 0, 255);
        RENDER_SIZE = BUILDER.comment("How large should the list be rendered?").defineInRange("render_size", 100, 0, 200);
        RENDER_COLOR = BUILDER.comment("Text color as a 6-char hex (RRGGBB).").define("render_color", "FFFFFF");
        RENDER_TEXT_VERTICAL_ALIGNMENT = BUILDER.comment("What direction should the list be rendered?").defineEnum("render_text_vertical_alignment", RenderTextVerticalAlignment.TOP_TO_BOTTOM);
        RENDER_TEXT_HORIZONTAL_ALIGNMENT = BUILDER.comment("How should should the text be rendered?").defineEnum("render_text_horizontal_alignment", RenderTextHorizontalAlignment.LEFT_TO_RIGHT);
        BUILDER.pop();

        // All Display Handler Priorities
        BUILDER.push("priorities");
        InfoDisplayManager.getAllHandlers().forEach((id, handler) -> {
            int defaultPriority = handler.getDefaultPriority();
            PRIORITY_MAP.put(id, BUILDER.comment("Higher = Lower").define(id + "_priority", defaultPriority));
        });
        BUILDER.pop();

        // All Display Handler Visibility
        BUILDER.push("visibility");
        InfoDisplayManager.getAllHandlers().forEach((id, _) -> VISIBILITY_MAP.put(id, BUILDER.define(id + "_visibility", true)));
        BUILDER.pop();

        // Clock
        BUILDER.push("clock");
        CLOCK_FORMAT = BUILDER.defineEnum("clock_format", TimeDisplayHandler.TimeFormat.HOUR12);
        CLOCK_MERIDIEMS = BUILDER.comment("Whether to display AM/PM on 12-hour time.").define("clock_meridiems", true);
        CLOCK_ZEROS = BUILDER.comment("Whether to display zeros in place of absent tens digits.").define("clock_zeros", true);
        BUILDER.pop();

        // Lifeform Analyzer
        BUILDER.push("lifeform_analyzer");
        LIFEFORM_ANALYZER_SOUND = BUILDER.comment("Whether to play a sound when a new lifeform is detected.").define("lifeform_analyzer_sound", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static int getPriority(Identifier id) {
        return PRIORITY_MAP.get(id).get();
    }

    public static boolean getVisibility(Identifier id) {
        return VISIBILITY_MAP.get(id).get();
    }

}
