package cozmicweb.pda.common.registry;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.item.StopwatchItem;
import cozmicweb.pda.common.item.TallyCounterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PDACommon.MOD_ID);

    public static final DeferredItem<Item> TALLY_COUNTER = ITEMS.registerItem("tally_counter", TallyCounterItem::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> STOPWATCH = ITEMS.registerItem("stopwatch", StopwatchItem::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> WEATHER_RADIO = ITEMS.registerItem("weather_radio", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> SEXTANT = ITEMS.registerItem("sextant", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}