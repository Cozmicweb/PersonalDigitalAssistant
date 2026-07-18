package cozmicweb.pda.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.command.ArmiesCommand;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jspecify.annotations.NonNull;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(@NonNull RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        ArmiesCommand.register(dispatcher);
    }

}