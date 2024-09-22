package net.witixin.toasty.toasts.sources;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.witixin.toasty.ToastyClientSavedData;
import net.witixin.toasty.ToastyConstants;
import net.witixin.toasty.factories.IdentifiedToastFactory;
import net.witixin.toasty.toasts.ToastSource;

import java.util.ArrayList;
import java.util.List;

public class OnLoginToastSource implements ToastSource {

    public static final ResourceLocation ID = ToastyConstants.resourceLocation("on_login");

    private static final List<IdentifiedToastFactory> firstLoginToasts = new ArrayList<>();

    public static void processAll() {
        for (IdentifiedToastFactory identifiedToastFactory : firstLoginToasts) {
            if (!identifiedToastFactory.factory().displayOnce()) {
                Minecraft.getInstance().getToasts().addToast(identifiedToastFactory.factory().createToast());
            }
            else if (!ToastyClientSavedData.hasSeenToast(identifiedToastFactory)) {
                ToastyClientSavedData.markToastAsShown(identifiedToastFactory);
                Minecraft.getInstance().getToasts().addToast(identifiedToastFactory.factory().createToast());
            }
        }
    }

    @Override
    public void registerFactory(ResourceLocation target, IdentifiedToastFactory factory) {
        firstLoginToasts.add(factory);
    }

    @Override
    public void beforeReloadCallback() {
        firstLoginToasts.clear();
    }

    @Override
    public ResourceLocation getToastSourceLocation() {
        return ID;
    }
}
