package net.witixin.toasty;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(ToastyConstants.MOD_ID)
public class ToastyDummyMod {

    public ToastyDummyMod(IEventBus bus) {
        bus.addListener(ToastyDummyMod::runDatagen);
    }

    public static void runDatagen(final GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeClient(), new ToastySimpleProvider(event.getGenerator().getPackOutput(),
                event.getLookupProvider(), event.getExistingFileHelper()));
    }
}
