package com.calboot.servercommand.mixins;

import com.calboot.servercommand.ServerCommandSettings;
import lombok.val;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerEntityMixin {

    @Unique
    private static final StatusEffectInstance GLOWING_EFFECT_INSTANCE = new StatusEffectInstance(StatusEffects.GLOWING, 2);

    @Unique
    private StatusEffectInstance glowingEffect;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(final CallbackInfo ci) {
        val thisEntity = ((ZombieVillagerEntity) (Object) this);
        if (ServerCommandSettings.highlightZombieVillagers) {
            if (!thisEntity.hasStatusEffect(StatusEffects.GLOWING)) {
                thisEntity.addStatusEffect((glowingEffect = new StatusEffectInstance(GLOWING_EFFECT_INSTANCE)));
            }
        } else {
            if (glowingEffect != null) {
                ((StatusEffectInstanceMixin) glowingEffect).setDuration(0);
            }
        }
    }

}
