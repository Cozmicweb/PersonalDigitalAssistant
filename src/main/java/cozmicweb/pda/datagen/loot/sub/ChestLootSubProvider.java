package cozmicweb.pda.datagen.loot.sub;

import cozmicweb.pda.common.registry.ModItems;
import cozmicweb.pda.datagen.item.property.SetRandomTallyCountFunction;
import cozmicweb.pda.datagen.loot.ModLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.function.BiConsumer;

public record ChestLootSubProvider(HolderLookup.Provider registries) implements LootTableSubProvider {

    @Override
    public void generate(@NonNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(ModLootTableProvider.MINESHAFT_LOOT, this.mineshaftLootTable());
        output.accept(ModLootTableProvider.DUNGEON_LOOT, this.dungeonLootTable());
        output.accept(ModLootTableProvider.BURIED_LOOT, this.buriedLootTable());
        output.accept(ModLootTableProvider.OUTPOST_LOOT, this.outpostLootTable());
    }

    public LootTable.Builder mineshaftLootTable() {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModItems.DEPTH_METER))
                                .add(LootItem.lootTableItem(ModItems.METAL_DETECTOR))
                                .add(EmptyLootItem.emptyItem().setWeight(15))
                );
    }

    public LootTable.Builder dungeonLootTable() {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModItems.DPS_METER))
                                .add(LootItem.lootTableItem(ModItems.RADAR))
                                .add(LootItem.lootTableItem(ModItems.LIFEFORM_ANALYZER))
                                .add(EmptyLootItem.emptyItem().setWeight(15))
                );
    }

    public LootTable.Builder buriedLootTable() {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModItems.FISHERMANS_POCKET_GUIDE))
                                .add(LootItem.lootTableItem(ModItems.SEXTANT))
                                .add(EmptyLootItem.emptyItem().setWeight(15))
                );
    }

    public LootTable.Builder outpostLootTable() {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModItems.TALLY_COUNTER).apply(() -> new SetRandomTallyCountFunction(Collections.emptyList())))
                                .add(LootItem.lootTableItem(ModItems.STOPWATCH))
                                .add(LootItem.lootTableItem(ModItems.WEATHER_RADIO))
                                .add(EmptyLootItem.emptyItem().setWeight(15))
                );
    }


}
