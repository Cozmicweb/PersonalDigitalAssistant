package cozmicweb.pda.common.display.handlers;

import cozmicweb.pda.common.PDAConfig;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.fixes.BlockEntityKeepPacked;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.function.Predicate;

public class OreDisplayHandler extends InfoDisplayHandler {

    private static long lastCheck = System.currentTimeMillis();
    private static Component lastText = Component.empty();

    private static final Predicate<BlockState> IS_VALUABLE = s -> s.is(Tags.Blocks.ORES) || s.is(Tags.Blocks.STORAGE_BLOCKS);

    public OreDisplayHandler(Identifier id) {
        super(id);
    }

    public int getSize() {
        return PDAConfig.METAL_DETECTOR_SIZE.get();
    }

    @Override
    public int getDefaultPriority() {
        return 400;
    }

    @Override
    public int getUpdateInterval() {
        return PDAConfig.METAL_DETECTOR_UPDATE_RATE.get();
    }

    @Override
    public Component getDisplayText() {
        long now = System.currentTimeMillis();
        if (now - lastCheck < getUpdateInterval() * 1000L) return lastText;
        lastCheck = now;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return Component.empty();

        Vec3 position = player.position();
        Level level = player.level();
        int size = getSize();

        getNearby(level, position, size).ifPresentOrElse(hit -> {
            BlockPos pos = hit.first(); // todo: xray visuals?
            BlockState state = hit.second();
            lastText = Component.translatable("text.pda.metal_detector.detected_nearby", state.getBlock().getName());
        }, () -> {
            lastText = Component.translatable("text.pda.metal_detector.nothing");
        });

        return lastText;
    }

    public static @NonNull Optional<Pair<BlockPos, BlockState>> getNearby(Level level, Vec3 center, int size) {
        AABB box = AABB.ofSize(center, size, size, size);

        return BlockPos.betweenClosedStream(box)
                .map(pos -> {
                    BlockState state = level.getBlockState(pos);
                    return IS_VALUABLE.test(state) ? Pair.of(pos.immutable(), state) : null;
                })
                .filter(Objects::nonNull)
                .min(Comparator.comparingDouble(hit -> center.distanceToSqr(Vec3.atLowerCornerOf(hit.left()))));
    }

}
