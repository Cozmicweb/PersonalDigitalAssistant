package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.common.PDAConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MonstersDisplayHandler extends InfoDisplayHandler {

    private static long lastCheck = System.currentTimeMillis();
    private static Component lastText = Component.empty();

    public MonstersDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.monsters");
    }

    public int getSize() {
        return PDAConfig.RADAR_SIZE.get();
    }

    @Override
    public int getDefaultPriority() {
        return 600;
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

        List<Monster> monsters = level.getEntitiesOfClass(Monster.class, AABB.ofSize(position, size, size, size));

        lastText = Component.translatable("text.pda.radar.detected_nearby", monsters.size());
        return lastText;
    }

}
