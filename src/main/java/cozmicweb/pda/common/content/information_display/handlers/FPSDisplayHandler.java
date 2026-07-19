package cozmicweb.pda.common.content.information_display.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class FPSDisplayHandler extends InfoDisplayHandler {

    public FPSDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.fps");
    }

    @Override
    public int getDefaultPriority() {
        return -1;
    }

    @Override
    public Component getDisplayText() {
        int fps = Minecraft.getInstance().getFps();
        return Component.translatable("text.pda.fps.format", fps);
    }

}
