package cozmicweb.pda.client.gui;

import cozmicweb.pda.client.PDAClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.GuiLayer;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;
import org.jspecify.annotations.NonNull;

public class InfoOverlay implements GuiLayer {

    public static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(@NonNull GuiGraphicsExtractor guiGraphics, @NonNull DeltaTracker deltaTracker) {
        if (mc.player == null || mc.level == null || mc.gui.hud.isHidden() || mc.debugEntries.isOverlayVisible() || mc.gui.screen() != null)
            return;

        int y = 5;
        for (InfoDisplayHandler handler : InfoDisplayManager.getActiveHandlers()) {
            try {
                Component text = handler.getDisplayText();
                guiGraphics.text(mc.font, text, 5, y, ARGB.opaque(0xFFFFFF));
                y += 10;
            } catch (Exception e) {
                PDAClient.LOGGER.error("Error rendering info display", e);
            }
        }
    }

}