package cozmicweb.pda.datagen;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.registry.ModItems;
import cozmicweb.pda.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
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
                .add(ModItems.TALLY_COUNTER.get())
                .add(ModItems.STOPWATCH.get())
                .add(ModItems.WEATHER_RADIO.get())
                .add(ModItems.SEXTANT.get())
                .add(ModItems.RADAR.get())
                .add(ModItems.LIFEFORM_ANALYZER.get())
                .add(ModItems.DPS_METER.get())
                .add(ModItems.DEPTH_METER.get())
                .add(ModItems.FISHERMANS_POCKET_GUIDE.get())
                .add(ModItems.METAL_DETECTOR.get())
                .add(ModItems.REK_3000.get())
                .add(ModItems.GPS.get())
                .add(ModItems.FISH_FINDER.get())
                .add(ModItems.PILLAGER_TECH.get())
                .add(ModItems.PDA.get())
                .add(Items.COMPASS)
                .add(Items.CLOCK);
        }
    }

}
