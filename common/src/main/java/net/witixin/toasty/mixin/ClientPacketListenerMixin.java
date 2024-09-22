package net.witixin.toasty.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.witixin.toasty.ToastyClientSavedData;
import net.witixin.toasty.ToastyEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {

    @Shadow
    private ClientLevel level;

    private ClientPacketListenerMixin(Minecraft pMinecraft, Connection pConnection, CommonListenerCookie pCommonListenerCookie) {
        super(pMinecraft, pConnection, pCommonListenerCookie);
    }

    @Inject(method = "handleTakeItemEntity", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/item/ItemEntity;getItem()" + "Lnet/minecraft/world/item/ItemStack;"))
    private void afterItemPickup(ClientboundTakeItemEntityPacket pPacket, CallbackInfo callbackInfo) {
        LivingEntity livingentity = (LivingEntity) this.level.getEntity(pPacket.getPlayerId());
        if (livingentity == null) {
            livingentity = this.minecraft.player;
        }
        if (livingentity == this.minecraft.player) {
            ToastyEventHandler.onItemPickup(((ItemEntity) this.level.getEntity(pPacket.getItemId())).getItem());
        }
    }

    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
    private void afterLogin(ClientboundLoginPacket packet, CallbackInfo callbackInfo) {
        ToastyClientSavedData.joinWorld();
    }

}
