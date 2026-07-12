package cozmicweb.pda.common.display.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PositionDisplayHandler extends InfoDisplayHandler {

    @Override
    public Component getDisplayText() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player != null) {
            Vec3 position = player.position();
            return Component.literal(String.format("%.0f, %.0f, %.0f", position.x, position.y, position.z));
        } else {
            return Component.literal("0, 0, 0");
        }
    }

}
