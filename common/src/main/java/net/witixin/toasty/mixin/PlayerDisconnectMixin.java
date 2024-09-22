package net.witixin.toasty.mixin;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.witixin.toasty.ToastyClientSavedData;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true, print = true)
@Mixin(ClientCommonPacketListenerImpl.class)
public class PlayerDisconnectMixin {

    @Inject(method = "handleDisconnect", at = @At(value = "TAIL"))
    private void afterLogout(ClientboundDisconnectPacket packet, CallbackInfo callbackInfo) {
        ToastyClientSavedData.disconnectFromServer();
    }
}
