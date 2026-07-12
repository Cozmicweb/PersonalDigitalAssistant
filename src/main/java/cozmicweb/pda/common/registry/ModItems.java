package cozmicweb.pda.common.registry;

import com.ibm.icu.impl.Trie;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.data.RareMobData;
import cozmicweb.pda.common.data.RareMobEntry;
import cozmicweb.pda.common.display.handlers.RareMobDisplayHandler;
import cozmicweb.pda.common.item.StopwatchItem;
import cozmicweb.pda.common.item.TallyCounterItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PDACommon.MOD_ID);

    public static final DeferredItem<Item> TALLY_COUNTER = ITEMS.registerItem("tally_counter", TallyCounterItem::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> STOPWATCH = ITEMS.registerItem("stopwatch", StopwatchItem::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> WEATHER_RADIO = ITEMS.registerItem("weather_radio", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> SEXTANT = ITEMS.registerItem("sextant", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> RADAR = ITEMS.registerItem("radar", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> LIFEFORM_ANALYZER = ITEMS.registerItem("lifeform_analyzer", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> DPS_METER = ITEMS.registerItem("dps_meter", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> DEPTH_METER = ITEMS.registerItem("depth_meter", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> FISHERMANS_POCKET_GUIDE = ITEMS.registerItem("fishermans_pocket_guide", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> METAL_DETECTOR = ITEMS.registerItem("metal_detector", Item::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> REK_3000 = ITEMS.registerItem("rek_3000", Item::new, props -> props.stacksTo(1).rarity(Rarity.RARE));
    public static final DeferredItem<Item> GPS = ITEMS.registerItem("gps", Item::new, props -> props.stacksTo(1).rarity(Rarity.RARE));
    public static final DeferredItem<Item> FISH_FINDER = ITEMS.registerItem("fish_finder", Item::new, props -> props.stacksTo(1).rarity(Rarity.RARE));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}