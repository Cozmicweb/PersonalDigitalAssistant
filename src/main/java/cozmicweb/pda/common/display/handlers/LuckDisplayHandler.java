package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class LuckDisplayHandler extends InfoDisplayHandler {

    public LuckDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.luck");
    }

    @Override
    public int getDefaultPriority() {
        return 300;
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        float luck = player == null ? 0 : player.getLuck();
        return Component.translatable("text.pda.fishermans_pocket_guide.text", String.format("%.1f", luck));
    }

}
