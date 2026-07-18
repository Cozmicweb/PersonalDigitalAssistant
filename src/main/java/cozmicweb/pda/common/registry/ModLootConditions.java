package cozmicweb.pda.common.registry;

import com.mojang.serialization.MapCodec;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.loot.OverrideLootCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootConditions {

    public static final DeferredRegister<MapCodec<? extends LootItemCondition>> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, PDACommon.MOD_ID);

    public static final Supplier<MapCodec<OverrideLootCondition>> HAS_OVERRIDDEN_LOOT = LOOT_CONDITION_TYPES.register("has_overridden_loot", () -> OverrideLootCondition.CODEC);
    
    public static void register(IEventBus modEventBus) {
        LOOT_CONDITION_TYPES.register(modEventBus);
    }

}
