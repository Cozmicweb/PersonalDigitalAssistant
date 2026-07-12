package cozmicweb.pda.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface IClickReactive {
    default void onLeftClickDown(ServerPlayer player, ItemStack stack) {}
    default void onLeftClickUp(ServerPlayer player, ItemStack stack) {}
    default void onRightClickDown(ServerPlayer player, ItemStack stack) {}
    default void onRightClickUp(ServerPlayer player, ItemStack stack) {}
}