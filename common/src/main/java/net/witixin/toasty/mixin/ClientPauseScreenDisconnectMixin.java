package net.witixin.toasty.mixin;

import net.minecraft.client.gui.screens.PauseScreen;
import net.witixin.toasty.ToastyClientSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class ClientPauseScreenDisconnectMixin {

    @Inject(method = "onDisconnect", at = @At(value = "TAIL"))
    private void onDisconnect(CallbackInfo callbackInfo) {
        ToastyClientSavedData.disconnectFromServer();
    }
}
