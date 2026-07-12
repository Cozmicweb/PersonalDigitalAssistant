package cozmicweb.pda.common.display;

import cozmicweb.pda.common.registry.ModDataMaps;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.display.handlers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class InfoDisplayManager {
    private static final Map<Identifier, InfoDisplayHandler> HANDLERS = new HashMap<>();

    public static void initialize() {
        register(PDACommon.id("display_time"), new TimeDisplayHandler());
        register(PDACommon.id("display_kills"), new KillsDisplayHandler());
        register(PDACommon.id("display_position"), new PositionDisplayHandler());
        register(PDACommon.id("display_velocity"), new VelocityDisplayHandler());
        register(PDACommon.id("display_weather"), new WeatherDisplayHandler());
        register(PDACommon.id("display_moon_phase"), new MoonPhaseDisplayHandler());
        register(PDACommon.id("display_monsters"), new MonstersDisplayHandler());
        register(PDACommon.id("display_rare"), new RareMobDisplayHandler());
        register(PDACommon.id("display_dps"), new DPSDisplayHandler());
    }

    public static void register(Identifier id, InfoDisplayHandler logic) {
        if (HANDLERS.putIfAbsent(id, logic) != null)
            throw new IllegalStateException("Duplicate display handler: " + id);
    }

    public static Set<InfoDisplayHandler> getActiveHandlers() {
        Set<InfoDisplayHandler> active = new LinkedHashSet<>();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return active;

        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack.isEmpty()) continue;
            active.addAll(getHandlersFor(stack));
        }
        return active;
    }

    public static boolean isHandlerActive(String id) {
        Identifier identifier = PDACommon.id(id);
        InfoDisplayHandler handler = get(identifier);
        if (handler == null) return false;
        return getActiveHandlers().contains(handler);
    }

    @Nullable
    public static Identifier getIdFor(InfoDisplayHandler handler) {
        for (Map.Entry<Identifier, InfoDisplayHandler> entry : HANDLERS.entrySet()) {
            if (entry.getValue() == handler) return entry.getKey();
        }
        return null;
    }

    @Nullable
    public static InfoDisplayHandler get(Identifier id) {
        return HANDLERS.get(id);
    }

    @NonNull
    public static List<InfoDisplayHandler> getHandlersFor(@NonNull ItemStack stack) {
        List<Identifier> ids = stack.getData(ModDataMaps.HANDLERS);
        if (ids == null) return Collections.emptyList();
        List<InfoDisplayHandler> handlers = new ArrayList<>();
        for (Identifier id : ids) {
            InfoDisplayHandler handler = InfoDisplayManager.get(id);
            if (handler != null) handlers.add(handler);
        }
        return handlers;
    }

}
