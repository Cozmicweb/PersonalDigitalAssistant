package cozmicweb.pda.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import cozmicweb.pda.common.registry.ModComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record NeedleRotationProperty() implements SelectItemModelProperty<Integer> {

    public static final SelectItemModelProperty.Type<NeedleRotationProperty, Integer> TYPE =
            SelectItemModelProperty.Type.create(
                    MapCodec.unit(new NeedleRotationProperty()),
                    Codec.intRange(0, 15)
            );

    @Override
    public @NonNull Integer get(
            @NonNull ItemStack itemStack,
            @Nullable ClientLevel clientLevel,
            @Nullable LivingEntity livingEntity,
            int i,
            @NonNull ItemDisplayContext itemDisplayContext) {
        return itemStack.getOrDefault(ModComponents.STOPWATCH_ANIM.get(), 1);
    }

    @Override
    public @NonNull Codec<Integer> valueCodec() {
        return Codec.INT;
    }

    @Override
    public @NonNull Type<? extends SelectItemModelProperty<Integer>, Integer> type() {
        return TYPE;
    }

}
