package com.github.touhoumaidaffection.mixin.client;

import com.github.touhoumaidaffection.client.LapPillowClientState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityLapPillowPassengerMixin {
    private static final ThreadLocal<Boolean> touhou_maid_affection$RECURSION_GUARD = ThreadLocal.withInitial(() -> false);

    @Inject(method = "isPassenger", at = @At("HEAD"), cancellable = true)
    private void touhou_maid_affection$isPassenger(CallbackInfoReturnable<Boolean> cir) {
        if (touhou_maid_affection$RECURSION_GUARD.get()
                || LapPillowClientState.renderingDepth <= 0
                || !((Object) this instanceof AbstractClientPlayer player)) {
            return;
        }

        touhou_maid_affection$RECURSION_GUARD.set(true);
        try {
            if (LapPillowClientState.shouldUseSleepPoseBridge(player)) {
                cir.setReturnValue(false);
            }
        } finally {
            touhou_maid_affection$RECURSION_GUARD.set(false);
        }
    }

    @Inject(method = "getVehicle", at = @At("HEAD"), cancellable = true)
    private void touhou_maid_affection$getVehicle(CallbackInfoReturnable<Entity> cir) {
        if (touhou_maid_affection$RECURSION_GUARD.get()
                || LapPillowClientState.renderingDepth <= 0
                || !((Object) this instanceof AbstractClientPlayer player)) {
            return;
        }

        touhou_maid_affection$RECURSION_GUARD.set(true);
        try {
            if (LapPillowClientState.shouldUseSleepPoseBridge(player)) {
                cir.setReturnValue(null);
            }
        } finally {
            touhou_maid_affection$RECURSION_GUARD.set(false);
        }
    }

    // On 1.21.1 this injection lived in LivingEntityLapPillowSleepMixin, because that runtime keeps
    // official method names and Mixin can resolve "isSwimming" through inheritance. On 1.20.1 the
    // runtime is SRG-named, and the annotation processor only maps members declared by the mixin
    // target itself: "isSwimming" is declared by Entity, not by LivingEntity, so a LivingEntity
    // target silently produced no refmap entry and the injection never applied. Injecting from
    // Entity - the declaring class - maps it normally. The handler condition is unchanged.
    @Inject(method = "isSwimming", at = @At("HEAD"), cancellable = true, require = 0)
    private void touhou_maid_affection$disableSwimmingForLapPillowState(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractClientPlayer player
                && LapPillowClientState.shouldUseSleepPoseBridge(player)) {
            cir.setReturnValue(false);
        }
    }
}
