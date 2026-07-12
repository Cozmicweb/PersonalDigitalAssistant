package cozmicweb.pda.client.event;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = PDACommon.MOD_ID, value = Dist.CLIENT)
public class TooltipEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<InfoDisplayHandler> handlers = InfoDisplayManager.getHandlersFor(stack);

        if (handlers.isEmpty()) {
            return;
        }

        List<String> behaviors = handlers.stream()
                .map(InfoDisplayHandler::getBehavior)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (behaviors.isEmpty()) {
            return;
        }

        for (int i = 0; i < behaviors.size(); i += 4) {
            int end = Math.min(i + 4, behaviors.size());
            String line = String.join(", ", behaviors.subList(i, end));
            if (i == 0) {
                line = "Displays " + line;
            }
            if (end < behaviors.size()) {
                line += ",";
            }
            event.getToolTip().add(Component.literal(line).withColor(0x8A8E92));
        }
    }
}
