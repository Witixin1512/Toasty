package net.witixin.toasty.toasts.sources;

import net.minecraft.resources.ResourceLocation;
import net.witixin.toasty.ToastyConstants;
import net.witixin.toasty.ToastyEventHandler;
import net.witixin.toasty.factories.IdentifiedToastFactory;
import net.witixin.toasty.toasts.ToastSource;

public class EntityKillToastSource implements ToastSource {

    public static final ResourceLocation ID = ToastyConstants.resourceLocation("entity_kill");

    @Override
    public void registerFactory(ResourceLocation target, IdentifiedToastFactory factory) {
        ToastyEventHandler.trackEntityType(target, factory);
    }

    @Override
    public void beforeReloadCallback() {
        ToastyEventHandler.beforeEntityReload();
    }

    @Override
    public ResourceLocation getToastSourceLocation() {
        return ID;
    }

}
