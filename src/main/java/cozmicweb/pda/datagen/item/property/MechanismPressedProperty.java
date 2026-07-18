package cozmicweb.pda.datagen.item.property;

import com.mojang.serialization.MapCodec;
import cozmicweb.pda.common.registry.ModComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record MechanismPressedProperty() implements ConditionalItemModelProperty {

    public static final MapCodec<MechanismPressedProperty> MAP_CODEC = MapCodec.unit(new MechanismPressedProperty());

    @Override
    public @NonNull MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(
            @NonNull ItemStack itemStack,
            @Nullable ClientLevel clientLevel,
            @Nullable LivingEntity livingEntity,
            int i,
            @NonNull ItemDisplayContext itemDisplayContext) {
        return itemStack.getOrDefault(ModComponents.STOPWATCH_PRESSED.get(), false);
    }

}
