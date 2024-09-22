package net.witixin.toasty;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.witixin.toasty.resources.ToastyJsonReloadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToastyConstants {

    public static final String MOD_ID = "toasty";
    public static final String MOD_NAME = "Toasty";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void initialise() {
        ToastyFactories.init();
        ToastySources.init();
    }

    public static ResourceLocation resourceLocation(final String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void registerReloadListener() {
        if (Minecraft.getInstance() != null) {
            ReloadableResourceManager resourceManager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
            ToastyJsonReloadListener toastReloadListener = new ToastyJsonReloadListener("toasts");
            resourceManager.registerReloadListener(toastReloadListener);
        }
    }
}