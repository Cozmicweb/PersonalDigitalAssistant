package cozmicweb.pda.common;

import com.mojang.logging.LogUtils;
import cozmicweb.pda.common.content.information_display.InfoDisplayManager;
import cozmicweb.pda.common.registry.*;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
@Mod(PDACommon.MOD_ID)
public class PDACommon {
    public static final String MOD_ID = "pda";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PDACommon(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, PDAConfig.SPEC);

        ModItems.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModComponents.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModLootFunctions.register(modEventBus);
        ModGlobalLootModifiers.register(modEventBus);
        ModLootConditions.register(modEventBus);
        ModGameRules.register(modEventBus);
        InfoDisplayManager.initialize();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {

    }

    @Contract("_ -> new")
    public static @NonNull Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

}
