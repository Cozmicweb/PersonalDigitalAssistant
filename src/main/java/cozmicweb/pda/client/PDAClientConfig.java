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

    public static final ModConfigSpec.EnumValue<TimeDisplayHandler.TimeFormat> TIME_FORMAT;
    public static final ModConfigSpec.BooleanValue TIME_MERIDIEMS;
    public static final ModConfigSpec.BooleanValue TIME_ZEROS;

    public static final ModConfigSpec.BooleanValue RARE_MOB_SOUND;

    public static final ModConfigSpec.ConfigValue<Integer> LIST_POS_X;
    public static final ModConfigSpec.ConfigValue<Integer> LIST_POS_Y;
    public static final ModConfigSpec.IntValue LIST_TEXT_ALPHA;
    public static final ModConfigSpec.BooleanValue LIST_TEXT_DROP_SHADOW;
    public static final ModConfigSpec.IntValue LIST_TEXT_SIZE;
    public static final ModConfigSpec.ConfigValue<String> LIST_TEXT_COLOR;
    public static final ModConfigSpec.EnumValue<RenderTextVerticalAlignment> LIST_VERTICAL_ALIGNMENT;
    public static final ModConfigSpec.EnumValue<RenderTextHorizontalAlignment> LIST_HORIZONTAL_ALIGNMENT;

    static {
        BUILDER.push("info_list");
            LIST_POS_X = BUILDER.define("list_pos_x", 5);
            LIST_POS_Y = BUILDER.define("list_pos_y", 5);
            LIST_TEXT_DROP_SHADOW = BUILDER.define("list_text_drop_shadow", true);
            LIST_TEXT_ALPHA = BUILDER.defineInRange("list_text_alpha", 255, 0, 255);
            LIST_TEXT_SIZE = BUILDER.defineInRange("list_text_size", 100, 0, 200);
            LIST_TEXT_COLOR = BUILDER.define("list_text_color", "FFFFFF");
            LIST_VERTICAL_ALIGNMENT = BUILDER.defineEnum("list_vertical_alignment", RenderTextVerticalAlignment.TOP_TO_BOTTOM);
            LIST_HORIZONTAL_ALIGNMENT = BUILDER.defineEnum("list_horizontal_alignment", RenderTextHorizontalAlignment.LEFT_TO_RIGHT);
        BUILDER.pop();

        BUILDER.push("info_visibility");
        InfoDisplayManager.getAllHandlers().forEach((id, _) -> VISIBILITY_MAP.put(id, BUILDER.define(id + "_visibility", true)));
        BUILDER.pop();

        BUILDER.push("info_priorities");
            InfoDisplayManager.getAllHandlers().forEach((id, handler) -> {
                int defaultPriority = handler.getDefaultPriority();
                PRIORITY_MAP.put(id, BUILDER.comment("Higher = Lower").define(id + "_priority", defaultPriority));
            });
        BUILDER.pop();

        BUILDER.push("info_display_settings");
            BUILDER.push("display_time");
            TIME_FORMAT = BUILDER.defineEnum("clock_format", TimeDisplayHandler.TimeFormat.HOUR12);
            TIME_MERIDIEMS = BUILDER.define("time_meridiems", true);
            TIME_ZEROS = BUILDER.define("time_zeros", true);
            BUILDER.pop();

            BUILDER.push("display_rare_mob");
            RARE_MOB_SOUND = BUILDER.define("rare_mob_sound", true);
            BUILDER.pop();
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
