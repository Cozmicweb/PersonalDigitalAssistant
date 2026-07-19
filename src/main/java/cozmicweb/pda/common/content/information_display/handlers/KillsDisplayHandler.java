package cozmicweb.pda.common.content.information_display.handlers;

import cozmicweb.pda.common.registry.ModAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class KillsDisplayHandler extends InfoDisplayHandler {

    private static final String LAST_KILLED_ENTITY_KEY = "last_killed_entity";
    private static final String LAST_KILL_COUNT_KEY = "last_kill_count";

    public KillsDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public Component getBehavior() {
        return Component.translatable("pda.behavior.kills");
    }

    @Override
    public int getDefaultPriority() {
        return 700;
    }

    @Override
    public boolean requiresServerSync() {
        return true;
    }

    @Override
    public void updateServerData(Object[] data) {
        if (data.length > 0 && data[0] instanceof String entityType) {
            serverData.put(LAST_KILLED_ENTITY_KEY, entityType);
        }

        if (data.length > 1 && data[1] instanceof Integer killCount) {
            serverData.put(LAST_KILL_COUNT_KEY, killCount);
        }
    }

    @Override
    public Object[] handleServerRequest(Player player, Object[] params) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return vararg("minecraft:zombie", 0);

        String latestKilledEntity = serverPlayer.getData(ModAttachments.LAST_ENTITY_TYPE_KILLED);
        Identifier id = Identifier.tryParse(latestKilledEntity);

        EntityType<?> type = id == null
                ? EntityType.ZOMBIE
                : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(EntityType.ZOMBIE);

        Identifier resolvedId = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        int killCount = serverPlayer.getStats().getValue(Stats.ENTITY_KILLED.get(type));

        return vararg(resolvedId.toString(), killCount);
    }

    @Override
    public Component getDisplayText() {
        String entityType = (String) serverData.getOrDefault(LAST_KILLED_ENTITY_KEY, "minecraft:zombie");
        int killCount = (int) serverData.getOrDefault(LAST_KILL_COUNT_KEY, 0);
        Component translation = BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.parse(entityType))
                .map(EntityType::getDescription)
                .orElse(EntityType.ZOMBIE.getDescription());
        return Component.translatable("text.pda.kills.text", translation, killCount);
    }

}
