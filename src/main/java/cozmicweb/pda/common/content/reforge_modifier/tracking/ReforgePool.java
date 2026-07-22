package cozmicweb.pda.common.content.reforge_modifier.tracking;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;

public record ReforgePool(List<TagKey<Item>> items, Map<String, ReforgeDefinition> reforges) {}
