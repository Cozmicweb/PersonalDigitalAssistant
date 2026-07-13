package cozmicweb.pda.common.display;

import cozmicweb.pda.client.PDAClientConfig;
import cozmicweb.pda.common.registry.ModDataMaps;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.display.handlers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.NonNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;

public class InfoDisplayManager {
    private static final Map<Identifier, InfoDisplayHandler> HANDLERS = new HashMap<>();

    public static void initialize() {
        register(PDACommon.id("display_time"), TimeDisplayHandler.class);
        register(PDACommon.id("display_mob_kills"), KillsDisplayHandler.class);
        register(PDACommon.id("display_horizontal_position"), HorizontalPositionDisplayHandler.class);
        register(PDACommon.id("display_vertical_position"), VerticalPositionDisplayHandler.class);
        register(PDACommon.id("display_velocity"), VelocityDisplayHandler.class);
        register(PDACommon.id("display_weather"), WeatherDisplayHandler.class);
        register(PDACommon.id("display_moon_phase"), MoonPhaseDisplayHandler.class);
        register(PDACommon.id("display_nearby_monsters"), MonstersDisplayHandler.class);
        register(PDACommon.id("display_rare_mob"), RareMobDisplayHandler.class);
        register(PDACommon.id("display_dps"), DPSDisplayHandler.class);
        register(PDACommon.id("display_luck"), LuckDisplayHandler.class);
        register(PDACommon.id("display_valuable_ore"), OreDisplayHandler.class);
    }

    public static <T extends InfoDisplayHandler> void register(Identifier id, Class<T> clazz) {
        T handler = null;

        try {
            handler = clazz.getDeclaredConstructor(Identifier.class).newInstance(id);
        } catch (Exception e) {
            PDACommon.LOGGER.error("Failed to register display handler: {}", id, e);
        }

        if (handler != null && HANDLERS.putIfAbsent(id, handler) != null)
            throw new IllegalStateException("Duplicate display handler: " + id);
    }

    public static Set<InfoDisplayHandler> getActiveHandlers() {
        Set<InfoDisplayHandler> active = new LinkedHashSet<>();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return active;
        boolean clientSide = player.level().isClientSide();

        // Vanilla Inventory
        player.getInventory().getNonEquipmentItems().forEach(stack -> collectHandlers(stack, clientSide, active));

        // Curios Inventory
        CuriosApi.getCuriosInventory(player).flatMap(curios -> curios.getStacksHandler("information_accessories")).ifPresent(stacksHandler -> {
            IDynamicStackHandler stacks = stacksHandler.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                collectHandlers(stacks.getStackInSlot(i), clientSide, active);
            }
        });

        return active;
    }

    private static void collectHandlers(@NonNull ItemStack stack, boolean clientSide, Set<InfoDisplayHandler> active) {
        if (stack.isEmpty()) return;
        List<InfoDisplayHandler> handlers = getHandlersFor(stack);

        if (clientSide)
            handlers.stream().filter(h -> PDAClientConfig.getVisibility(h.id)).forEach(active::add);
        else
            active.addAll(handlers);
    }

    public static boolean isHandlerActive(String id) {
        Identifier identifier = PDACommon.id(id);
        InfoDisplayHandler handler = get(identifier);
        if (handler == null) return false;
        boolean visible = true;
        Level level = Minecraft.getInstance().level;
        if (level != null && level.isClientSide())
            visible = PDAClientConfig.getVisibility(identifier);
        return getActiveHandlers().contains(handler) && visible;
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

    @Contract(pure = true)
    public static @NonNull @UnmodifiableView Map<Identifier, InfoDisplayHandler> getAllHandlers() {
        return Collections.unmodifiableMap(HANDLERS);
    }

}
