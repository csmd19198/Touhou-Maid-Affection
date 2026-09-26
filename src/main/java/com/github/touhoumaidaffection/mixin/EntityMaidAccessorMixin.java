package com.github.touhoumaidaffection.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Exposes the protected {@code Entity#getTypeName} so {@code MaidDisplayNameResolver} can ask a maid
 * for the name of her type.
 *
 * <p>Deliberately mixed into {@link Entity} rather than into Touhou Little Maid's {@code EntityMaid}:
 * {@code getTypeName} is a vanilla method, so its runtime name is the SRG one, and the mixin
 * annotation processor can only look that mapping up when the target class is a Minecraft class.
 * Targeting the mod class made the processor emit "unable to locate obfuscation mapping" and the
 * invoker then failed at runtime. Mixing into Entity injects the invoker into the base class; calling
 * it on a maid instance still dispatches to her override.
 */
@Mixin(Entity.class)
public interface EntityMaidAccessorMixin {
    @Invoker("getTypeName")
    Component touhou_maid_affection$invokeGetTypeName();
}
