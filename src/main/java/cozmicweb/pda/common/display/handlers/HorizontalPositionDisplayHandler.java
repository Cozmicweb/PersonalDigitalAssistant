package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import cozmicweb.pda.common.display.InfoDisplayManager;
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
    public int getDefaultPriority() {
        return 1000;
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return Component.empty();

        Vec3 pos = player.position();
        if (InfoDisplayManager.isHandlerActive("display_vertical_position")) {
            return Component.literal(String.format(PDAClientConfig.COMBINED_POSITION_FORMAT.get(), pos.x, pos.y, pos.z));
        }
        return Component.literal(String.format(PDAClientConfig.POSITION_FORMAT.get(), pos.x, pos.z));
    }

}
