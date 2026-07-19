package cozmicweb.pda.common.content.information_display.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class VelocityDisplayHandler extends InfoDisplayHandler {

    public VelocityDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.velocity");
    }

    @Override
    public int getDefaultPriority() {
        return 900;
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        double velocity = player == null ? 0 : player.getDeltaMovement().multiply(20, 0, 20).length();
        return Component.translatable("text.pda.velocity.format", String.format("%.2f", velocity));
    }

}
