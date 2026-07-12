package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jspecify.annotations.NonNull;

public class TimeDisplayHandler extends InfoDisplayHandler {

    public static boolean is24HourFormat() {
        return PDAClientConfig.TIME_FORMAT.get() == TimeFormat.HOUR24;
    }

    public static boolean shouldZerosPersist() {
        return PDAClientConfig.TIME_ZEROS.get();
    }

    public static boolean shouldAppendMeridiems() {
        return PDAClientConfig.TIME_MERIDIEMS.get();
    }

    public TimeDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.time");
    }

    @Override
    public Component getDisplayText() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        int finalHour = 12;
        int finalMinute = 0;
        Component meridiem = Component.empty();

        if (level != null) {
            long time = level.getOverworldClockTime() % 24000L;
            int totalMinutes = (int) (((time + 18000) % 24000) * 1440 / 24000);

            int hour24 = totalMinutes / 60;
            finalMinute = totalMinutes % 60;

            if (is24HourFormat()) {
                finalHour = hour24;
            } else {
                Component amPm = hour24 < 12 ? Component.translatable("text.pda.time.pm") : Component.translatable("text.pda.time.am");
                int hour12 = hour24 % 12;
                if (hour12 == 0) hour12 = 12;

                finalHour = hour12;
                if (shouldAppendMeridiems()) meridiem = Component.literal(" ").append(amPm);
            }
        }

        String pattern = shouldZerosPersist() ? "%02d:%02d" : "%d:%02d";
        return Component.literal(pattern.formatted(finalHour, finalMinute)).append(meridiem);
    }

    public enum TimeFormat implements TranslatableEnum {
        HOUR12,
        HOUR24;

        @Override
        public @NonNull Component getTranslatedName() {
            return Component.translatable("pda.configuration.clock_time_format." + this.name().toLowerCase());
        }
    }

}
