package cozmicweb.pda.common.loot;

import com.mojang.serialization.MapCodec;
import cozmicweb.pda.common.registry.ModAttachments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record OverrideLootCondition() implements LootItemCondition {
    public static final MapCodec<OverrideLootCondition> CODEC = MapCodec.unit(OverrideLootCondition::new);

    @Override
    public boolean test(@NonNull LootContext context) {
        return Optional.ofNullable(context.getOptionalParameter(LootContextParams.THIS_ENTITY)).map(p -> !p.getData(ModAttachments.OVERRIDE_LOOT).isEmpty()).orElse(false);
    }

    @Override
    public @NonNull MapCodec<? extends LootItemCondition> codec() {
        return CODEC;
    }
}