package net.witixin.toasty.factories;

import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.resources.ResourceLocation;
import net.witixin.toasty.ToastyClientSavedData;

public interface ToastFactory<T extends Toast> {

    ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/recipe");
    int DEFAULT_TIME_IN_MILLISECONDS = 5000;

    T createToast();

    FactoryType factoryType();

    boolean displayOnce();

    default void afterSeen(ResourceLocation key) {
        if (displayOnce()) {
            ToastyClientSavedData.markToastAsShown(this, key);
        }
    }
}
