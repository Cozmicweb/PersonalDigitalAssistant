package cozmicweb.pda.common.content.raider_army.raiders;

import cozmicweb.pda.common.PDACommon;
import cozmicweb.pda.common.registry.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record RaiderGroup<T extends Mob>(Identifier lootId, String translation, float price, float weight, int min, int max, EntityType<T> type, Consumer<T> formatter) {
    public static final List<RaiderGroup<?>> ALL_RAIDERS = new ArrayList<>();
    /* Price, Weight, Min, Max */

    // Vindicators
    public static final RaiderGroup<Vindicator> GOBLIN = new RaiderGroup<>(getPDALootId("goblin"), translateMod("goblin"), 1, 14, 5, 25, EntityType.VINDICATOR, RaiderPresets::goblin);
    public static final RaiderGroup<Vindicator> WARRIOR = new RaiderGroup<>(getPDALootId("warrior"), translateMod("warrior"), 30, 15, 1, 10, EntityType.VINDICATOR, RaiderPresets::warrior);
    public static final RaiderGroup<Vindicator> BRUTE = new RaiderGroup<>(getPDALootId("brute"), translateMod("brute"), 70, 10, 1, 8, EntityType.VINDICATOR, RaiderPresets::brute);

    // Pillagers
    public static final RaiderGroup<Pillager> ARCHER = new RaiderGroup<>(getPDALootId("archer"), translateMod("archer"), 2, 14, 1, 20, EntityType.PILLAGER, RaiderPresets::archer);
    public static final RaiderGroup<Pillager> PILLAGER = new RaiderGroup<>(getPDALootId("pillager"), translateVanilla("pillager"), 20, 13, 1, 12, EntityType.PILLAGER, RaiderPresets::pillager);
    public static final RaiderGroup<Pillager> SNIPER = new RaiderGroup<>(getPDALootId("sniper"), translateMod("sniper"), 60, 10, 1, 10, EntityType.PILLAGER, RaiderPresets::sniper);

    // Ravager
    public static final RaiderGroup<Ravager> BABY_RAVAGER = new RaiderGroup<>(getPDALootId("baby_ravager"), translateMod("baby_ravager"), 75, 10, 2, 5, EntityType.RAVAGER, RaiderPresets::babyRavager);
    public static final RaiderGroup<Ravager> RAVAGER = new RaiderGroup<>(getPDALootId("ravager"), translateVanilla("ravager"), 200, 9, 1, 4, EntityType.RAVAGER);
    public static final RaiderGroup<Ravager> WARLORD = new RaiderGroup<>(getPDALootId("warlord"), translateMod("warlord"), 500, 0.3f, 1, 2, EntityType.RAVAGER, RaiderPresets::warlord);

    // Witch
    public static final RaiderGroup<Witch> WITCH = new RaiderGroup<>(getPDALootId("witch"), translateVanilla("witch"), 50, 7, 1, 8, EntityType.WITCH);
    public static final RaiderGroup<Witch> SHAMAN = new RaiderGroup<>(getPDALootId("shaman"), translateMod("shaman"), 130, 5, 1, 6, EntityType.WITCH, RaiderPresets::shaman);

    // Evoker
    public static final RaiderGroup<Evoker> EVOKER = new RaiderGroup<>(getPDALootId("evoker"), translateVanilla("evoker"), 80, 6, 1, 8, EntityType.EVOKER);
    public static final RaiderGroup<Evoker> INVOKER = new RaiderGroup<>(getPDALootId("invoker"), translateMod("invoker"), 150, 4, 1, 6, EntityType.EVOKER, RaiderPresets::invoker);

    // Illusioner
    public static final RaiderGroup<Illusioner> ILLUSIONER = new RaiderGroup<>(getPDALootId("illusioner"), translateVanilla("illusioner"), 100, 3, 1, 8, EntityType.ILLUSIONER);
    public static final RaiderGroup<Illusioner> MAGICIAN = new RaiderGroup<>(getPDALootId("magician"), translateMod("magician"), 200, 2, 1, 6, EntityType.ILLUSIONER, RaiderPresets::magician);
    public static final RaiderGroup<Illusioner> WIZARD = new RaiderGroup<>(getPDALootId("wizard"), translateMod("wizard"), 300, 1, 1, 5, EntityType.ILLUSIONER, RaiderPresets::wizard);

    // Special
    public static final RaiderGroup<CaveSpider> SPIDER = new RaiderGroup<>(getPDALootId("spider"), translateVanilla("spider"), 2, 2, 15, 50, EntityType.CAVE_SPIDER, RaiderPresets::spider);
    public static final RaiderGroup<Zombie> GIANT = new RaiderGroup<>(getPDALootId("giant"), translateVanilla("giant"), 100, 2, 1, 2, EntityType.ZOMBIE, RaiderPresets::giant);
    public static final RaiderGroup<Phantom> PTERODACTYL = new RaiderGroup<>(getPDALootId("pterodactyl"), translateMod("pterodactyl"), 130, 2, 1, 3, EntityType.PHANTOM, RaiderPresets::pterodactyl);

    public RaiderGroup(Identifier lootId, @Nullable String translation, float price, float weight, int min, int max, EntityType<T> type, @Nullable Consumer<T> formatter) {
        this.lootId = lootId;
        this.translation = translation;
        this.price = price;
        this.weight = weight;
        this.min = min;
        this.max = max;
        this.type = type;
        this.formatter = formatter;

        ALL_RAIDERS.add(this);
    }

    public RaiderGroup(Identifier lootId, String translation, float price, float weight, int min, int max, EntityType<T> type) {
        this(lootId, translation, price, weight, min, max, type, null);
    }

    public @Nullable T create(ServerLevel level) {
        T entity = type.create(level, EntitySpawnReason.EVENT);
        if (entity != null) {
            if (formatter != null)
                formatter.accept(entity);
            if (translation != null) {
                entity.setCustomName(Component.translatable(translation));
                entity.setCustomNameVisible(false);
            }
            entity.setData(ModAttachments.OVERRIDE_LOOT, lootId.toString());
        }
        return entity;
    }

    @Contract(pure = true)
    public static @NonNull String translateMod(String id) {
        return "entity." + PDACommon.MOD_ID + "." + id;
    }

    @Contract(pure = true)
    public static @NonNull String translateVanilla(String id) {
        return "entity.minecraft." + id;
    }

    @Contract("_ -> new")
    public static @NonNull Identifier getPDALootId(String id) {
        return PDACommon.id("entities/" + id + "_override");
    }

}