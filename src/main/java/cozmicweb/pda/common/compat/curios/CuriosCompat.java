package cozmicweb.pda.common.compat.curios;

import cozmicweb.pda.common.display.InfoDisplayManager;
import cozmicweb.pda.common.display.handlers.InfoDisplayHandler;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Set;

public class CuriosCompat {

    public static void collectCurios(Player player, boolean clientSide, Set<InfoDisplayHandler> active) {
        CuriosApi.getCuriosInventory(player).flatMap(curios -> curios.getStacksHandler("information_accessories")).ifPresent(stacksHandler -> {
            IDynamicStackHandler stacks = stacksHandler.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                InfoDisplayManager.collectHandlers(stacks.getStackInSlot(i), clientSide, active);
            }
        });
    }

}
