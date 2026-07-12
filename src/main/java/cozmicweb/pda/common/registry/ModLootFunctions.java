package cozmicweb.pda.common.registry;

import com.mojang.serialization.MapCodec;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.datagen.SetRandomTallyCountFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootFunctions {

    public static final DeferredRegister<MapCodec<? extends LootItemFunction>> LOOT_FUNCTION_TYPES =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, PDACommon.MOD_ID);

    public static final Supplier<MapCodec<SetRandomTallyCountFunction>> RANDOM_TALLY_COUNT =
            LOOT_FUNCTION_TYPES.register("random_tally_count", () -> SetRandomTallyCountFunction.CODEC);

    public static void register(IEventBus modEventBus) {
        LOOT_FUNCTION_TYPES.register(modEventBus);
    }

}
