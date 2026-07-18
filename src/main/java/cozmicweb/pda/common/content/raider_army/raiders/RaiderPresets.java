package cozmicweb.pda.common.content.raider_army.raiders;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.function.Consumer;

public class RaiderPresets {
    private RaiderPresets() {}

    public static void warrior(Vindicator v) {
        RaiderPresets.apply(v,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 0.95),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 0.2),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 10),
                RaiderPresets.AttributeEdit.of(Attributes.ARMOR, 8),
                RaiderPresets.InventoryEdit.of(EquipmentSlot.MAINHAND, Items.STONE_SWORD.getDefaultInstance()));
    }

    public static void spider(CaveSpider s) {
        RaiderPresets.apply(s,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 0.3),
                RaiderPresets.AttributeEdit.of(Attributes.MOVEMENT_SPEED, 0.4),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 4));
    }

    public static void archer(Pillager p) {
        RaiderPresets.apply(p,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 0.88),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 0.5),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 6),
                RaiderPresets.AttributeEdit.of(Attributes.ARMOR, 2),
                RaiderPresets.InventoryEdit.of(EquipmentSlot.MAINHAND, Items.CROSSBOW.getDefaultInstance()));
    }

    public static void pillager(Pillager p) {
        RaiderPresets.apply(p,
                RaiderPresets.InventoryEdit.of(EquipmentSlot.MAINHAND, Items.CROSSBOW.getDefaultInstance()));
    }

    public static void goblin(Vindicator v) {
        RaiderPresets.apply(v,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 0.7),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 0.7),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 6),
                RaiderPresets.InventoryEdit.of(EquipmentSlot.MAINHAND, ItemStack.EMPTY));
    }

    public static void giant(Zombie z) {
        RaiderPresets.apply(z,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 3),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 40),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 18),
                RaiderPresets.AttributeEdit.of(Attributes.BURNING_TIME, 0));
    }

    public static void pterodactyl(Phantom p) {
        RaiderPresets.apply(p,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 3),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 50),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 16),
                RaiderPresets.AttributeEdit.of(Attributes.BURNING_TIME, 0));
    }

    public static void babyRavager(Ravager r) {
        RaiderPresets.apply(r,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 0.7),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 20),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 9),
                RaiderPresets.AttributeEdit.of(Attributes.KNOCKBACK_RESISTANCE, 0));
    }

    public static void brute(Vindicator v) {
        RaiderPresets.apply(v,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 1.15),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 1.4),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 30),
                RaiderPresets.AttributeEdit.of(Attributes.ARMOR, 6),
                RaiderPresets.AttributeEdit.of(Attributes.KNOCKBACK_RESISTANCE, 0.5),
                RaiderPresets.InventoryEdit.of(EquipmentSlot.MAINHAND, Items.IRON_AXE.getDefaultInstance()));
    }

    public static void sniper(Pillager p) {
        RaiderPresets.apply(p,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 1.05),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 1.2),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 30),
                RaiderPresets.AttributeEdit.of(Attributes.MOVEMENT_SPEED, 0.35),
                RaiderPresets.InventoryEdit.of(EquipmentSlot.MAINHAND, quickEnchant(p.registryAccess(),
                        Items.CROSSBOW.getDefaultInstance(), Enchantments.QUICK_CHARGE, 3, Enchantments.PIERCING, 3)));
    }

    public static void shaman(Witch w) {
        RaiderPresets.apply(w,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 1.1),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 40),
                RaiderPresets.AttributeEdit.of(Attributes.MOVEMENT_SPEED, 0.3));
    }

    public static void invoker(Evoker e) {
        RaiderPresets.apply(e,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 1.2),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 45),
                RaiderPresets.AttributeEdit.of(Attributes.ARMOR, 4));
    }

    public static void magician(Illusioner e) {
        RaiderPresets.apply(e,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 1.2),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 50),
                RaiderPresets.AttributeEdit.of(Attributes.ARMOR, 5));
    }

    public static void wizard(Illusioner e) {
        RaiderPresets.apply(e,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 1.3),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 150),
                RaiderPresets.AttributeEdit.of(Attributes.ARMOR, 8),
                RaiderPresets.InventoryEdit.of(EquipmentSlot.OFFHAND, Items.TOTEM_OF_UNDYING.getDefaultInstance()));
    }

    public static void warlord(Ravager r) {
        RaiderPresets.apply(r,
                RaiderPresets.AttributeEdit.of(Attributes.SCALE, 1.5),
                RaiderPresets.AttributeEdit.of(Attributes.MAX_HEALTH, 250),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_DAMAGE, 18),
                RaiderPresets.AttributeEdit.of(Attributes.ATTACK_KNOCKBACK, 1.5),
                RaiderPresets.AttributeEdit.of(Attributes.MOVEMENT_SPEED, 0.35),
                RaiderPresets.AttributeEdit.of(Attributes.KNOCKBACK_RESISTANCE, 1.0));
    }

    public interface RaiderPreset {
        void apply(@NonNull LivingEntity entity);
    }

    public record CustomEdit(Consumer<LivingEntity> edit) implements RaiderPreset {
        public static @NonNull CustomEdit of(Consumer<LivingEntity> edit) {
            return new CustomEdit(edit);
        }

        @Override
        public void apply(@NonNull LivingEntity entity) {
            edit.accept(entity);
        }
    }

    public record InventoryEdit(EquipmentSlot slot, ItemStack stack) implements RaiderPreset {
        @Contract("_, _ -> new")
        public static @NonNull InventoryEdit of(EquipmentSlot slot, ItemStack stack) {
            return new InventoryEdit(slot, stack);
        }

        @Override
        public void apply(@NonNull LivingEntity entity) {
            entity.setItemSlot(slot(), stack());
        }
    }

    public record AttributeEdit(Holder<Attribute> attribute, double value) implements RaiderPreset {
        @Contract("_, _ -> new")
        public static @NonNull AttributeEdit of(Holder<Attribute> attribute, double value) {
            return new AttributeEdit(attribute, value);
        }

        @Override
        public void apply(@NonNull LivingEntity entity) {
            AttributeMap attributes = entity.getAttributes();
            AttributeInstance instance = attributes.getInstance(attribute());
            if (instance != null) instance.setBaseValue(value());
        }
    }

    /* ResourceKey<Enchantment>, int */
    private static ItemStack quickEnchant(RegistryAccess registryAccess, ItemStack stack, Object @NonNull ... enchantmentLevelPair) {
        int size = enchantmentLevelPair.length;
        if (size % 2 != 0)
            throw new IllegalArgumentException("Malformed enchantment level pair");
        HolderLookup.RegistryLookup<Enchantment> enchantmentLookup = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(stack));

        for (int i = 0; i < size; i += 2) {
            Object leftPair = enchantmentLevelPair[i];
            Object rightPair = enchantmentLevelPair[i + 1];

            if (leftPair instanceof ResourceKey<?> key && rightPair instanceof Integer level) {
                key.cast(Registries.ENCHANTMENT).ifPresent(enchantment -> {
                    mutable.set(enchantmentLookup.getOrThrow(enchantment), level);
                });
            }
        }

        EnchantmentHelper.setEnchantments(stack, mutable.toImmutable());
        return stack;
    }

    public static void apply(@NonNull LivingEntity entity, RaiderPreset @NonNull ... edits) {
        Arrays.stream(edits).forEach(e -> e.apply(entity));
    }

}
