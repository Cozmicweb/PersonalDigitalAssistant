package cozmicweb.pda.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cozmicweb.pda.common.registry.ModAttachments;
import cozmicweb.pda.common.registry.ModGlobalLootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jspecify.annotations.NonNull;

public class OverrideLootModifier extends LootModifier {
    public static final MapCodec<OverrideLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, OverrideLootModifier::new));

    public OverrideLootModifier(LootItemCondition[] conditions, int priority) {
        super(conditions, priority);
    }

    @Override
    protected @NonNull ObjectArrayList<ItemStack> doApply(@NonNull ObjectArrayList<ItemStack> loot, @NonNull LootContext context) {
        Entity entity = context.getParameter(LootContextParams.THIS_ENTITY);
        String lootString = entity.getData(ModAttachments.OVERRIDE_LOOT);
        if (lootString.isBlank()) return loot;
        Identifier lootId = Identifier.parse(lootString);

        ServerLevel serverLevel = context.getLevel();
        ResourceKey<LootTable> tableKey = ResourceKey.create(Registries.LOOT_TABLE, lootId);
        LootTable customTable = serverLevel.getServer().reloadableRegistries().getLootTable(tableKey);

        LootParams params = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withOptionalParameter(LootContextParams.DAMAGE_SOURCE, context.getOptionalParameter(LootContextParams.DAMAGE_SOURCE))
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY))
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, context.getOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY))
                .withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, context.getOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER))
                .create(LootContextParamSets.ENTITY);

        ObjectArrayList<ItemStack> results = new ObjectArrayList<>();
        //noinspection deprecation (raw must be used here to prevent infinite loop)
        customTable.getRandomItemsRaw(params, results::add);

        loot.clear();
        loot.addAll(results);
        return loot;
    }

    @Override
    public @NonNull MapCodec<? extends IGlobalLootModifier> codec() {
        return ModGlobalLootModifiers.OVERRIDE_LOOT.get();
    }
}
