package net.witixin.toasty;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.witixin.toasty.factories.FactoryType;
import net.witixin.toasty.factories.IdentifiedToastFactory;
import net.witixin.toasty.factories.ToastFactory;
import net.witixin.toasty.platform.ToastyServices;
import net.witixin.toasty.toasts.sources.OnLoginToastSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ToastyClientSavedData {

    private static final Codec<Map<FactoryType, Set<ResourceLocation>>> CODEC = Codec.unboundedMap(ToastyFactories.FACTORY_TYPE_CODEC,
            ResourceLocation.CODEC.listOf().xmap(HashSet::new, set -> set.stream().toList()));

    private static final Path TOASTY_SAVE_FILE = Path.of("seen_toasts.json");

    @Nullable
    static Path saveDirectory = null;

    static Map<FactoryType, Set<ResourceLocation>> seenFactories = new HashMap<>();

    static int unsavedCount = 0;

    public static void joinWorld() {
        String path = getPathForServer();
        if (path != null) {
            saveDirectory = ToastyServices.PLATFORM.getToastyDirectory().resolve(path);
            //parse seenFactories
            try {
                if (Files.notExists(saveDirectory)) Files.createDirectories(saveDirectory);
                else {
                    if (Files.exists(saveDirectory.resolve(TOASTY_SAVE_FILE))) {
                        FileReader reader = new FileReader(saveDirectory.resolve(TOASTY_SAVE_FILE).toFile());
                        JsonObject object = GsonHelper.parse(reader);
                        reader.close();
                        seenFactories.clear();
                        seenFactories.putAll(CODEC.decode(JsonOps.INSTANCE, object).getOrThrow().getFirst());
                    } else {
                        //The file does not exist
                        seenFactories.clear();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            onLogin();
        } else {
            ToastyConstants.LOG.error("Save location for server could not be acquired... Toasty will not save seen toasts to disk.");
        }
    }

    public static void disconnectFromServer() {
        //flush seen factories
        writeToDisk();
        seenFactories.clear();
        saveDirectory = null;
        unsavedCount = 0;
    }

    public static String getPathForServer() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hasSingleplayerServer()) {
            return "sp/" + minecraft.getSingleplayerServer().storageSource.getLevelId();
        } else if (minecraft.getCurrentServer() != null) {
            StringBuilder path = new StringBuilder("mp/");
            ServerData data = minecraft.getCurrentServer();
            if (data.isRealm()) {
                path.append("realm/");
            } else if (data.isLan()) {
                path.append("lan/");
            } else if (data.ping == 1) {
                path.append("localhost/");
            } else {
                path.append(minecraft.getCurrentServer().ip).append("_");
            }
            path.append(minecraft.getCurrentServer().name);
            return path.toString();
        }
        return null;
    }

    @ApiStatus.Internal
    public static void markToastAsShown(ToastFactory<?> factory, ResourceLocation key) {
        seenFactories.computeIfAbsent(factory.factoryType(), k -> new HashSet<>()).add(key);
        updateSeenToasts();
    }

    public static void markToastAsShown(IdentifiedToastFactory factory) {
        markToastAsShown(factory.factory(), factory.location());
    }

    private static void updateSeenToasts() {
        if (++unsavedCount > 10) {
            writeToDisk();
        }
    }

    public static boolean hasSeenToast(IdentifiedToastFactory factory) {
        final Set<ResourceLocation> seenToasts = seenFactories.get(factory.factory().factoryType());
        return seenToasts != null && seenToasts.contains(factory.location());
    }

    private static void writeToDisk() {
        if (saveDirectory == null) {
            ToastyConstants.LOG.error("Save location for server could not be acquired... Toasty will not save seen toasts to disk.");
            return;
        }
        try {
            if (Files.notExists(saveDirectory)) {
                Files.createDirectories(saveDirectory);
            }
            else {
                JsonWriter writer = new JsonWriter(new FileWriter(saveDirectory.resolve(TOASTY_SAVE_FILE).toFile()));
                writer.setIndent("  ");
                GsonHelper.writeValue(writer, CODEC.encode(seenFactories, JsonOps.INSTANCE, new JsonObject()).getOrThrow(), null);
                writer.flush();
                writer.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void onLogin() {
        OnLoginToastSource.processAll();
        updateSeenToasts();
    }
}
