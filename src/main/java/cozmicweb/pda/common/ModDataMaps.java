package cozmicweb.pda.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class ModDataMaps {

    public static final DataMapType<Item, Identifier> HANDLERS = DataMapType.builder(
            PDACommon.id("display_handlers"),
            Registries.ITEM,
            Identifier.CODEC
    ).build();

    @SubscribeEvent
    public static void register(RegisterDataMapTypesEvent event) {
        event.register(HANDLERS);
    }

}
