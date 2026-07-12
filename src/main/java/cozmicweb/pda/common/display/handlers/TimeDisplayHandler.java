package cozmicweb.pda.common.display.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;

public class TimeDisplayHandler extends InfoDisplayHandler {

    @Override
    public Component getDisplayText() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        if (level != null) {
            long time = level.getOverworldClockTime() % 24000L;
            int totalMinutes = (int) (((time + 18000) % 24000) * 1440 / 24000);

            int hour24 = totalMinutes / 60;
            int minute = totalMinutes % 60;

            String amPm = hour24 < 12 ? "AM" : "PM";
            int hour12 = hour24 % 12;
            if (hour12 == 0) hour12 = 12;

            String formatted = String.format("%d:%02d %s", hour12, minute, amPm);
            return Component.literal(formatted);
        } else {
            return Component.literal("12:00 PM");
        }
    }

}
