package cozmicweb.pda.common.registry;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.content.raider_army.BattleStandardType;
import cozmicweb.pda.common.item.BattleStandardItem;
import cozmicweb.pda.common.item.StopwatchItem;
import cozmicweb.pda.common.item.TallyCounterItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PDACommon.MOD_ID);

    public static final DeferredItem<Item> CRT_TV = ITEMS.registerItem("crt_tv", Item::new, props -> props.stacksTo(1).rarity(ModRarities.LIGHT_RED.getValue()));
    public static final DeferredItem<Item> TALLY_COUNTER = ITEMS.registerItem("tally_counter", TallyCounterItem::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> STOPWATCH = ITEMS.registerItem("stopwatch", StopwatchItem::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> WEATHER_RADIO = ITEMS.registerItem("weather_radio", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> SEXTANT = ITEMS.registerItem("sextant", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> RADAR = ITEMS.registerItem("radar", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> LIFEFORM_ANALYZER = ITEMS.registerItem("lifeform_analyzer", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> DPS_METER = ITEMS.registerItem("dps_meter", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> DEPTH_METER = ITEMS.registerItem("depth_meter", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> FISHERMANS_POCKET_GUIDE = ITEMS.registerItem("fishermans_pocket_guide", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> METAL_DETECTOR = ITEMS.registerItem("metal_detector", Item::new, props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));

    public static final DeferredItem<Item> REK_3000 = ITEMS.registerItem("rek_3000", Item::new, props -> props.stacksTo(1).rarity(ModRarities.ORANGE.getValue()));
    public static final DeferredItem<Item> GPS = ITEMS.registerItem("gps", Item::new, props -> props.stacksTo(1).rarity(ModRarities.ORANGE.getValue()));
    public static final DeferredItem<Item> FISH_FINDER = ITEMS.registerItem("fish_finder", Item::new, props -> props.stacksTo(1).rarity(ModRarities.ORANGE.getValue()));
    public static final DeferredItem<Item> PILLAGER_TECH = ITEMS.registerItem("pillager_tech", Item::new, props -> props.stacksTo(1).rarity(ModRarities.ORANGE.getValue()));

    public static final DeferredItem<Item> PDA = ITEMS.registerItem("pda", Item::new, props -> props.stacksTo(1).rarity(ModRarities.PINK.getValue()));

    public static final DeferredItem<Item> EMPTY_BATTLE_STANDARD = ITEMS.registerItem("battle_standard_empty", Item::new, props -> props.stacksTo(16));
    public static final DeferredItem<Item> OMINOUS_BATTLE_STANDARD = ITEMS.registerItem("battle_standard_ominous", props -> new BattleStandardItem(props, BattleStandardType.OMINOUS), props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> WARDING_BATTLE_STANDARD = ITEMS.registerItem("battle_standard_warding", props -> new BattleStandardItem(props, BattleStandardType.WARDING), props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()));
    public static final DeferredItem<Item> REVEALING_BATTLE_STANDARD = ITEMS.registerItem("battle_standard_revealing", props -> new BattleStandardItem(props, BattleStandardType.REVEALING), props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()).durability(10));
    public static final DeferredItem<Item> GATHERING_BATTLE_STANDARD = ITEMS.registerItem("battle_standard_gathering", props -> new BattleStandardItem(props, BattleStandardType.GATHERING), props -> props.stacksTo(1).rarity(ModRarities.BLUE.getValue()).durability(10));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}