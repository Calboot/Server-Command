package com.calboot.servercommand.mixins;

import com.calboot.servercommand.ServerCommandSettings;
import com.calboot.servercommand.Utils;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Unique
    private RegistryKey<World> worldRegistryKey;

    @Unique
    private boolean thunderingPreviousValue;

    @Shadow
    @Final
    private ServerWorldProperties worldProperties;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(
            final MinecraftServer server,
            final Executor workerExecutor,
            final LevelStorage.Session session,
            final ServerWorldProperties properties,
            final RegistryKey<World> worldKey,
            final DimensionOptions dimensionOptions,
            final WorldGenerationProgressListener worldGenerationProgressListener,
            final boolean debugWorld,
            final long seed,
            final List<Spawner> spawners,
            final boolean shouldTickTime,
            final CallbackInfo ci) {
        worldRegistryKey = worldKey;
        thunderingPreviousValue = worldProperties.isThundering();
    }

    @Inject(method = "tickWeather", at = @At("RETURN"))
    private void onTickWeather(final CallbackInfo ci) {
        // Tracking thunderstorms
        if (isCurrentlyThundering() != thunderingPreviousValue
                && isThisOverworld()
                && ServerCommandSettings.trackOverworldThunderstorm
        ) {
            val server = ((ServerWorld) (Object) this).getServer();
            val txt = isCurrentlyThundering()
                    ? Text.translatable("message.servercommand.thunder.started")
                    : Text.translatable("message.servercommand.thunder.stopped");
            server.getPlayerManager().getPlayerList().forEach(player -> player.sendMessage(txt));
            server.sendMessage(txt);
            thunderingPreviousValue = isCurrentlyThundering();
        }
    }

    @Inject(method = "spawnEntity", at = @At("RETURN"))
    private void onEntitySpawned(final Entity entity, final CallbackInfoReturnable<Boolean> cir) {
        // Tracking lightnings
        if (EntityType.LIGHTNING_BOLT.equals(entity.getType())
                && isThisOverworld()
                && ServerCommandSettings.trackOverworldLightnings
                && cir.getReturnValue()
        ) {
            val blockPos = ((LightningEntityMixin) entity).getStuckBlockPos();
            val server   = ((ServerWorld) (Object) this).getServer();
            val players  = server.getPlayerManager().getPlayerList();
            val txt = Text.translatable(
                    "message.servercommand.lightning_tracked",
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ());
            if (ServerCommandSettings.trackOverworldLightningsMaxDistant > 0) {
                players
                        .stream()
                        .filter(player -> Utils.getSquaredDistance2D(blockPos, player.getBlockPos()) <= MathHelper.square(ServerCommandSettings.trackOverworldLightningsMaxDistant))
                        .forEach(player -> player.sendMessage(txt, false));
            } else {
                players.forEach(player -> player.sendMessage(txt));
            }
            server.sendMessage(txt);
        }
    }

    @Unique
    private boolean isThisOverworld() {
        return World.OVERWORLD.equals(worldRegistryKey);
    }

    @Unique
    private boolean isCurrentlyThundering() {
        return worldProperties.isRaining() && worldProperties.isThundering();
    }

}
