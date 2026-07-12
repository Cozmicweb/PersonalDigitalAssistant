package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jspecify.annotations.NonNull;

public class TimeDisplayHandler extends InfoDisplayHandler {

    public static boolean is24HourFormat() {
        return PDAClientConfig.CLOCK_FORMAT.get() == TimeFormat.HOUR24;
    }

    public static boolean shouldZerosPersist() {
        return PDAClientConfig.CLOCK_ZEROS.get();
    }

    public static boolean shouldAppendMeridiems() {
        return PDAClientConfig.CLOCK_MERIDIEMS.get();
    }

    @Override
    public Component getDisplayText() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        int finalHour = 12;
        int finalMinute = 0;
        String meridiem = "";

        if (level != null) {
            long time = level.getOverworldClockTime() % 24000L;
            int totalMinutes = (int) (((time + 18000) % 24000) * 1440 / 24000);

            int hour24 = totalMinutes / 60;
            finalMinute = totalMinutes % 60;

            if (is24HourFormat()) {
                finalHour = hour24;
            } else {
                String amPm = hour24 < 12 ? "PM" : "AM"; // IDK why it's swapped
                int hour12 = hour24 % 12;
                if (hour12 == 0) hour12 = 12;

                finalHour = hour12;
                if (shouldAppendMeridiems()) meridiem = " " + amPm;
            }
        }

        String pattern = shouldZerosPersist() ? "%02d:%02d" : "%d:%02d";
        return Component.literal(pattern.formatted(finalHour, finalMinute) + meridiem);
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
