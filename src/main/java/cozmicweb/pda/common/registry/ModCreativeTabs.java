package cozmicweb.pda.common.registry;

import cozmicweb.pda.common.PDACommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PDACommon.MOD_ID);

    public static final Supplier<CreativeModeTab> PDA_TAB = CREATIVE_TABS.register("pda_tab",() ->
        CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.pda.pda_tab"))
                .icon(() -> ModItems.PDA.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.accept(Items.COMPASS);
                    output.accept(Items.CLOCK);
                    output.accept(ModItems.CRT_TV.get());
                    output.accept(ModItems.TALLY_COUNTER.get());
                    output.accept(ModItems.STOPWATCH.get());
                    output.accept(ModItems.WEATHER_RADIO.get());
                    output.accept(ModItems.SEXTANT.get());
                    output.accept(ModItems.RADAR.get());
                    output.accept(ModItems.LIFEFORM_ANALYZER.get());
                    output.accept(ModItems.DPS_METER.get());
                    output.accept(ModItems.DEPTH_METER.get());
                    output.accept(ModItems.FISHERMANS_POCKET_GUIDE.get());
                    output.accept(ModItems.METAL_DETECTOR.get());
                    output.accept(ModItems.REK_3000.get());
                    output.accept(ModItems.GPS.get());
                    output.accept(ModItems.FISH_FINDER.get());
                    output.accept(ModItems.PILLAGER_TECH.get());
                    output.accept(ModItems.PDA.get());
                    output.accept(ModItems.EMPTY_BATTLE_STANDARD.get());
                    output.accept(ModItems.OMINOUS_BATTLE_STANDARD.get());
                    output.accept(ModItems.WARDING_BATTLE_STANDARD.get());
                })
                .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }

}
