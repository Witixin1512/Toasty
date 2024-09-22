package net.witixin.toasty.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.witixin.toasty.ToastyFactories;
import net.witixin.toasty.factories.ToastFactory;
import net.witixin.toasty.toasts.TargettedToastSource;

import java.util.List;
import java.util.Optional;

public record ToastyLoadingData(Optional<List<String>> loaders, Optional<List<String>> requiredMods, List<TargettedToastSource> sources,
                                ToastFactory<?> factory) {

    public static final Codec<ToastyLoadingData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.optionalField(
            "loaders", Codec.STRING.listOf(), true).forGetter(ToastyLoadingData::loaders), Codec.optionalField("modids",
            Codec.STRING.listOf(), true).forGetter(ToastyLoadingData::requiredMods), TargettedToastSource.CODEC.listOf().fieldOf(
                    "toast_source").forGetter(ToastyLoadingData::sources),
            ToastyFactories.FACTORY_CODEC.fieldOf("toast").forGetter(ToastyLoadingData::factory)).apply(instance, ToastyLoadingData::new));

    public static ToastyLoadingData noRestrictions(List<TargettedToastSource> sourcesLocations, ToastFactory<?> factory) {
        return new ToastyLoadingData(Optional.empty(), Optional.empty(), sourcesLocations, factory);
    }

    public static ToastyLoadingData noRestrictions(TargettedToastSource sourceLocation, ToastFactory<?> factory) {
        return new ToastyLoadingData(Optional.empty(), Optional.empty(), List.of(sourceLocation), factory);
    }
}
