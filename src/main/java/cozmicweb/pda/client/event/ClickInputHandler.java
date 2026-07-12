package cozmicweb.pda.client.event;

import cozmicweb.pda.common.IClickReactive;
import cozmicweb.pda.common.network.ClickStatePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = "pda", value = Dist.CLIENT)
public class ClickInputHandler {
    private static boolean wasAttackDown = false;
    private static boolean wasUseDown = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack held = mc.player.getMainHandItem();
        if (!(held.getItem() instanceof IClickReactive)) {
            wasAttackDown = mc.options.keyAttack.isDown();
            wasUseDown = mc.options.keyUse.isDown();
            return;
        }

        boolean attackDown = mc.options.keyAttack.isDown();
        boolean useDown = mc.options.keyUse.isDown();

        if (attackDown != wasAttackDown)
            ClientPacketDistributor.sendToServer(new ClickStatePayload(true, attackDown));
        if (useDown != wasUseDown)
            ClientPacketDistributor.sendToServer(new ClickStatePayload(false, useDown));

        wasAttackDown = attackDown;
        wasUseDown = useDown;
    }
}