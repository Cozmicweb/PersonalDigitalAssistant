package cozmicweb.pda.common.registry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.data.RareMobData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class ModDataMaps {

    public static final DataMapType<Item, List<Identifier>> HANDLERS = DataMapType.builder(
            PDACommon.id("display_handlers"),
            Registries.ITEM,
            Codec.either(Identifier.CODEC.listOf(), Identifier.CODEC).xmap(
                    either -> either.map(list -> list, List::of),
                    list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list)
            )
    ).synced(Identifier.CODEC.listOf(), true).build();

    public static final DataMapType<EntityType<?>, RareMobData> RARE_MOBS = DataMapType.builder(
            PDACommon.id("rare_mobs"),
            Registries.ENTITY_TYPE,
            RareMobData.CODEC
    ).synced(RareMobData.CODEC, true).build();

    @SubscribeEvent
    public static void register(RegisterDataMapTypesEvent event) {
        event.register(HANDLERS);
        event.register(RARE_MOBS);
    }

}
