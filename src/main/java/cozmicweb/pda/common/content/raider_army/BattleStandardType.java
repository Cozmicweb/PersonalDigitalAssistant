package cozmicweb.pda.common.content.raider_army;

import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum BattleStandardType implements StringRepresentable {
    STANDARD,
    OMINOUS,
    WARDING;

    @Override
    public @NonNull String getSerializedName() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.getSerializedName();
    }
}