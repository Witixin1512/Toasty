package net.witixin.toasty.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.witixin.toasty.ToastyConstants;
import net.witixin.toasty.ToastySources;
import net.witixin.toasty.factories.IdentifiedToastFactory;
import net.witixin.toasty.platform.ToastyServices;
import net.witixin.toasty.toasts.TargettedToastSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ToastyJsonReloadListener extends SimpleJsonResourceReloadListener {

    public ToastyJsonReloadListener(String pDirectory) {
        super(new Gson(), pDirectory);
    }

    private static boolean isValidLoader(Optional<List<String>> allowedLoaders) {
        if (allowedLoaders.isEmpty()) return true;

        for (String loader : allowedLoaders.get()) {
            if (ToastyServices.PLATFORM.getPlatformName().equalsIgnoreCase(loader)) return true;
        }
        return false;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ToastySources.beforeReload();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            ResourceLocation jsonLocation = entry.getKey();
            JsonObject object = entry.getValue().getAsJsonObject();

            DataResult<ToastyLoadingData> result = ToastyLoadingData.CODEC.parse(JsonOps.INSTANCE, object);
            if (result.isError()) {
                ToastyConstants.LOG.error("Could not parse {}: {}", jsonLocation.toString(), result.error().get().message());
            } else {
                ToastyLoadingData toastyData = result.getOrThrow();
                if (!isValidLoader(toastyData.loaders())) continue;

                for (TargettedToastSource targettedSource : toastyData.sources()) {
                    ToastyConstants.LOG.info("Adding factory for file: {}", jsonLocation.toString());
                    for (ResourceLocation location : targettedSource.targets()) {
                        targettedSource.source().registerFactory(location, new IdentifiedToastFactory(jsonLocation, toastyData.factory()));
                    }
                }
            }

        }
    }
}
