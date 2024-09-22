package net.witixin.toasty.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.witixin.toasty.ToastyConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class RegisterReloadListenerMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V",
            shift = At.Shift.AFTER), method = "<init>")
    private void registerReloadListener(GameConfig config, CallbackInfo callbackInfo) {
        ToastyConstants.registerReloadListener();
    }
}
