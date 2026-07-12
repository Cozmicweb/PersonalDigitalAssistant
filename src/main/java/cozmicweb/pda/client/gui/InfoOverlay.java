package cozmicweb.pda.client.gui;

import cozmicweb.pda.client.PDAClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.gui.GuiLayer;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;

public class InfoOverlay implements GuiLayer {

    public static final Minecraft minecraft = Minecraft.getInstance();

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        int y = 5;
        for (InfoDisplayHandler handler : InfoDisplayManager.getActiveHandlers()) {
            try {
                Component text = handler.getDisplayText();
                guiGraphics.text(minecraft.font, text, 5, y, ARGB.opaque(0xFFFFFF));
                y += 10;
            } catch (Exception e) {
                PDAClient.LOGGER.error("Error rendering info display", e);
            }
        }
    }

}