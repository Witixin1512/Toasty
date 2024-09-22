package net.witixin.toasty;

import net.minecraft.resources.ResourceLocation;
import net.witixin.toasty.toasts.ToastSource;
import net.witixin.toasty.toasts.sources.EntityKillToastSource;
import net.witixin.toasty.toasts.sources.OnLoginToastSource;
import net.witixin.toasty.toasts.sources.ItemPickupToastSource;

import java.util.HashMap;
import java.util.Map;

public class ToastySources {
    private static final Map<ResourceLocation, ToastSource> TOAST_SOURCES = new HashMap<>();

    public static void init() {

        registerToastSource(new ItemPickupToastSource());
        registerToastSource(new EntityKillToastSource());
        registerToastSource(new OnLoginToastSource());
    }

    public static boolean isValidToastSource(ResourceLocation id) {
        return TOAST_SOURCES.containsKey(id);
    }

    public static ToastSource getSource(ResourceLocation id) {
        if (!isValidToastSource(id)) throw new RuntimeException("Given id: " + id.toString() + " is not a registered toast source!");
        return TOAST_SOURCES.get(id);
    }

    public static void registerToastSource(ToastSource source) {
        if (TOAST_SOURCES.containsKey(source.getToastSourceLocation()))
            throw new RuntimeException("That toast type is already registered!");
        TOAST_SOURCES.put(source.getToastSourceLocation(), source);
    }

    /**
     * Give each ToastSource a chance to do cleanup before a reload
     */
    public static void beforeReload() {
        for (ToastSource source : TOAST_SOURCES.values()) {
            source.beforeReloadCallback();
        }
    }
}
