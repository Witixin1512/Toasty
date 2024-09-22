package net.witixin.toasty.factories;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public record FactoryType(Codec<? extends ToastFactory<?>> codec, ResourceLocation identifier) {
}
