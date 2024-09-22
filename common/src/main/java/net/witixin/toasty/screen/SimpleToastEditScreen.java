package net.witixin.toasty.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.witixin.toasty.factories.SimpleToastFactory;
import net.witixin.toasty.platform.ToastyServices;
import net.witixin.toasty.resources.ToastyLoadingData;
import net.witixin.toasty.toasts.TargettedToastSource;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleToastEditScreen extends Screen {

    private Component title;
    private Component text;
    private ItemStack stack;
    private int displayTimeInMilliseconds;
    private boolean displayOnce;
    private String fileName;

    private List<String> requiredModids = new ArrayList<>();
    private List<String> requiredLoaders = new ArrayList<>();

    private List<TargettedToastSource> sources = new ArrayList<>();

    public SimpleToastEditScreen() {
        super(Component.translatable("toasty.edit_screen"));

        this.title = Component.translatable("toasty.default.toast.title");
        this.text = Component.translatable("toasty.default.toast.body");
        this.stack = Items.BREAD.getDefaultInstance();
        this.displayTimeInMilliseconds = SimpleToastFactory.DEFAULT_TIME_IN_MILLISECONDS;
        this.displayOnce = false;
        this.fileName = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath() + "_hint";

        this.addRenderableWidget(Button.builder(Component.translatable("toasty.default.toast.showcase"),
                ((btn) -> Minecraft.getInstance().getToasts().addToast(new SimpleToastFactory(stack, title, text, displayTimeInMilliseconds,
                        displayOnce).createToast()))).size(64, 16).pos(16, 32).build());

        this.addRenderableWidget(Button.builder(Component.translatable("toasty.default.toast.write_to_disk"),
                ((btn -> writeToDisk(this)))).size(64, 16).pos(128, 32).build());

    }

    private static void writeToDisk(SimpleToastEditScreen screen) {
        SimpleToastFactory factory = new SimpleToastFactory(screen.stack, screen.title, screen.text, screen.displayTimeInMilliseconds,
                screen.displayOnce);
        ToastyLoadingData data = new ToastyLoadingData(optionalList(screen.requiredLoaders), optionalList(screen.requiredModids),
                screen.sources, factory);
        DataResult<JsonElement> result = ToastyLoadingData.CODEC.encode(data, JsonOps.INSTANCE, new JsonObject());

        try {
            Path gamePath = ToastyServices.PLATFORM.getGameDirectory();
            Path toastyDirectory = gamePath.resolve("toasty/custom_toasts");
            if (!Files.exists(toastyDirectory)) {
                Files.createDirectories(toastyDirectory);
            }
            String fileName = screen.fileName + ".json";
            Path file = toastyDirectory.resolve(fileName);
            if (Files.exists(file)) {
                //error toast
                Minecraft.getInstance().getToasts().addToast(new SimpleToastFactory(Items.BARRIER.getDefaultInstance(),
                        Component.translatable("toasty.screen.error.title_file_exists"), Component.translatable("toasty.screen.error" +
                        ".body_file_exists"), SimpleToastFactory.DEFAULT_TIME_IN_MILLISECONDS, false).createToast());
            } else {
                //Ensure it can also be decoded
                ToastyLoadingData.CODEC.decode(JsonOps.INSTANCE, result.getOrThrow()).getOrThrow();

                JsonWriter writer = new JsonWriter(new FileWriter(file.toFile()));
                writer.setIndent("  ");
                GsonHelper.writeValue(writer, result.getOrThrow(), null);
                writer.flush();
                writer.close();

                //Success toast
                Minecraft.getInstance().getToasts().addToast(new SimpleToastFactory(Items.TORCH.getDefaultInstance(),
                        Component.translatable("toasty.screen.success.title"), Component.translatable("toasty.screen.success.body"),
                        SimpleToastFactory.DEFAULT_TIME_IN_MILLISECONDS, false).createToast());
            }

        } catch (IOException | IllegalStateException e) {
            Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastId.FILE_DROP_FAILURE,
                    Component.translatable("toasty.screen.error.title"), Component.translatable("toasty.screen.error.body")));

            e.printStackTrace();
        }
    }

    private static Optional<List<String>> optionalList(List<String> list) {
        return list.isEmpty() ? Optional.empty() : Optional.of(list);
    }
}
