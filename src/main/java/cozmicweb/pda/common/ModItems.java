package cozmicweb.pda.common;

import cozmicweb.pda.common.item.TallyCounterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PDACommon.MOD_ID);

    public static final DeferredItem<Item> TALLY_COUNTER = ITEMS.registerItem("tally_counter", TallyCounterItem::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> STOPWATCH = ITEMS.registerItem("stopwatch", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}