package com.calboot.servercommand.commands;

import com.calboot.servercommand.ServerCommandSettings;
import com.calboot.servercommand.Utils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class TptCommand {

    private static int teleportTo(final CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        val source = context.getSource();
        val p1     = source.getPlayerOrThrow();
        val p2name = StringArgumentType.getString(context, "player");
        val p2     = source.getServer().getPlayerManager().getPlayer(p2name);
        if (p2 != null) {
            Utils.teleportTo(p1, p2);
            val txt = Text.translatable("command.servercommand.tpt.success", p1.getName(), p2.getName());
            p1.sendMessage(txt);
            p2.sendMessage(txt);
            return 0;
        }
        source.sendError(Text.translatable("command.servercommand.tpt.player_not_found", p2name));
        return 1;
    }

    public static void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        val command = CommandManager
                .literal("tpt")
                .requires(serverCommandSource -> serverCommandSource.isExecutedByPlayer() && ServerCommandSettings.commandTpt)
                .then(
                        CommandManager
                                .argument("player", StringArgumentType.word())
                                .suggests(((context, builder) -> {
                                    val names = context.getSource().getPlayerNames();
                                    names.remove(context.getSource().getPlayerOrThrow().getName().getString());
                                    return CommandSource.suggestMatching(names, builder);
                                }))
                                .executes(TptCommand::teleportTo)
                );
        dispatcher.register(command);
    }

}
