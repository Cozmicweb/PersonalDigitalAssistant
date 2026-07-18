package cozmicweb.pda.datagen.loot.sub;

import cozmicweb.pda.common.content.raider_army.raiders.RaiderGroup;
import cozmicweb.pda.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

public record EntityLootSubProvider(HolderLookup.Provider registries) implements LootTableSubProvider {

    public static final ResourceKey<LootTable> GOBLIN_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.GOBLIN.lootId());
    public static final ResourceKey<LootTable> WARRIOR_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.WARRIOR.lootId());
    public static final ResourceKey<LootTable> BRUTE_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.BRUTE.lootId());

    public static final ResourceKey<LootTable> ARCHER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.ARCHER.lootId());
    public static final ResourceKey<LootTable> PILLAGER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.PILLAGER.lootId());
    public static final ResourceKey<LootTable> SNIPER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.SNIPER.lootId());

    public static final ResourceKey<LootTable> BABY_RAVAGER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.BABY_RAVAGER.lootId());
    public static final ResourceKey<LootTable> RAVAGER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.RAVAGER.lootId());
    public static final ResourceKey<LootTable> WARLORD_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.WARLORD.lootId());

    public static final ResourceKey<LootTable> WITCH_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.WITCH.lootId());
    public static final ResourceKey<LootTable> SHAMAN_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.SHAMAN.lootId());

    public static final ResourceKey<LootTable> EVOKER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.EVOKER.lootId());
    public static final ResourceKey<LootTable> INVOKER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.INVOKER.lootId());

    public static final ResourceKey<LootTable> ILLUSIONER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.ILLUSIONER.lootId());
    public static final ResourceKey<LootTable> MAGICIAN_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.MAGICIAN.lootId());
    public static final ResourceKey<LootTable> WIZARD_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.WIZARD.lootId());

    public static final ResourceKey<LootTable> SPIDER_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.SPIDER.lootId());
    public static final ResourceKey<LootTable> GIANT_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.GIANT.lootId());
    public static final ResourceKey<LootTable> PTERODACTYL_OVERRIDE = ResourceKey.create(Registries.LOOT_TABLE, RaiderGroup.PTERODACTYL.lootId());

    public static LootPool.Builder ACCESSORY_POOL = LootPool.lootPool()
            .when(LootItemRandomChanceCondition.randomChance(0.1f))
            .add(TagEntry.expandTag(ModTags.ARMY_INFORMATION_ACCESSORIES));

    @Override
    public void generate(@NonNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(GOBLIN_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.COPPER_NUGGET).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.STRING).setWeight(4).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.LEATHER).setWeight(4).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        output.accept(WARRIOR_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.COPPER_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.LEATHER).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));
        output.accept(BRUTE_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.COPPER_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));



        output.accept(ARCHER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.FLINT).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.FEATHER).setWeight(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.ARROW).setWeight(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        output.accept(PILLAGER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.FLINT).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                .add(LootItem.lootTableItem(Items.ARROW).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))));
        output.accept(SNIPER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.ARROW).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))));



        output.accept(BABY_RAVAGER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.SADDLE).setWeight(1))));
        output.accept(RAVAGER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(15))
                .add(LootItem.lootTableItem(Items.SADDLE).setWeight(2))
                .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        output.accept(WARLORD_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.SADDLE).setWeight(1))
                .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
                .withPool(ACCESSORY_POOL));



        output.accept(WITCH_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(12))
                .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));
        output.accept(SHAMAN_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(15))
                .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.GHAST_TEAR).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))));



        output.accept(EVOKER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING).setWeight(1))));
        output.accept(INVOKER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(9))
                .add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                .add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING).setWeight(2)))
                .withPool(ACCESSORY_POOL));



        output.accept(ILLUSIONER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.ARROW).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
                .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));
        output.accept(MAGICIAN_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(9))
                .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.BLAZE_ROD).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))));
        output.accept(WIZARD_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(9))
                .add(LootItem.lootTableItem(Items.BLAZE_ROD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.AMETHYST_CLUSTER).setWeight(1)))
                .withPool(ACCESSORY_POOL));



        output.accept(SPIDER_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(20))
                .add(LootItem.lootTableItem(Items.STRING).setWeight(4).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        output.accept(GIANT_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))))
                .withPool(ACCESSORY_POOL));
        output.accept(PTERODACTYL_OVERRIDE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(EmptyLootItem.emptyItem().setWeight(10))
                .add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .add(LootItem.lootTableItem(Items.FEATHER).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.ARROW).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
                .withPool(ACCESSORY_POOL));
    }

}