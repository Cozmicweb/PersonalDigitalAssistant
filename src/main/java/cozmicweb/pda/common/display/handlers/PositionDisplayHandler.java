package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

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

    public static @NonNull String getPattern() {
        return PDAClientConfig.COMPASS_FORMAT.get();
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        Vec3 playerPos = player == null ? ZERO : player.position();
        return Component.literal(formatPattern(getPattern(), (int) playerPos.x, (int) playerPos.y, (int) playerPos.z));
    }

}
