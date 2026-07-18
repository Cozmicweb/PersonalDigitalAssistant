package cozmicweb.pda.datagen.loot;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.loot.OverrideLootCondition;
import cozmicweb.pda.common.loot.OverrideLootModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, PDACommon.MOD_ID);
    }

    @Override
    protected void start() {
        this.add("override_loot", new OverrideLootModifier(new LootItemCondition[] { new OverrideLootCondition() }, 0));

        this.add("add_mineshaft_loot",
                new AddTableLootModifier(
                        new LootItemCondition[] { LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT.identifier()).build() },
                        1,
                        ModLootTableProvider.MINESHAFT_LOOT));
        this.add("add_dungeon_loot",
                new AddTableLootModifier(
                        new LootItemCondition[] { LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON.identifier()).build() },
                        1,
                        ModLootTableProvider.DUNGEON_LOOT));
        this.add("add_buried_loot",
                new AddTableLootModifier(
                        new LootItemCondition[] { LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE.identifier()).build() },
                        1,
                        ModLootTableProvider.BURIED_LOOT));
        this.add("add_outpost_loot",
                new AddTableLootModifier(
                        new LootItemCondition[] { LootTableIdCondition.builder(BuiltInLootTables.PILLAGER_OUTPOST.identifier()).build() },
                        1,
                        ModLootTableProvider.OUTPOST_LOOT));
    }

}
