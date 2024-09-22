package net.witixin.toasty.toasts;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.witixin.toasty.ToastySources;
import net.witixin.toasty.factories.IdentifiedToastFactory;

import java.util.List;

public interface ToastSource {

    Codec<List<ResourceLocation>> RL_LIST = Codec.either(ResourceLocation.CODEC, ResourceLocation.CODEC.listOf()).xmap(either -> {
        if (either.left().isPresent()) return List.of(either.left().get());
        else return either.right().get();
    }, Either::right);

    Codec<ToastSource> CODEC = ResourceLocation.CODEC.comapFlatMap(

            location -> {
                if (ToastySources.isValidToastSource(location)) return DataResult.success(ToastySources.getSource(location));
                else
                    return DataResult.error(() -> "Invalid id given: " + location.toString() + " as it is not a valid toast source " +
                            "identifier!");
            }, ToastSource::getToastSourceLocation);

    void registerFactory(ResourceLocation target, IdentifiedToastFactory factory);

    void beforeReloadCallback();

    ResourceLocation getToastSourceLocation();

}
