package cozmicweb.pda.datagen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cozmicweb.pda.common.PDAConfig;
import cozmicweb.pda.common.registry.ModComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record SetRandomTallyCountFunction(List<LootItemCondition> conditions) implements LootItemFunction {

    public static final MapCodec<SetRandomTallyCountFunction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of())
                    .forGetter(SetRandomTallyCountFunction::conditions)
    ).apply(inst, SetRandomTallyCountFunction::new));

    @Override
    public @NonNull MapCodec<? extends LootItemFunction> codec() {
        return CODEC;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext lootContext) {
        RandomSource random = lootContext.getRandom();
        int max = Math.max((int) Math.pow(10, PDAConfig.TALLY_COUNTER_LIMIT.get() / 3.0) - 1, 0);
        int count = random.nextInt(max);
        stack.set(ModComponents.TALLY_COUNT, count);
        stack.set(ModComponents.TALLY_DISPLAY, count);
        return stack;
    }

}