package net.witixin.toasty;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.witixin.toasty.resources.ToastyLoadingData;

import java.util.concurrent.CompletableFuture;

public abstract class BaseToastProvider extends JsonCodecProvider<ToastyLoadingData> {

    public BaseToastProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                             ExistingFileHelper existingFileHelper) {
        super(output, PackOutput.Target.RESOURCE_PACK, "toasts", PackType.CLIENT_RESOURCES, ToastyLoadingData.CODEC, lookupProvider,
                ToastyConstants.MOD_ID, existingFileHelper);
    }

}
