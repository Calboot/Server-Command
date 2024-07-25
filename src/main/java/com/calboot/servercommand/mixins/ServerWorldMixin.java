package com.calboot.servercommand.mixins;

import com.calboot.servercommand.ServerCommandSettings;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Unique
    private RegistryKey<World> worldRegistryKey;

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
    }

    @Inject(method = "spawnEntity", at = @At("RETURN"))
    private void onEntitySpawned(final Entity entity, final CallbackInfoReturnable<Boolean> cir) {
        if (EntityType.LIGHTNING_BOLT.equals(entity.getType())
                && World.OVERWORLD.equals(worldRegistryKey)
                && ServerCommandSettings.trackOverworldLightnings
                && cir.getReturnValue()
        ) {
            val blockPos = ((LightningEntityMixin) entity).getStuckBlockPos();
            val server   = ((ServerWorld) (Object) this).getServer();
            val players  = server.getPlayerManager().getPlayerList();
            val txt      = Text.translatable("message.servercommand.lightning_tracked", blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (ServerCommandSettings.trackOverworldLightningsMaxDistant > 0) {
                players
                        .stream()
                        .filter(player -> getSquaredDistance2D(blockPos, player.getBlockPos()) <= MathHelper.square(ServerCommandSettings.trackOverworldLightningsMaxDistant))
                        .forEach(player -> player.sendMessage(txt, false));
            } else {
                players.forEach(player -> player.sendMessage(txt));
            }
            server.sendMessage(txt);
        }
    }

    @Unique
    private double getSquaredDistance2D(final BlockPos pos1, final BlockPos pos2) {
        return MathHelper.square(pos1.getX() - pos2.getX()) + MathHelper.square(pos1.getZ() - pos2.getZ());
    }

}
