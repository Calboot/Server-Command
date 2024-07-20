package com.calboot.servercommand;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import com.calboot.servercommand.commands.*;
import com.calboot.servercommand.sounds.TraderSpawnedSound;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public final class ServerCommand implements CarpetExtension, ModInitializer {

    public static final String MOD_ID = "servercommand";

    public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().toString();

    public static final String MOD_NAME = "Server Command";

    public static final ServerCommand INSTANCE = new ServerCommand();

    public final SettingsManager settingsManager = new SettingsManager(MOD_VERSION, MOD_ID, MOD_NAME);

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(INSTANCE);
    }

    @Override
    public void onGameStarted() {
        settingsManager.parseSettingsClass(ServerCommandSettings.class);
        registerSoundEvents();
    }

    @Override
    public void registerCommands(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess commandRegistryAccess) {
        SuicideCommand.register(dispatcher);
        ClearItemCommand.register(dispatcher);
        TptCommand.register(dispatcher);
        CCommand.register(dispatcher);
        SCommand.register(dispatcher);
        ShowSeedCommand.register(dispatcher);
    }

    @Override
    public SettingsManager extensionSettingsManager() {
        return settingsManager;
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    @Override
    public Map<String, String> canHasTranslations(final String lang) {
        final InputStream langStream = ServerCommand.class.getClassLoader().getResourceAsStream("assets/servercommand/lang/%s.json".formatted(lang));
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
        return new GsonBuilder().setLenient().create().fromJson(jsonData, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public void registerSoundEvents() {
        TraderSpawnedSound.register();
    }

}
