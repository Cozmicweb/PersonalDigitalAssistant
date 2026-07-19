package cozmicweb.pda.common;

import com.mojang.logging.LogUtils;
import cozmicweb.pda.common.content.information_display.InfoDisplayManager;
import cozmicweb.pda.common.registry.*;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosSlotTypes;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
@Mod(PDACommon.MOD_ID)
public class PDACommon {
    public static final String MOD_ID = "pda";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PDACommon(IEventBus modEventBus, ModContainer container) {
        initCompat();

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

    private void initCompat() {
        PDACompat.curiosLoaded = ModList.get().isLoaded("curios");

        if (PDACompat.curiosLoaded)
            CuriosSlotTypes.registerPredicate(PDACommon.id("all_information_accessories"), (_, stack) -> stack.is(ModTags.ALL_INFORMATION_ACCESSORIES));
    }

    @Contract("_ -> new")
    public static @NonNull Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

}
