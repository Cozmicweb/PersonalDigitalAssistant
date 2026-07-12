package cozmicweb.pda.client.gui;

import cozmicweb.pda.client.PDAClient;
import cozmicweb.pda.client.PDAClientConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.gui.GuiLayer;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class InfoOverlay implements GuiLayer {

    public static final Minecraft mc = Minecraft.getInstance();
    private static final int LINE_HEIGHT = 10;

    public static int getX(GuiGraphicsExtractor graphics) {
        RenderTextHorizontalAlignment alignment = getRenderAlignment();
        if (alignment == RenderTextHorizontalAlignment.LEFT_TO_RIGHT)
            return PDAClientConfig.LIST_POS_X.get();
        else if (alignment == RenderTextHorizontalAlignment.RIGHT_TO_LEFT)
            return graphics.guiWidth() - PDAClientConfig.LIST_POS_X.get();
        else
            return graphics.guiWidth() / 2 + PDAClientConfig.LIST_POS_X.get();
    }

    public static int getY(GuiGraphicsExtractor graphics) {
        if (getDirection() == RenderTextVerticalAlignment.TOP_TO_BOTTOM)
            return PDAClientConfig.LIST_POS_Y.get();
        else if (getDirection() == RenderTextVerticalAlignment.BOTTOM_TO_TOP)
            return graphics.guiHeight() - PDAClientConfig.LIST_POS_Y.get();
        else
            return graphics.guiHeight() / 2 + PDAClientConfig.LIST_POS_Y.get();
    }

    public static RenderTextVerticalAlignment getDirection() {
        return PDAClientConfig.LIST_VERTICAL_ALIGNMENT.get();
    }

    public static int getAlpha() {
        return PDAClientConfig.LIST_TEXT_ALPHA.get();
    }

    public static boolean getDropShadow() {
        return PDAClientConfig.LIST_TEXT_DROP_SHADOW.get();
    }

    public static RenderTextHorizontalAlignment getRenderAlignment() {
        return PDAClientConfig.LIST_HORIZONTAL_ALIGNMENT.get();
    }

    public static int getColor() {
        String hex = PDAClientConfig.LIST_TEXT_COLOR.get();
        boolean valid = hex.matches("^#?[0-9A-Fa-f]{6}$");
        int result;

        if (valid) {
            int alpha = getAlpha();
            int rgb = Integer.parseInt(hex.replaceFirst("^#", ""), 16);
            result = (alpha << 24) | rgb;
        } else {
            result = 0xFFFFFFFF;
            if (mc.player != null)
                mc.player.sendOverlayMessage(Component.translatable("pda.configuration.render_text_color.error", hex).withColor(TextColor.RED));
        }

        return result;
    }

    public static double getSize() {
        return PDAClientConfig.LIST_TEXT_SIZE.get() / 100.0;
    }

    @Override
    public void render(@NonNull GuiGraphicsExtractor guiGraphics, @NonNull DeltaTracker deltaTracker) {
        if (mc.player == null || mc.level == null || mc.gui.hud.isHidden() || mc.debugEntries.isOverlayVisible())
            return;

        Set<InfoDisplayHandler> handlers = InfoDisplayManager.getActiveHandlers();
        List<InfoDisplayHandler> sorted = handlers.stream().filter(h -> PDAClientConfig.getVisibility(h.id)).sorted(Comparator.comparingInt(InfoDisplayHandler::getPriority)).toList();

        List<Component> lines = new ArrayList<>();
        for (InfoDisplayHandler handler : sorted) {
            try {
                Component text = handler.getDisplayText();
                if (!text.getString().isEmpty()) lines.add(text);
            } catch (Exception e) {
                PDAClient.LOGGER.error("Error resolving info display text", e);
            }
        }
        if (lines.isEmpty()) return;

        RenderTextVerticalAlignment direction = getDirection();
        double scale = getSize();
        int anchorY = getY(guiGraphics);
        int totalHeight = (int) (LINE_HEIGHT * lines.size() * getSize());

        int y;
        int step;
        if (direction == RenderTextVerticalAlignment.TOP_TO_BOTTOM) {
            y = anchorY;
            step = LINE_HEIGHT;
        } else if (direction == RenderTextVerticalAlignment.BOTTOM_TO_TOP) {
            y = anchorY - LINE_HEIGHT;
            step = -LINE_HEIGHT;
        } else {
            y = anchorY - totalHeight / 2;
            step = LINE_HEIGHT;
        }

        for (Component text : lines) {
            try {
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().scale((float) scale);
                switch (getRenderAlignment()) {
                    case LEFT_TO_RIGHT -> drawLeftToRight(guiGraphics, text, y, scale);
                    case RIGHT_TO_LEFT -> drawRightToLeft(guiGraphics, text, y, scale);
                    case CENTERED -> drawCentered(guiGraphics, text, y, scale);
                }
                guiGraphics.pose().popMatrix();
            } catch (Exception e) {
                PDAClient.LOGGER.error("Error rendering info display", e);
            }

            y += (int) (step * getSize());
        }
    }

    private static int unscale(int value, double scale) {
        return Math.round((float) (value / scale));
    }

    private static void drawLeftToRight(GuiGraphicsExtractor graphics, Component text, int y, double scale) {
        int x = unscale(getX(graphics), scale);
        int localY = unscale(y, scale);
        graphics.text(mc.font, text, x, localY, getColor(), getDropShadow());
    }

    private static void drawCentered(GuiGraphicsExtractor graphics, @NonNull Component text, int y, double scale) {
        FormattedCharSequence toRender = text.getVisualOrderText();
        int x = unscale(getX(graphics), scale) - mc.font.width(toRender) / 2;
        int localY = unscale(y, scale);
        graphics.text(mc.font, toRender, x, localY, getColor(), getDropShadow());
    }

    private static void drawRightToLeft(GuiGraphicsExtractor graphics, @NonNull Component text, int y, double scale) {
        FormattedCharSequence toRender = text.getVisualOrderText();
        int x = unscale(getX(graphics), scale) - mc.font.width(toRender);
        int localY = unscale(y, scale);
        graphics.text(mc.font, toRender, x, localY, getColor(), getDropShadow());
    }

}