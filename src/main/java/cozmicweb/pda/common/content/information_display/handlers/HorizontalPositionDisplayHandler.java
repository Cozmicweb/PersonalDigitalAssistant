package cozmicweb.pda.common.content.information_display.handlers;

import cozmicweb.pda.common.content.information_display.InfoDisplayManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class HorizontalPositionDisplayHandler extends InfoDisplayHandler {

    public HorizontalPositionDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.horizontal_position");
    }

    @Override
    public int getDefaultPriority() {
        return 1000;
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return Component.empty();

        Vec3 pos = player.position();
        if (InfoDisplayManager.isHandlerActive("display_vertical_position")) {
            return Component.translatable("text.pda.position_combined.format", String.format("%.1f", pos.x), String.format("%.1f", pos.y), String.format("%.1f", pos.z));
        }
        return Component.translatable("text.pda.position.format", String.format("%.1f", pos.x), String.format("%.1f", pos.z));
    }

}
