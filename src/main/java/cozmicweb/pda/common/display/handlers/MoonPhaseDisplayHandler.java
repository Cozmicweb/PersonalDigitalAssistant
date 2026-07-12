package cozmicweb.pda.common.display.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import net.neoforged.neoforge.event.level.NoteBlockEvent;

import java.util.Map;

public class MoonPhaseDisplayHandler extends InfoDisplayHandler {

    public static final Map<MoonPhase, String> PHASE_NAMES = Map.of(
            MoonPhase.FULL_MOON, "moon_phase.pda.full_moon",
            MoonPhase.WANING_GIBBOUS, "moon_phase.pda.waning_gibbous",
            MoonPhase.THIRD_QUARTER, "moon_phase.pda.last_quarter",
            MoonPhase.WANING_CRESCENT, "moon_phase.pda.waning_crescent",
            MoonPhase.NEW_MOON, "moon_phase.pda.new_moon",
            MoonPhase.WAXING_CRESCENT, "moon_phase.pda.waxing_crescent",
            MoonPhase.FIRST_QUARTER, "moon_phase.pda.first_quarter",
            MoonPhase.WAXING_GIBBOUS, "moon_phase.pda.waxing_gibbous"
    );

    public MoonPhaseDisplayHandler(Identifier id) {
        super(id);
    }

    @Override
    public int getDefaultPriority() {
        return 200;
    }

    @Override
    public Component getDisplayText() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return Component.empty();
        Level level = player.level();
        MoonPhase phase = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, player.blockPosition());
        return Component.translatable(PHASE_NAMES.get(phase));
    }

}
