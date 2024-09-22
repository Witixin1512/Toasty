package net.witixin.toasty.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.witixin.toasty.ToastyEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDeathMixin extends Entity {
    public LivingEntityDeathMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At("TAIL"))
    private void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        if (this.level().isClientSide) {
            ToastyEventHandler.onEntityKill(((LivingEntity) (Object) this).getLastDamageSource(), this.getType());
        }
    }
}
