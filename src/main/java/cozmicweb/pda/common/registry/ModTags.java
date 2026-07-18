package cozmicweb.pda.common.registry;

import cozmicweb.pda.common.PDACommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ModTags {

    public static final TagKey<Item> ALL_INFORMATION_ACCESSORIES = bind("all_information_accessories");
    public static final TagKey<Item> ARMY_INFORMATION_ACCESSORIES = bind("army_information_accessories");
    public static final TagKey<Item> BATTLE_STANDARDS = bind("battle_standards");

    private ModTags() {}

    private static @NonNull TagKey<Item> bind(String name) {
        return TagKey.create(Registries.ITEM, PDACommon.id(name));
    }

}
