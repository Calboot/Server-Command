package com.calboot.servercommand;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static carpet.api.settings.RuleCategory.*;

public class ServerCommandSettings {

    public static final String SERVERCOMMAND = "servercommand";

    @Rule(
            categories = {FEATURE, EXPERIMENTAL, SERVERCOMMAND}
    )
    public static boolean disableBatSpawning = false;

    @Rule(
            categories = {EXPERIMENTAL, SERVERCOMMAND}
    )
    public static boolean wanderingTraderSpawnedMessage = false;

    @Rule(
            categories = {EXPERIMENTAL, SERVERCOMMAND}
    )
    public static boolean wanderingTraderSpawnedSound = false;

    @Rule(
            categories = {COMMAND, SERVERCOMMAND}
    )
    public static boolean commandC = false;

    @Rule(
            categories = {COMMAND, SERVERCOMMAND}
    )
    public static boolean commandS = false;

    @Rule(
            categories = {COMMAND, SERVERCOMMAND}
    )
    public static boolean commandClearItem = false;

    @Rule(
            categories = {COMMAND, SERVERCOMMAND}
    )
    public static boolean commandShowSeed = false;

    @Rule(
            categories = {COMMAND, SERVERCOMMAND}
    )
    public static boolean commandSuicide = false;

    @Rule(
            categories = {COMMAND, SERVERCOMMAND}
    )
    public static boolean commandTpt = false;

    @Rule(
            categories = {EXPERIMENTAL, SERVERCOMMAND}
    )
    public static boolean highlightZombieVillagers = false;

    @Rule(
            categories = {EXPERIMENTAL, SERVERCOMMAND}
    )
    public static boolean trackOverworldLightnings = false;

    @Rule(
            categories = {EXPERIMENTAL, SERVERCOMMAND},
            validators = RadiusValidator.class
    )
    public static int trackOverworldLightningsMaxDistant = 128;

    private static final class RadiusValidator extends Validator<Integer> {

        @Override
        public Integer validate(@Nullable final ServerCommandSource source, final CarpetRule<Integer> changingRule, final Integer newValue, final String userInput) {
            if (newValue < 0) {
                return null;
            }
            return newValue;
        }

        @Override
        public String description() {
            return "The radius must not be negative";
        }

    }

}
