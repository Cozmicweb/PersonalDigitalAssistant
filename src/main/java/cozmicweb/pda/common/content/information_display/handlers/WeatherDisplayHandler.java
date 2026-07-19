package cozmicweb.pda.common.content.information_display.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class WeatherDisplayHandler extends InfoDisplayHandler {

    public WeatherDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.weather");
    }

    @Override
    public int getDefaultPriority() {
        return 100;
    }

    @Override
    public Component getDisplayText() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return Component.empty();
        boolean thundering = level.isThundering();
        boolean raining = level.isRaining();
        String translation = thundering ? "thunder" : (raining ? "rain" : "clear");
        return Component.translatable("weather.pda." + translation);
    }

}
