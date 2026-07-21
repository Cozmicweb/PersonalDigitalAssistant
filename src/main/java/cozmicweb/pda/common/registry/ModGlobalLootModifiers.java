package cozmicweb.pda.common.registry;

import com.mojang.serialization.MapCodec;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.loot.OverrideLootModifier;
import cozmicweb.pda.common.loot.RandomReforgeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModGlobalLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, PDACommon.MOD_ID);

    public static final Supplier<MapCodec<OverrideLootModifier>> OVERRIDE_LOOT = GLM_SERIALIZERS.register("override_loot", () -> OverrideLootModifier.CODEC);
    public static final Supplier<MapCodec<RandomReforgeModifier>> RANDOM_REFORGE = GLM_SERIALIZERS.register("random_reforge", () -> RandomReforgeModifier.CODEC);

    public static void register(IEventBus modEventBus) {
        GLM_SERIALIZERS.register(modEventBus);
    }

}
