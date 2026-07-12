package cozmicweb.pda.datagen;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.registry.ModItems;
import cozmicweb.pda.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModTagProvider {

    public static class Item extends ItemTagsProvider {
        public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, PDACommon.MOD_ID);
        }

        @Override
        protected void addTags(HolderLookup.@NonNull Provider registries) {
            this.tag(ModTags.INFORMATION_ACCESSORIES)
                .add(ModItems.TALLY_COUNTER.getKey())
                .add(ModItems.STOPWATCH.getKey())
                .add(ModItems.WEATHER_RADIO.getKey())
                .add(ModItems.SEXTANT.getKey())
                .add(ModItems.RADAR.getKey())
                .add(ModItems.LIFEFORM_ANALYZER.getKey())
                .add(ModItems.DPS_METER.getKey())
                .add(ModItems.DEPTH_METER.getKey())
                .add(ModItems.FISHERMANS_POCKET_GUIDE.getKey())
                .add(ModItems.METAL_DETECTOR.getKey())
                .add(ModItems.REK_3000.getKey())
                .add(ModItems.GPS.getKey())
                .add(ModItems.FISH_FINDER.getKey())
                .add(ModItems.PILLAGER_TECH.getKey())
                .add(ModItems.PDA.getKey())
                .add(BuiltInRegistries.ITEM.getResourceKey(Items.COMPASS).orElseThrow())
                .add(BuiltInRegistries.ITEM.getResourceKey(Items.CLOCK).orElseThrow());
        }
    }

}
