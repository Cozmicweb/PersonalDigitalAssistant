package cozmicweb.pda.common.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record DamageTracker(float[] window, long lastTick) {

    public static int getWindowSize() {
        return 100;
    }

    public DamageTracker() {
        this(new float[getWindowSize()], 0);
    }

    public DamageTracker addDamage(long currentTick, float damage) {
        float[] newWindow = window;
        int windowSize = getWindowSize();
        if (newWindow.length != windowSize) {
            float[] resized = new float[windowSize];
            System.arraycopy(window, 0, resized, 0, Math.min(window.length, windowSize));
            newWindow = resized;
        } else {
            newWindow = window.clone();
        }

        if (currentTick > lastTick) {
            int ticksToClear = (int) Math.min(windowSize, currentTick - lastTick);
            for (int i = 1; i <= ticksToClear; i++) {
                newWindow[(int) ((lastTick + i) % windowSize)] = 0;
            }
        }
        newWindow[(int) (currentTick % windowSize)] += damage;
        return new DamageTracker(newWindow, currentTick);
    }

    public float getSum(long currentTick) {
        int windowSize = getWindowSize();
        if (currentTick - lastTick >= windowSize) return 0;

        float sum = 0;
        for (float v : window) sum += v;

        if (currentTick > lastTick) {
            int ticksToClear = (int) Math.min(windowSize, currentTick - lastTick);
            for (int i = 1; i <= ticksToClear; i++) {
                sum -= window[(int) ((lastTick + i) % windowSize)];
            }
        }
        return Math.max(0, sum);
    }

    public static final Codec<DamageTracker> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.listOf().xmap(
                    list -> {
                        int windowSize = getWindowSize();
                        float[] array = new float[windowSize];
                        for (int i = 0; i < Math.min(windowSize, list.size()); i++) array[i] = list.get(i);
                        return array;
                    },
                    array -> {
                        List<Float> list = new ArrayList<>(array.length);
                        for (float f : array) list.add(f);
                        return list;
                    }
            ).fieldOf("window").forGetter(DamageTracker::window),
            Codec.LONG.fieldOf("lastTick").forGetter(DamageTracker::lastTick)
    ).apply(inst, DamageTracker::new));

    public static final StreamCodec<ByteBuf, DamageTracker> STREAM_CODEC = new StreamCodec<ByteBuf, DamageTracker>() {
        @Override
        public @NonNull DamageTracker decode(@NonNull ByteBuf buf) {
            int listSize = buf.readInt();
            float[] window = new float[listSize];
            for (int i = 0; i < listSize; i++) window[i] = buf.readFloat();
            long lastTick = buf.readLong();
            return new DamageTracker(window, lastTick);
        }

        @Override
        public void encode(@NonNull ByteBuf buf, DamageTracker tracker) {
            buf.writeInt(tracker.window.length);
            for (float f : tracker.window) buf.writeFloat(f);
            buf.writeLong(tracker.lastTick);
        }
    };
}
