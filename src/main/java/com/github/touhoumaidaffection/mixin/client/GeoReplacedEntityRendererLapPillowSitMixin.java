package com.github.touhoumaidaffection.mixin.client;

import com.github.touhoumaidaffection.TouhouMaidAffection;
import com.github.touhoumaidaffection.client.LapPillowClientState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.AnimatableEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoReplacedEntityRenderer")
public abstract class GeoReplacedEntityRendererLapPillowSitMixin {
    // The target method lives in Touhou Little Maid's bundled GeckoLib, which is not obfuscated, so
    // the method reference itself must not be remapped; the @At member is a Minecraft one, so it
    // keeps remap = true and still lands in the generated refmap.
    @Redirect(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;Lcom/github/tartaricacid/touhoulittlemaid/geckolib3/core/AnimatableEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;shouldRiderSit()Z", remap = true),
            require = 0,
            remap = false
    )
    private boolean touhou_maid_affection$preferSleepPoseDuringLapPillowInGeoRenderer(
            Entity vehicle,
            LivingEntity entity,
            AnimatableEntity animatable,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        if (entity instanceof AbstractClientPlayer player && LapPillowClientState.shouldUseSleepPoseBridge(player)) {
            if (player.tickCount % 40 == 0) {
                TouhouMaidAffection.LOGGER.info(
                        "[LapPillow] Geo render sit-bridge override: player={} vehicle={} forcedPose={} bridge=true",
                        player.getScoreboardName(),
                        vehicle.getType().toShortString(),
                        player.getForcedPose()
                );
            }
            return false;
        }
        return vehicle.shouldRiderSit();
    }
}
