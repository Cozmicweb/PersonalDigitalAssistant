package cozmicweb.pda.common.display.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class VelocityDisplayHandler extends InfoDisplayHandler {

    public static final String UNIT = " ᴍ/ꜱ";

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        double velocity = player == null ? 0 : player.getDeltaMovement().multiply(20, 0, 20).length();
        return Component.literal(String.format("%.2f", velocity) + UNIT);
    }

}
