package com.calboot.servercommand.commands;

import com.calboot.servercommand.ServerCommandSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class SuicideCommand {

    private static int suicide(final CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().getPlayerOrThrow().kill();
        return 0;
    }

    public static void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        val command = CommandManager
                .literal("suicide")
                .requires(serverCommandSource -> ServerCommandSettings.commandSuicide)
                .executes(SuicideCommand::suicide);
        dispatcher.register(command);
    }

}
