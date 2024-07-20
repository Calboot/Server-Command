package com.calboot.servercommand.sounds;

import com.calboot.servercommand.ServerCommand;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class TraderSpawnedSound {

    public static final Identifier ID = new Identifier(ServerCommand.MOD_ID, "trader_spawned");

    public static SoundEvent SOUND = SoundEvent.of(ID);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, ID, SOUND);
    }

    public static void playFor(final ServerPlayerEntity player) {
        player.networkHandler.sendPacket(
                new PlaySoundS2CPacket(
                        RegistryEntry.of(SOUND),
                        SoundCategory.VOICE,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        2F,
                        1F,
                        player.getWorld().getRandom().nextLong()
                )
        );
    }

}
