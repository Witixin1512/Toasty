package net.witixin.toasty;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.witixin.toasty.command.OpenToastEditScreenCommand;
import net.witixin.toasty.platform.ToastyServices;

@Mod(value = ToastyConstants.MOD_ID, dist = Dist.CLIENT)
public class Toasty {

    public Toasty(IEventBus eventBus) {

        ToastyConstants.initialise();
        ToastyConstants.registerReloadListener();
        if (ToastyServices.PLATFORM.isDevelopmentEnvironment()) {
            NeoForge.EVENT_BUS.<RegisterClientCommandsEvent>addListener(event -> OpenToastEditScreenCommand.register(event.getDispatcher()));
        }
        NeoForge.EVENT_BUS.<LivingDeathEvent>addListener(event -> {
            if (event.getEntity().level().isClientSide)
                ToastyEventHandler.onEntityKill(event.getEntity().getLastDamageSource(), event.getEntity().getType());
        });
    }

}