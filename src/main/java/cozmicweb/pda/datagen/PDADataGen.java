package cozmicweb.pda.datagen;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.PDACompat;
import cozmicweb.pda.common.compat.curios.ModCuriosProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class PDADataGen {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.@NonNull Client event) {
        DataGenerator gen = event.getGenerator();
        PackOutput pack = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        gen.addProvider(true, new ModModelProvider(pack));
        gen.addProvider(true, new ModSoundProvider(pack));
        gen.addProvider(true, new ModTagProvider.Item(pack, lookup));
        gen.addProvider(true, new ModLootTableProvider(pack, lookup));
        gen.addProvider(true, new ModLootModifierProvider(pack, lookup));
        gen.addProvider(true, new ModRecipeProvider.Runner(pack, lookup));

        if (PDACompat.curiosLoaded)
            gen.addProvider(true, new ModCuriosProvider(pack, lookup));
    }

}
