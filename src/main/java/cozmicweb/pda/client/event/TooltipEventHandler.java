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

        List<Component> behaviors = handlers.stream()
                .map(InfoDisplayHandler::getBehavior)
                .filter(c -> !c.equals(Component.empty()))
                .collect(Collectors.toList());

        if (behaviors.isEmpty()) {
            return;
        }

        for (int i = 0; i < behaviors.size(); i += 4) {
            int end = Math.min(i + 4, behaviors.size());
            List<Component> subList = behaviors.subList(i, end);
            
            Component line;
            if (i == 0) {
                line = Component.translatable("text.pda.tooltip.displays");
            } else {
                line = Component.empty();
            }

            for (int j = 0; j < subList.size(); j++) {
                line = line.copy().append(subList.get(j));
                if (j < subList.size() - 1 || end < behaviors.size()) {
                    line = line.copy().append(Component.literal(", "));
                }
            }
            
            event.getToolTip().add(line.copy().withColor(0x8A8E92));
        }
    }
}
