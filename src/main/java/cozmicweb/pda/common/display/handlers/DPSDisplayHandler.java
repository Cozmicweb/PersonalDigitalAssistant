package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.attachments.DamageTracker;
import cozmicweb.pda.common.registry.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

public class DPSDisplayHandler extends InfoDisplayHandler {

    private static final String DPS_KEY = "dps";

    @Override
    public boolean requiresServerSync() {
        return true;
    }

    @Override
    public int getUpdateInterval() {
        return PDAConfig.RADAR_UPDATE_RATE.get();
    }

    @Override
    public void updateServerData(Object[] data) {
        if (data.length > 0 && data[0] instanceof Float dps) {
            serverData.put(DPS_KEY, dps);
        }
    }

    @Override
    public Object[] handleServerRequest(Player player, Object[] params) {
        DamageTracker tracker = player.getData(ModAttachments.DAMAGE_TRACKER);
        float sum = tracker.getSum(player.level().getGameTime());
        float windowSize = DamageTracker.getWindowSize();
        float dps = windowSize > 0 ? sum / (windowSize / 20f) : 0;
        return vararg(dps);
    }

    @Override
    public Component getDisplayText() {
        float dps = (float) serverData.getOrDefault(DPS_KEY, 0.0f);
        return Component.literal(String.format(Locale.ROOT, "DPS: %.1f", dps));
    }

}
