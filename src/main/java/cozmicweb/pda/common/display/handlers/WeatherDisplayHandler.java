package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jspecify.annotations.NonNull;

public class WeatherDisplayHandler extends InfoDisplayHandler {

    @Override
    public Component getDisplayText() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return Component.empty();
        boolean thundering = level.isThundering();
        boolean raining = level.isRaining();
        return Component.literal(thundering ? "Thundering" : (raining ? "Raining" : "Sunny"));
    }

}
