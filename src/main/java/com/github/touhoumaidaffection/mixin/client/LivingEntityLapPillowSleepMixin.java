package com.github.touhoumaidaffection.mixin.client;

import com.github.touhoumaidaffection.client.LapPillowClientState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLapPillowSleepMixin {
    @Inject(method = "isSleeping", at = @At("HEAD"), cancellable = true)
    private void touhou_maid_affection$bridgeLapPillowSleepingState(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractClientPlayer player
                && LapPillowClientState.renderingDepth > 0
                && LapPillowClientState.shouldUseSleepPoseBridge(player)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getBedOrientation", at = @At("HEAD"), cancellable = true)
    private void touhou_maid_affection$bridgeLapPillowBedOrientation(CallbackInfoReturnable<Direction> cir) {
        if ((Object) this instanceof AbstractClientPlayer player
                && LapPillowClientState.renderingDepth > 0
                && LapPillowClientState.shouldUseSleepPoseBridge(player)) {
            cir.setReturnValue(LapPillowClientState.resolveSleepDirection(player));
        }
    }

    @Inject(method = "getFallFlyingTicks", at = @At("HEAD"), cancellable = true)
    private void touhou_maid_affection$disableFallFlyingForLapPillowRender(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof AbstractClientPlayer player
                && LapPillowClientState.renderingDepth > 0
                && LapPillowClientState.shouldUseSleepPoseBridge(player)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "isFallFlying", at = @At("HEAD"), cancellable = true, require = 0)
    private void touhou_maid_affection$disableFallFlyingForLapPillowState(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractClientPlayer player
                && LapPillowClientState.shouldUseSleepPoseBridge(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isVisuallySwimming", at = @At("HEAD"), cancellable = true)
    private void touhou_maid_affection$disableVisualSwimmingForLapPillowRender(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractClientPlayer player
                && LapPillowClientState.renderingDepth > 0
                && LapPillowClientState.shouldUseSleepPoseBridge(player)) {
            cir.setReturnValue(false);
        }
    }

    // NOTE(1.20.1): the "isSwimming" bridge cannot live here. That method is declared by Entity, and
    // on a SRG-named runtime the annotation processor cannot produce a refmap entry for a member that
    // the LivingEntity target does not declare, which made the injection a silent no-op. Its
    // equivalent lives in EntityLapPillowPassengerMixin, which targets the declaring class.
}
