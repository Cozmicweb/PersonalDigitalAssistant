package cozmicweb.pda.client;

import com.mojang.logging.LogUtils;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.client.gui.InfoOverlay;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.network.ServerDataRequestPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mod(value = PDACommon.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PDACommon.MOD_ID, value = Dist.CLIENT)
public class PDAClient {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static int tickCounter = 0;

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (tickCounter++ % 20 == 0) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            Set<InfoDisplayHandler> handlers = InfoDisplayManager.getActiveHandlers();
            List<ServerDataRequestPayload.Request> requests = new ArrayList<>();

            for (InfoDisplayHandler handler : handlers) {
                if (handler.requiresServerSync()) {
                    Identifier id = InfoDisplayManager.getIdFor(handler);
                    if (id != null) {
                        requests.add(new ServerDataRequestPayload.Request(id, List.of(handler.getServerDataParameters())));
                    }
                }
            }

            if (!requests.isEmpty()) {
                player.connection.send(new ServerDataRequestPayload(requests));
            }
        }
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.BOSS_OVERLAY, PDACommon.id("information_overlay"), new InfoOverlay());
    }

}
