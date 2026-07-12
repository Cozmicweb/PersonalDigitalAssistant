package cozmicweb.pda.datagen;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.registry.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, PDACommon.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.TALLY_COUNTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.STOPWATCH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WEATHER_RADIO.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SEXTANT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RADAR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LIFEFORM_ANALYZER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DPS_METER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DEPTH_METER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FISHERMANS_POCKET_GUIDE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.METAL_DETECTOR.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.REK_3000.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GPS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FISH_FINDER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PILLAGER_TECH.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.PDA.get(), ModelTemplates.FLAT_ITEM);
    }

}