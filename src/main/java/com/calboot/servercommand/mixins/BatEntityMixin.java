package com.calboot.servercommand.mixins;

import com.calboot.servercommand.ServerCommandSettings;
import net.minecraft.entity.passive.BatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BatEntity.class)
public class BatEntityMixin {

    @Inject(method = "canSpawn", at = @At("RETURN"), cancellable = true)
    private static void canSpawn(final CallbackInfoReturnable<Boolean> cir) {
        // Disabling bat spawning
        if (ServerCommandSettings.disableBatSpawning && cir.getReturnValue()) {
            cir.setReturnValue(false);
        }
    }

}
