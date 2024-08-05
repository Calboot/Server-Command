package com.calboot.servercommand;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.val;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Utils {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static Map<String, Map<String, String>> translations = new HashMap<>(2);

    private static Map<String, String> getTranslation0(final String lang) {
        val langStream = ServerCommand.class.getClassLoader().getResourceAsStream("assets/servercommand/lang/%s.json".formatted(lang));
        if (langStream == null) {
            // we don't have that language
            return Collections.emptyMap();
        }
        final String jsonData;
        try {
            jsonData = new String(langStream.readAllBytes(), StandardCharsets.UTF_8);
            langStream.close();
        } catch (final IOException e) {
            return Collections.emptyMap();
        }
        return GSON.fromJson(jsonData, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public static double getSquaredDistance2D(final BlockPos pos1, final BlockPos pos2) {
        return MathHelper.square(pos1.getX() - pos2.getX())
                + MathHelper.square(pos1.getZ() - pos2.getZ());
    }

    public static void teleportTo(final ServerPlayerEntity p1, final ServerPlayerEntity p2) {
        p1.teleport(p2.getWorld(), p2.getX(), p2.getY(), p2.getZ(), p2.getYaw(), p2.getPitch());
    }

    public static void playSoundFor(final ServerPlayerEntity player, final SoundEvent sound, final SoundCategory category) {
        player.networkHandler.sendPacket(
                new PlaySoundS2CPacket(
                        RegistryEntry.of(sound),
                        category,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        1F,
                        1F,
                        player.getWorld().getRandom().nextLong()
                )
        );
    }

    public static Map<String, String> getTranslation(final String lang) {
        if (translations.containsKey(lang)) {
            return translations.get(lang);
        }
        val res = getTranslation0(lang);
        translations.put(lang, res);
        return res;
    }

}
