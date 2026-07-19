package cozmicweb.pda.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import cozmicweb.pda.client.PDAClient;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.content.information_display.InfoDisplayManager;
import cozmicweb.pda.common.content.information_display.handlers.InfoDisplayHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(modid = PDACommon.MOD_ID, value = Dist.CLIENT)
public class TooltipEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        List<InfoDisplayHandler> handlers = InfoDisplayManager.getHandlersFor(stack);
        if (handlers.isEmpty())
            return;
        handlers.sort(Comparator.comparingDouble(InfoDisplayHandler::getPriority));

        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), PDAClient.VIEW_INFO_MAPPING.get().getKey().getValue())) {
            Component keyComponent = PDAClient.VIEW_INFO_MAPPING.get().getTranslatedKeyMessage();
            Component tooltip = Component.empty()
                    .append(Component.translatable("tooltip.pda.more_info.prefix").withStyle(ChatFormatting.DARK_GRAY))
                    .append(keyComponent.copy().withStyle(ChatFormatting.GRAY))
                    .append(Component.translatable("tooltip.pda.more_info.suffix").withStyle(ChatFormatting.DARK_GRAY));
            event.getToolTip().add(tooltip);
        } else {
            Component keyComponent = PDAClient.VIEW_INFO_MAPPING.get().getTranslatedKeyMessage();
            Component tooltip = Component.empty()
                    .append(Component.translatable("tooltip.pda.more_info.prefix").withStyle(ChatFormatting.DARK_GRAY))
                    .append(keyComponent.copy().withStyle(ChatFormatting.WHITE))
                    .append(Component.translatable("tooltip.pda.more_info.suffix").withStyle(ChatFormatting.DARK_GRAY));
            event.getToolTip().add(tooltip);
            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("tooltip.pda.header").withStyle(ChatFormatting.GRAY));

            for (InfoDisplayHandler handler : handlers) {
                Component behavior = handler.getBehavior();
                if (!behavior.getString().isEmpty()) {
                    ChatFormatting color = handler.isActive() ? ChatFormatting.WHITE : ChatFormatting.DARK_GRAY;
                    event.getToolTip().add(Component.literal(" ⏺ ").append(behavior).withStyle(color));
                }
            }
        }
    }
}
