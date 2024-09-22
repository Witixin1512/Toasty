package net.witixin.toasty;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.witixin.toasty.factories.IdentifiedToastFactory;

import java.util.HashMap;
import java.util.Map;

public class ToastyEventHandler {

    private static final Map<ResourceLocation, IdentifiedToastFactory> itemPickupToasts = new HashMap<>();
    private static final Map<ResourceLocation, IdentifiedToastFactory> entityKillToasts = new HashMap<>();

    public static void beforeEntityReload() {
        entityKillToasts.clear();
    }

    public static void beforeItemReload() {
        itemPickupToasts.clear();
    }

    public static void onItemPickup(final ItemStack pickedItemStack) {
        tryCreateToastFromFactory(itemPickupToasts, BuiltInRegistries.ITEM.getKey(pickedItemStack.getItem()));
    }

    public static void onEntityKill(final DamageSource source, final EntityType<?> entityType) {
        if (source.is(DamageTypeTags.IS_PLAYER_ATTACK) && (source.getEntity() == Minecraft.getInstance().player || source.getDirectEntity() == Minecraft.getInstance().player)) {
            tryCreateToastFromFactory(entityKillToasts, BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
        }
    }

    public static void trackItem(ResourceLocation target, IdentifiedToastFactory factory) {
        itemPickupToasts.put(target, factory);
    }

    public static void trackEntityType(ResourceLocation target, IdentifiedToastFactory factory) {
        entityKillToasts.put(target, factory);
    }

    private static void tryCreateToastFromFactory(Map<ResourceLocation, IdentifiedToastFactory> map, ResourceLocation key) {
        IdentifiedToastFactory identifiedFactory = map.get(key);
        if (identifiedFactory != null && (!identifiedFactory.factory().displayOnce() || !ToastyClientSavedData.hasSeenToast(identifiedFactory))) {
            Minecraft.getInstance().getToasts().addToast(identifiedFactory.factory().createToast());
            identifiedFactory.factory().afterSeen(identifiedFactory.location());
        }
    }

}
