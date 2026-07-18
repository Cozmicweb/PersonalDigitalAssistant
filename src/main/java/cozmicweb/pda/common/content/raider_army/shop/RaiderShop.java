package cozmicweb.pda.common.content.raider_army.shop;

import cozmicweb.pda.common.content.raider_army.raiders.RaiderGroup;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RaiderShop {
    private static final float EPSILON = 0.01f;

    private RaiderShop() {}

    public static @NonNull List<RaiderGroup<?>> shop(@NonNull List<RaiderGroup<?>> pool, float budget, RandomSource random) {
        List<RaiderGroup<?>> result = new ArrayList<>();
        if (pool.isEmpty() || budget <= 0)
            return result;

        float poolWeight = 0f;
        for (RaiderGroup<?> group : pool)
            poolWeight += group.weight();

        if (poolWeight <= 0f)
            return result;

        float remaining = budget;

        Map<RaiderGroup<?>, Integer> counts = new HashMap<>();
        Map<RaiderGroup<?>, Float> spent = new HashMap<>();

        for (RaiderGroup<?> group : pool) {
            int min = group.min();
            if (min <= 0 || group.price() <= 0)
                continue;

            int guaranteed = Math.min(min, group.max());
            float minCost = group.price() * guaranteed;
            if (minCost > remaining)
                continue;

            remaining -= minCost;
            counts.merge(group, guaranteed, Integer::sum);
            spent.merge(group, minCost, Float::sum);
            for (int i = 0; i < guaranteed; i++)
                result.add(group);
        }

        List<RaiderGroup<?>> eligible = new ArrayList<>();
        List<Float> drawWeights = new ArrayList<>();

        while (remaining > 0) {
            eligible.clear();
            drawWeights.clear();
            float totalDrawWeight = 0f;

            for (RaiderGroup<?> group : pool) {
                if (counts.getOrDefault(group, 0) < group.min())
                    continue;
                if (counts.getOrDefault(group, 0) >= group.max())
                    continue;
                if (group.price() > remaining)
                    continue;

                float target = budget * (group.weight() / poolWeight);
                float need = target - spent.getOrDefault(group, 0f);
                float drawWeight = group.weight() * Math.max(need, EPSILON);

                eligible.add(group);
                drawWeights.add(drawWeight);
                totalDrawWeight += drawWeight;
            }

            if (eligible.isEmpty() || totalDrawWeight <= 0)
                break;

            float roll = random.nextFloat() * totalDrawWeight;
            float cumulative = 0f;
            RaiderGroup<?> picked = eligible.getLast();

            for (int i = 0; i < eligible.size(); i++) {
                cumulative += drawWeights.get(i);
                if (roll <= cumulative) {
                    picked = eligible.get(i);
                    break;
                }
            }

            remaining -= picked.price();
            counts.merge(picked, 1, Integer::sum);
            spent.merge(picked, picked.price(), Float::sum);
            result.add(picked);
        }

        return result;
    }

}