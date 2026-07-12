package cozmicweb.pda.client.gui;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jspecify.annotations.NonNull;

public enum RenderTextVerticalAlignment implements TranslatableEnum {
    TOP_TO_BOTTOM,
    CENTERED,
    BOTTOM_TO_TOP;

    @Override
    public @NonNull Component getTranslatedName() {
        return Component.translatable("pda.configuration.render_text_vertical." + this.name().toLowerCase());
    }
}
