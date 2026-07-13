package cozmicweb.pda.datagen;

import cozmicweb.pda.common.PDACommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class ModCuriosProvider extends CuriosDataProvider {

    public ModCuriosProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(PDACommon.MOD_ID, output, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries) {
        this.createSlot("information_accessories")
                .icon(PDACommon.id("slot/information_accessory_slot"))
                .renderToggle(false)
                .size(2);

        this.createEntities("information_bearing_entities").addPlayer().addSlots("information_accessories");
    }

}