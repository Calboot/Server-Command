package com.calboot.servercommand.commands;

import com.calboot.servercommand.ServerCommandSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameMode;

public final class SCommand {

    private static int survival(final CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().getPlayerOrThrow().changeGameMode(GameMode.SURVIVAL);
        return 0;
    }

    public static void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        val command = CommandManager
                .literal("s")
                .requires(serverCommandSource -> ServerCommandSettings.commandS)
                .executes(SCommand::survival);
        dispatcher.register(command);
    }

}
