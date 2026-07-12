package cozmicweb.pda.common.event;

import cozmicweb.pda.common.ModAttachments;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;
import cozmicweb.pda.common.item.TallyCounterItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class MobKillEventListener {

    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();

        if (killer instanceof ServerPlayer player) {
            Identifier victimType = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());
            String victimTypeId = victimType.toString();

            player.setData(ModAttachments.LAST_ENTITY_TYPE_KILLED, victimTypeId);
            if (InfoDisplayManager.isHandlerActive("display_kills"))
                TallyCounterItem.tick(player);
        }
    }

}