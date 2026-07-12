package cozmicweb.pda.common.network;

import cozmicweb.pda.common.IClickReactive;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.item.TallyCounterItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // Send Kill Stats
        registrar.playToServer(
                ServerDataRequestPayload.TYPE,
                ServerDataRequestPayload.STREAM_CODEC,
                (payload, context) -> {
                    List<ServerDataResponsePayload.Response> responses = new ArrayList<>();
                    for (ServerDataRequestPayload.Request request : payload.requests()) {
                        InfoDisplayHandler handler = InfoDisplayManager.get(request.handlerId());
                        if (handler != null) {
                            Object[] data = handler.handleServerRequest(context.player(), request.params().toArray());
                            responses.add(new ServerDataResponsePayload.Response(request.handlerId(), List.of(data)));
                        }
                    }
                    if (!responses.isEmpty()) {
                        context.reply(new ServerDataResponsePayload(responses));
                    }
                }
        );
        registrar.playToClient(
                ServerDataResponsePayload.TYPE,
                ServerDataResponsePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    for (ServerDataResponsePayload.Response response : payload.responses()) {
                        InfoDisplayHandler handler = InfoDisplayManager.get(response.handlerId());
                        if (handler != null) {
                            handler.updateServerData(response.data().toArray());
                        }
                    }
                })
        );

        // Click States
        registrar.playToServer(
                ClickStatePayload.TYPE,
                ClickStatePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) context.player();
                    ItemStack held = player.getMainHandItem();

                    if (!(held.getItem() instanceof IClickReactive reactive)) return;

                    if (payload.isAttack()) {
                        if (payload.isPressed()) reactive.onLeftClickDown(player, held);
                        else reactive.onLeftClickUp(player, held);
                    } else {
                        if (payload.isPressed()) reactive.onRightClickDown(player, held);
                        else reactive.onRightClickUp(player, held);
                    }
                })
        );

    }
}
