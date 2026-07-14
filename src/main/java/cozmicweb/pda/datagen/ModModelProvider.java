package cozmicweb.pda.datagen;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.registry.ModItems;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.CompositeModel;
import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, PDACommon.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockGen, @NonNull ItemModelGenerators itemGen) {
        itemGen.generateFlatItem(ModItems.CRT_TV.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.WEATHER_RADIO.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.SEXTANT.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.RADAR.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.LIFEFORM_ANALYZER.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.DPS_METER.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.DEPTH_METER.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.FISHERMANS_POCKET_GUIDE.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.METAL_DETECTOR.get(), ModelTemplates.FLAT_ITEM);

        itemGen.generateFlatItem(ModItems.REK_3000.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.GPS.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.FISH_FINDER.get(), ModelTemplates.FLAT_ITEM);
        itemGen.generateFlatItem(ModItems.PILLAGER_TECH.get(), ModelTemplates.FLAT_ITEM);

        itemGen.generateFlatItem(ModItems.PDA.get(), ModelTemplates.FLAT_ITEM);

        generateStopwatchModel(itemGen);
        generateTallyCounterModel(itemGen);
    }

    private static void generateStopwatchModel(ItemModelGenerators gen) {
        Item stopwatch = ModItems.STOPWATCH.get();

        List<SelectItemModel.SwitchCase<Integer>> needleCases = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ItemModel.Unbaked frame = ItemModelUtils.plainModel(
                    gen.createFlatItemModel(stopwatch, "_needle_" + i, ModelTemplates.FLAT_ITEM)
            );
            needleCases.add(new SelectItemModel.SwitchCase<>(List.of(i), frame));
        }
        ItemModel.Unbaked needleFallback = needleCases.getFirst().model();
        ItemModel.Unbaked needleSelect = ItemModelUtils.select(new NeedleRotationProperty(), needleFallback, needleCases);

        ItemModel.Unbaked unpressed = ItemModelUtils.plainModel(gen.createFlatItemModel(stopwatch, "", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked pressed = ItemModelUtils.plainModel(gen.createFlatItemModel(stopwatch, "_pressed", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked baseConditional = ItemModelUtils.conditional(new MechanismPressedProperty(), pressed, unpressed);

        gen.itemModelOutput.accept(stopwatch, new CompositeModel.Unbaked(List.of(baseConditional, needleSelect), Optional.empty()));
    }

    private static void generateTallyCounterModel(@NonNull ItemModelGenerators gen) {
        Item tallyCounter = ModItems.TALLY_COUNTER.get();

        ItemModel.Unbaked unpressed = ItemModelUtils.plainModel(gen.createFlatItemModel(tallyCounter, "", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked pressed = ItemModelUtils.plainModel(gen.createFlatItemModel(tallyCounter, "_pressed", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked baseConditional = ItemModelUtils.conditional(new MechanismPressedProperty(), pressed, unpressed);

        gen.itemModelOutput.accept(tallyCounter, new CompositeModel.Unbaked(List.of(baseConditional), Optional.empty()));
    }

}