package com.calboot.servercommand;

import carpet.api.settings.Rule;

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

}
