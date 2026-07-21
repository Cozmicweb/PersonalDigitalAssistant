package cozmicweb.pda.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.content.reforge_modifier.ReforgeManager;
import cozmicweb.pda.common.registry.ModGlobalLootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jspecify.annotations.NonNull;

public class RandomReforgeModifier extends LootModifier {
    public static final MapCodec<RandomReforgeModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, RandomReforgeModifier::new));

    public RandomReforgeModifier(LootItemCondition[] conditions, int priority) {
        super(conditions, priority);
    }

    @Override
    protected @NonNull ObjectArrayList<ItemStack> doApply(@NonNull ObjectArrayList<ItemStack> loot, @NonNull LootContext context) {
        RandomSource random = context.getRandom();
        loot.stream().filter(_ -> random.nextDouble() < PDAConfig.REFORGE_RANDOM_CHANCE.get()).forEach(i -> ReforgeManager.addRandomReforge(i, random));
        return loot;
    }

    @Override
    public @NonNull MapCodec<? extends IGlobalLootModifier> codec() {
        return ModGlobalLootModifiers.RANDOM_REFORGE.get();
    }

}
