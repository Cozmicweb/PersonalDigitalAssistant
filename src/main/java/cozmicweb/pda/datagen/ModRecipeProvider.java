package cozmicweb.pda.datagen;

import cozmicweb.pda.common.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public @NonNull String getName() {
            return "Information Accessories";
        }
    }

    @Override
    protected void buildRecipes() {
        shapeless(RecipeCategory.TOOLS, ModItems.REK_3000)
                .requires(ModItems.RADAR)
                .requires(ModItems.TALLY_COUNTER)
                .requires(ModItems.LIFEFORM_ANALYZER)
                .unlockedBy(getHasName(ModItems.RADAR), has(ModItems.RADAR))
                .unlockedBy(getHasName(ModItems.TALLY_COUNTER), has(ModItems.TALLY_COUNTER))
                .unlockedBy(getHasName(ModItems.LIFEFORM_ANALYZER), has(ModItems.LIFEFORM_ANALYZER))
                .save(output);

        shapeless(RecipeCategory.TOOLS, ModItems.FISH_FINDER)
                .requires(ModItems.FISHERMANS_POCKET_GUIDE)
                .requires(ModItems.WEATHER_RADIO)
                .requires(ModItems.SEXTANT)
                .unlockedBy(getHasName(ModItems.FISHERMANS_POCKET_GUIDE), has(ModItems.FISHERMANS_POCKET_GUIDE))
                .unlockedBy(getHasName(ModItems.WEATHER_RADIO), has(ModItems.WEATHER_RADIO))
                .unlockedBy(getHasName(ModItems.SEXTANT), has(ModItems.SEXTANT))
                .save(output);

        shapeless(RecipeCategory.TOOLS, ModItems.PILLAGER_TECH)
                .requires(ModItems.DPS_METER)
                .requires(ModItems.STOPWATCH)
                .requires(ModItems.METAL_DETECTOR)
                .unlockedBy(getHasName(ModItems.DPS_METER), has(ModItems.DPS_METER))
                .unlockedBy(getHasName(ModItems.STOPWATCH), has(ModItems.STOPWATCH))
                .unlockedBy(getHasName(ModItems.METAL_DETECTOR), has(ModItems.METAL_DETECTOR))
                .save(output);

        shapeless(RecipeCategory.TOOLS, ModItems.GPS)
                .requires(Items.CLOCK)
                .requires(Items.COMPASS)
                .requires(ModItems.DEPTH_METER)
                .unlockedBy(getHasName(Items.CLOCK), has(Items.CLOCK))
                .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
                .unlockedBy(getHasName(ModItems.DEPTH_METER), has(ModItems.DEPTH_METER))
                .save(output);

        shapeless(RecipeCategory.TOOLS, ModItems.PDA)
                .requires(ModItems.GPS)
                .requires(ModItems.REK_3000)
                .requires(ModItems.PILLAGER_TECH)
                .requires(ModItems.FISH_FINDER)
                .unlockedBy(getHasName(ModItems.GPS), has(ModItems.GPS))
                .unlockedBy(getHasName(ModItems.REK_3000), has(ModItems.REK_3000))
                .unlockedBy(getHasName(ModItems.PILLAGER_TECH), has(ModItems.PILLAGER_TECH))
                .unlockedBy(getHasName(ModItems.FISH_FINDER), has(ModItems.FISH_FINDER))
                .save(output);
    }

}
