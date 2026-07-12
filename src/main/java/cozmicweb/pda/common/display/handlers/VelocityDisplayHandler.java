package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class VelocityDisplayHandler extends InfoDisplayHandler {

    @Override
    public int getPriority() {
        return 900;
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        double velocity = player == null ? 0 : player.getDeltaMovement().multiply(20, 0, 20).length();
        return Component.literal(String.format(PDAClientConfig.STOPWATCH_FORMAT.get(), velocity));
    }

}
