package cozmicweb.pda.common.content.reforge_modifier;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public record ItemReforge(Identifier id, int tier, Identifier groupId, Map<Holder<Attribute>, Double> modifiers) {

    public boolean canSupport(ItemStack stack) {
        return getGroup().canSupport(stack);
    }

    public @NonNull ReforgeGroup getGroup() {
        return ReforgeModifierRegistry.expectGroup(groupId);
    }

    public static @NonNull String getTranslationKey(@NonNull Identifier id) {
        return "reforge." + id.getNamespace() + "." + id.getPath();
    }

}