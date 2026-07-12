package cozmicweb.pda.common;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PDAConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Tally Counter
    public static final ModConfigSpec.BooleanValue TALLY_KILL_SOUND;
    public static final ModConfigSpec.IntValue TALLY_COUNT_LIMIT;

    static {
        // Tally Counter
        BUILDER.push("Tally Counter");
        TALLY_KILL_SOUND = BUILDER.comment("When a tally counter is in your inventory, it will play a tick sound when a mob is killed.").define("tally_kill_sound", true);
        TALLY_COUNT_LIMIT = BUILDER.comment("How many places should the tally counter have? Anything over 9 places will not work as intended.").defineInRange("tally_count_limit", 5, 1, 9);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
