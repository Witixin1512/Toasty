package net.witixin.toasty;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.witixin.toasty.factories.SimpleToastFactory;
import net.witixin.toasty.resources.ToastyLoadingData;
import net.witixin.toasty.toasts.TargettedToastSource;
import net.witixin.toasty.toasts.ToastSource;
import net.witixin.toasty.toasts.sources.EntityKillToastSource;
import net.witixin.toasty.toasts.sources.OnLoginToastSource;
import net.witixin.toasty.toasts.sources.ItemPickupToastSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToastySimpleProvider extends BaseToastProvider {

    private static final List<String> BOTH_LOADERS = List.of("Fabric", "NeoForge");
    private final ToastSource ITEM_PICKUP = ToastySources.getSource(ItemPickupToastSource.ID);
    private final ToastSource ENTITY_KILL = ToastySources.getSource(EntityKillToastSource.ID);
    private final ToastSource FIRST_LOGIN = ToastySources.getSource(OnLoginToastSource.ID);

    public ToastySimpleProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper);
    }

    @Override
    protected void gather() {
        /*unconditional(ToastyConstants.resourceLocation("iron_pickaxe"),
                ToastyLoadingData.noRestrictions(List.of(new TargettedToastSource(ITEM_PICKUP,
                        List.of(BuiltInRegistries.ITEM.getKey(Items.IRON_PICKAXE)))),
                        new SimpleToastFactory(Items.IRON_PICKAXE.getDefaultInstance(), Component.literal("Isn't it an Iron Pick"),
                                Component.literal("Can break diamond. Has 250 durability points. Swing responsibly."), 5000, true)));*/

        unconditional(ToastyConstants.resourceLocation("mod_icon"),
                ToastyLoadingData.noRestrictions(List.of(new TargettedToastSource(FIRST_LOGIN, List.of())),
                        new SimpleToastFactory(Items.BREAD.getDefaultInstance(), Component.literal("Toasty").withStyle(
                                style -> style.withBold(true)
                        ), Component.literal("Displays Toasts!").withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY)), 5000, true)));

        /*
        unconditional(ToastyConstants.resourceLocation("wither_kill"),
                ToastyLoadingData.noRestrictions(new TargettedToastSource(ENTITY_KILL,
                        List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CHICKEN))),
                        new SimpleToastFactory(Items.NETHER_STAR.getDefaultInstance(), Component.literal("Congratulations!"),
                                Component.literal("You have managed to kill a wither!"), 5000, true)));

         */
    }
}
