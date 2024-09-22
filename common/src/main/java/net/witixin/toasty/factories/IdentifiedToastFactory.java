package net.witixin.toasty.factories;

import net.minecraft.resources.ResourceLocation;

public record IdentifiedToastFactory(ResourceLocation location, ToastFactory<?> factory) {
}
