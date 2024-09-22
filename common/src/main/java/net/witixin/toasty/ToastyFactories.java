package net.witixin.toasty;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.witixin.toasty.factories.FactoryType;
import net.witixin.toasty.factories.SimpleToastFactory;
import net.witixin.toasty.factories.ToastFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ToastyFactories {

    private static final Map<ResourceLocation, FactoryType> KNOWN_TOASTS = new ConcurrentHashMap<>();

    public static final Codec<FactoryType> FACTORY_TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(rl -> {
        FactoryType factoryType = KNOWN_TOASTS.get(rl);
        if (factoryType != null) return DataResult.success(factoryType);
        else return DataResult.error(() -> "Key " + rl.toString() + " is not registered!");
    }, FactoryType::identifier);

    public static final Codec<ToastFactory<?>> FACTORY_CODEC = ToastyFactories.FACTORY_TYPE_CODEC.dispatch("toast_factory",
            ToastFactory::factoryType, type -> type.codec().fieldOf("factory"));

    public static void init() {
        registerFactoryType(SimpleToastFactory.TYPE);
    }

    public static boolean isValidFactory(ResourceLocation type) {
        return KNOWN_TOASTS.containsKey(type);
    }

    public static void registerFactoryType(FactoryType factoryType) {
        if (KNOWN_TOASTS.containsKey(factoryType.identifier())) throw new RuntimeException("That factory type is already registered!");
        KNOWN_TOASTS.put(factoryType.identifier(), factoryType);
    }

    public static void clear() {
        KNOWN_TOASTS.clear();
    }

}
