package net.witixin.toasty.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.witixin.toasty.platform.services.IPlatformHelper;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    /**
     * Get the game directory
     *
     * @return The path of the game directory
     */
    @Override
    public Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }
}
