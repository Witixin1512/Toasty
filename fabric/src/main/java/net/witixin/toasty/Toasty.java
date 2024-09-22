package net.witixin.toasty;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.witixin.toasty.command.OpenToastEditScreenCommand;
import net.witixin.toasty.platform.ToastyServices;

public class Toasty implements ModInitializer {

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            ToastyConstants.initialise();
            if (ToastyServices.PLATFORM.isDevelopmentEnvironment()) {
                ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("toasty_maker").executes(context -> OpenToastEditScreenCommand.executeCommand())));
            }
        }
    }

}
