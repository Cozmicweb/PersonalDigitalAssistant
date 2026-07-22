package cozmicweb.pda.datagen.tag;

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

    public static class ItemProvider extends ItemTagsProvider {
        public ItemProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, PDACommon.MOD_ID);
        }

        @Override
        protected void addTags(HolderLookup.@NonNull Provider registries) {
            this.tag(ModTags.ALL_INFORMATION_ACCESSORIES)
                .add(ModItems.CRT_TV.get())
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

            this.tag(ModTags.ARMY_INFORMATION_ACCESSORIES)
                .add(ModItems.STOPWATCH.get())
                .add(ModItems.WEATHER_RADIO.get())
                .add(ModItems.SEXTANT.get())
                .add(ModItems.RADAR.get())
                .add(ModItems.LIFEFORM_ANALYZER.get())
                .add(ModItems.DPS_METER.get())
                .add(ModItems.DEPTH_METER.get())
                .add(ModItems.CRT_TV.get());

            this.tag(ModTags.BATTLE_STANDARDS)
                    .add(ModItems.EMPTY_BATTLE_STANDARD.get())
                    .add(ModItems.OMINOUS_BATTLE_STANDARD.get())
                    .add(ModItems.WARDING_BATTLE_STANDARD.get())
                    .add(ModItems.REVEALING_BATTLE_STANDARD.get())
                    .add(ModItems.GATHERING_BATTLE_STANDARD.get());
        }
    }

}
