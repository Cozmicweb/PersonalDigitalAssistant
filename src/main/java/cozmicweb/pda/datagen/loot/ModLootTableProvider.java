package cozmicweb.pda.datagen.loot;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.datagen.loot.sub.ChestLootSubProvider;
import cozmicweb.pda.datagen.loot.sub.EntityLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {

    public static final ResourceKey<LootTable> MINESHAFT_LOOT = ModLootTableProvider.createKey("chests/abandoned_mineshaft");
    public static final ResourceKey<LootTable> DUNGEON_LOOT = ModLootTableProvider.createKey("chests/simple_dungeon");
    public static final ResourceKey<LootTable> BURIED_LOOT = ModLootTableProvider.createKey("chests/buried_treasure");
    public static final ResourceKey<LootTable> OUTPOST_LOOT = ModLootTableProvider.createKey("chests/pillager_outpost");

    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        List<LootTableProvider.SubProviderEntry> tables = List.of(
            new LootTableProvider.SubProviderEntry(ChestLootSubProvider::new, LootContextParamSets.CHEST),
            new LootTableProvider.SubProviderEntry(EntityLootSubProvider::new, LootContextParamSets.CHEST)
        );

        super(output, Collections.emptySet(), tables, registries);
    }

    public static @NonNull ResourceKey<LootTable> createKey(String location) {
        return ResourceKey.create(Registries.LOOT_TABLE, PDACommon.id(location));
    }

}
