package net.witixin.toasty.toasts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record TargettedToastSource(ToastSource source, List<ResourceLocation> targets) {

    public static final Codec<TargettedToastSource> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(ToastSource.CODEC.fieldOf("source_id").forGetter(TargettedToastSource::source), ToastSource.RL_LIST.fieldOf("targets").forGetter(TargettedToastSource::targets)).apply(instance, TargettedToastSource::new));
}
