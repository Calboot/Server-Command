package com.calboot.servercommand.commands;

import com.calboot.servercommand.ServerCommandSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import lombok.val;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class TptCommand {

    private static int teleportTo(final CommandContext<ServerCommandSource> context) {
        val source = context.getSource();
        val p1     = source.getPlayer();
        if (p1 == null) {
            return -1;
        }
        val p2name = StringArgumentType.getString(context, "player");
        val p2     = source.getServer().getPlayerManager().getPlayer(p2name);
        if (p2 != null) {
            p1.teleport(p2.getWorld(), p2.getX(), p2.getY(), p2.getZ(), p2.getYaw(), p2.getPitch());
            source.sendFeedback(Text.translatable("command.servercommand.tpt.success", p1.getName(), p2.getName()), true);
            return 0;
        }
        source.sendError(Text.translatable("command.servercommand.tpt.playernotfound", p2name));
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
                                    val names  = context.getSource().getPlayerNames();
                                    val player = context.getSource().getPlayer();
                                    if (player != null) {
                                        names.remove(player.getName().getString());
                                    }
                                    return CommandSource.suggestMatching(names, builder);
                                }))
                                .executes(TptCommand::teleportTo)
                );
        dispatcher.register(command);
    }

}
