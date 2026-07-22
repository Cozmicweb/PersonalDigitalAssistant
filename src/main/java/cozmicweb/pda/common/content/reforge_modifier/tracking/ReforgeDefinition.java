package cozmicweb.pda.common.content.reforge_modifier.tracking;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Map;

public record ReforgeDefinition(int tier, Map<Holder<Attribute>, Double> attributes) {}
