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
import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class InfoOverlay implements GuiLayer {

    public static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(@NonNull GuiGraphicsExtractor guiGraphics, @NonNull DeltaTracker deltaTracker) {
        if (mc.player == null || mc.level == null || mc.gui.hud.isHidden() || mc.debugEntries.isOverlayVisible())
            return;

        Set<InfoDisplayHandler> handlers = InfoDisplayManager.getActiveHandlers();
        List<InfoDisplayHandler> sorted = handlers.stream().sorted(Comparator.comparingInt(InfoDisplayHandler::getPriority)).toList();

        int y = 5;
        for (InfoDisplayHandler handler : sorted) {
            try {
                Component text = handler.getDisplayText();
                if (text.getString().isEmpty()) continue;
                guiGraphics.text(mc.font, text, 5, y, ARGB.opaque(0xFFFFFF));
                y += 10;
            } catch (Exception e) {
                PDAClient.LOGGER.error("Error rendering info display", e);
            }
        }
    }

}