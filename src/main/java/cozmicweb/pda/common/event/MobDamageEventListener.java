package cozmicweb.pda.common.event;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.item.TallyCounterItem;
import cozmicweb.pda.common.attachments.DamageTracker;
import cozmicweb.pda.common.registry.ModAttachments;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class MobDamageEventListener {

    @SubscribeEvent
    public static void onMobDamaged(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();
        float damage = event.getInflictedDamage();

        if (source.getEntity() instanceof Player player) {
            DamageTracker tracker = player.getData(ModAttachments.DAMAGE_TRACKER);
            player.setData(ModAttachments.DAMAGE_TRACKER, tracker.addDamage(player.level().getGameTime(), damage));
        }
    }

}