package cozmicweb.pda.common.registry;

import cozmicweb.pda.common.PDACommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ModTags {

    public static final TagKey<Item> INFORMATION_ACCESSORIES = bind("information_accessories");

    private ModTags() {}

    private static @NonNull TagKey<Item> bind(String name) {
        return TagKey.create(Registries.ITEM, PDACommon.id(name));
    }

}
