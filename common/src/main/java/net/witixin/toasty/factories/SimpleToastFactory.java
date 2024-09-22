package net.witixin.toasty.factories;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.witixin.toasty.ToastyConstants;
import net.witixin.toasty.toasts.SimpleToast;

public record SimpleToastFactory(ItemStack stack, Component title, Component text, int displayTimeInMilliseconds,
                                 boolean displayOnce) implements ToastFactory<SimpleToast> {

    public static final ResourceLocation ID = ToastyConstants.resourceLocation("simple");
    public static final Codec<SimpleToastFactory> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(ItemStack.ITEM_NON_AIR_CODEC.xmap(item -> item.value().getDefaultInstance(), ItemStack::getItemHolder).fieldOf("item").forGetter(SimpleToastFactory::stack), ComponentSerialization.CODEC.fieldOf("title").forGetter(SimpleToastFactory::title), ComponentSerialization.CODEC.fieldOf("text").forGetter(SimpleToastFactory::text),

            Codec.INT.optionalFieldOf("display_time_in_milliseconds", DEFAULT_TIME_IN_MILLISECONDS).forGetter(SimpleToastFactory::displayTimeInMilliseconds),
                    Codec.BOOL.optionalFieldOf("display_once", false).forGetter(SimpleToastFactory::displayOnce)).apply(instance,
                    SimpleToastFactory::new));

    public static final FactoryType TYPE = new FactoryType(CODEC, ID);

    @Override
    public SimpleToast createToast() {
            return new SimpleToast(this);
    }

    @Override
    public FactoryType factoryType() {
        return TYPE;
    }

}
