package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionDisplayHandler extends InfoDisplayHandler {

    private static final Vec3 ZERO = new Vec3(0, 0, 0);

    public static String formatPattern(String input, int x, int y, int z) {
        Pattern pattern = Pattern.compile("(?<!\\\\)%([xyz])");
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String replacement = switch (matcher.group(1)) {
                case "x" -> Integer.toString(x);
                case "y" -> Integer.toString(y);
                case "z" -> Integer.toString(z);
                default -> matcher.group();
            };
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString().replace("\\%", "%");
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return Component.empty();

        Vec3 pos = player.position();
        boolean hasCompass = player.getInventory().contains(Items.COMPASS.getDefaultInstance());
        boolean hasDepthMeter = player.getInventory().contains(ModItems.DEPTH_METER.toStack());
        boolean hasGPS = player.getInventory().contains(ModItems.GPS.toStack());

        List<String> parts = new ArrayList<>();
        if (hasGPS || hasCompass) parts.add(String.format("%.1f", pos.x));
        if (hasGPS || hasDepthMeter) parts.add(String.format("%.1f", pos.y));
        if (hasGPS || hasCompass) parts.add(String.format("%.1f", pos.z));

        return Component.literal(String.join(", ", parts));
    }

}
