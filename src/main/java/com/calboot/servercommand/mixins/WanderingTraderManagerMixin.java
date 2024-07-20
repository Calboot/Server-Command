package com.calboot.servercommand.mixins;

import com.calboot.servercommand.ServerCommandSettings;
import com.calboot.servercommand.sounds.TraderSpawnedSound;
import lombok.val;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin {

    @Accessor
    abstract ServerWorldProperties getProperties();

    @Inject(method = "spawn", at = @At("RETURN"))
    public void onTraderSpawn(final ServerWorld world, final boolean spawnMonsters, final boolean spawnAnimals, final CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() == 1) {
            val entity = world.getEntity(getProperties().getWanderingTraderId());
            if (entity != null) {
                if (ServerCommandSettings.wanderingTraderSpawnedMessage) {
                    val txt = Text.translatable("message.servercommand.trader_spawned", entity.getX(), entity.getY(), entity.getZ());
                    world.getServer().sendMessage(txt);
                    world.getServer().getPlayerManager().getPlayerList().forEach(player -> player.sendMessage(txt));
                }
                if (ServerCommandSettings.wanderingTraderSpawnedSound) {
                    world.getServer().getPlayerManager().getPlayerList().forEach(TraderSpawnedSound::playFor);
                }
            }
        }
    }

}
