package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.client.PDAClientConfig;
import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.data.RareMobEntry;
import cozmicweb.pda.common.registry.ModSounds;
import io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class RareMobDisplayHandler extends InfoDisplayHandler {

    private static long lastCheck = System.currentTimeMillis();
    private static Component lastText = Component.empty();
    private static String lastMob = "";

    public RareMobDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public String getBehavior() {
        return "rare mobs";
    }

    public int getSize() {
        return PDAConfig.RADAR_SIZE.get();
    }

    @Override
    public int getDefaultPriority() {
        return 500;
    }

    @Override
    public int getUpdateInterval() {
        return PDAConfig.RADAR_UPDATE_RATE.get();
    }

    @Override
    public Component getDisplayText() {
        long now = System.currentTimeMillis();
        if (now - lastCheck < getUpdateInterval() * 1000L) return lastText;
        lastCheck = now;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return Component.empty();

        Vec3 position = player.position();
        Level level = player.level();
        int size = getSize();

        List<LivingEntity> rareMobs = level.getEntitiesOfClass(
                LivingEntity.class,
                AABB.ofSize(position, size, size, size),
                entity -> RareMobEntry.getRareMobEntry(entity).isPresent());
        rareMobs.sort(Comparator.comparingDouble(a -> a.distanceToSqr(position)));

        if (rareMobs.isEmpty()) {
            lastText = Component.translatable("text.pda.lifeform_analyzer.nothing");
            return lastText;
        }

        LivingEntity rare = rareMobs.getFirst();
        if (PDAClientConfig.LIFEFORM_ANALYZER_SOUND.get() && !lastMob.equals(rare.getStringUUID()))
            player.playSound(ModSounds.RADAR_BEEP.value(), 0.3F, 2.0F);

        lastMob = rare.getStringUUID();
        lastText = RareMobEntry.getRareMobEntry(rare).map(e -> e.getName(rare)).orElse(rare.getDisplayName());
        return lastText;
    }

}
