package cozmicweb.pda.client.gui;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jspecify.annotations.NonNull;

public enum RenderTextHorizontalAlignment implements TranslatableEnum {
    LEFT_TO_RIGHT,
    CENTERED,
    RIGHT_TO_LEFT;

    @Override
    public @NonNull Component getTranslatedName() {
        return Component.translatable("pda.configuration.render_text_horizontal." + this.name().toLowerCase());
    }
}
