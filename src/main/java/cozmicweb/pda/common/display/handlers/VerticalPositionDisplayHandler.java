package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import cozmicweb.pda.common.display.InfoDisplayManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class VerticalPositionDisplayHandler extends InfoDisplayHandler {

    public VerticalPositionDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.vertical_position");
    }

    @Override
    public int getDefaultPriority() {
        return 1001;
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return Component.empty();

        if (InfoDisplayManager.isHandlerActive("display_horizontal_position")) {
            return Component.empty();
        }

        Vec3 pos = player.position();
        return Component.translatable("text.pda.depth.format", String.format("%.1f", pos.y));
    }
}
