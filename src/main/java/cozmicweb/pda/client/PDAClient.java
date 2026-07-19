package cozmicweb.pda.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import cozmicweb.pda.client.gui.InfoOverlay;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.content.information_display.InfoDisplayManager;
import cozmicweb.pda.common.content.information_display.handlers.InfoDisplayHandler;
import cozmicweb.pda.common.network.ServerDataRequestPayload;
import cozmicweb.pda.datagen.item.property.MechanismPressedProperty;
import cozmicweb.pda.datagen.item.property.NeedleRotationProperty;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mod(value = PDACommon.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PDACommon.MOD_ID, value = Dist.CLIENT)
public class PDAClient {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static int tickCounter = 0;

    public static final KeyMapping.Category PDA_CATEGORY = new KeyMapping.Category(PDACommon.id("category"));
    public static final Lazy<KeyMapping> VIEW_INFO_MAPPING = Lazy.of(
            () -> new KeyMapping(
                    "key.pda.view_info",
                    KeyConflictContext.GUI,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_CONTROL,
                    PDA_CATEGORY));

    public PDAClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.registerConfig(ModConfig.Type.CLIENT, PDAClientConfig.SPEC);
    }

    @SubscribeEvent
    public static void addPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES)
            return;

        event.addPackFinders(
                PDACommon.id("resourcepacks/terraria_styled_info"),
                PackType.CLIENT_RESOURCES,
                Component.literal("Terraria Styled Translations"),
                PackSource.DEFAULT,
                false,
                Pack.Position.TOP
        );
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        Set<InfoDisplayHandler> handlers = InfoDisplayManager.getActiveHandlers();
        List<ServerDataRequestPayload.Request> requests = new ArrayList<>();

        for (InfoDisplayHandler handler : handlers) {
            if (handler.requiresServerSync()) {
                if (tickCounter % handler.getUpdateInterval() == 0) {
                    Identifier id = InfoDisplayManager.getIdFor(handler);
                    if (id != null) {
                        requests.add(new ServerDataRequestPayload.Request(id, List.of(handler.getServerDataParameters())));
                    }
                }
            }
        }

        if (!requests.isEmpty()) {
            player.connection.send(new ServerDataRequestPayload(requests));
        }
        tickCounter++;
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.BOSS_OVERLAY, PDACommon.id("information_overlay"), new InfoOverlay());
    }

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.registerCategory(PDA_CATEGORY);
        event.register(VIEW_INFO_MAPPING.get());
    }

    @SubscribeEvent
    public static void onRegisterSelectItemModelProperty(RegisterSelectItemModelPropertyEvent event) {
        event.register(PDACommon.id("needle_rotation"), NeedleRotationProperty.TYPE);
    }

    @SubscribeEvent
    public static void onRegisterConditionalItemModelProperty(RegisterConditionalItemModelPropertyEvent event) {
        event.register(PDACommon.id("mechanism_pressed"), MechanismPressedProperty.MAP_CODEC);
    }

}
