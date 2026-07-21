package cozmicweb.pda.common.content.reforge_modifier;

import cozmicweb.pda.common.PDACommon;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record ReforgeGroup(Identifier id, List<TagKey<Item>> items, List<ItemReforge> reforges) {

    public static @NonNull ReforgeGroup tools() {
        return ReforgeModifierRegistry.expectGroup(PDACommon.id("tools"));
    }

    public static @NonNull ReforgeGroup ranged() {
        return ReforgeModifierRegistry.expectGroup(PDACommon.id("ranged"));
    }

    public boolean canSupport(@NonNull ItemStack stack) {
        return items.stream().anyMatch(stack::is);
    }

}