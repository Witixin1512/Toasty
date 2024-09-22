package net.witixin.toasty.toasts.sources;

import net.minecraft.resources.ResourceLocation;
import net.witixin.toasty.ToastyConstants;
import net.witixin.toasty.ToastyEventHandler;
import net.witixin.toasty.factories.IdentifiedToastFactory;
import net.witixin.toasty.toasts.ToastSource;

public class ItemPickupToastSource implements ToastSource {

    public static final ResourceLocation ID = ToastyConstants.resourceLocation("item_pickup");

    @Override
    public ResourceLocation getToastSourceLocation() {
        return ID;
    }

    @Override
    public void beforeReloadCallback() {
        ToastyEventHandler.beforeItemReload();
    }

    @Override
    public void registerFactory(ResourceLocation target, IdentifiedToastFactory factory) {
        ToastyEventHandler.trackItem(target, factory);
    }

}
