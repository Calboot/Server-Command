package com.calboot.servercommand.commands;

import com.calboot.servercommand.ServerCommandSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import lombok.val;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

public final class ShowSeedCommand {

    private static int showSeed(CommandContext<ServerCommandSource> context) {
        val  l    = context.getSource().getWorld().getSeed();
        Text text = Texts.bracketedCopyable(String.valueOf(l));
        context.getSource().sendFeedback(Text.translatable("commands.seed.success", text), false);
        return (int) l;
    }

    public static void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        val command = CommandManager
                .literal("showseed")
                .requires(s -> ServerCommandSettings.commandShowSeed)
                .executes(ShowSeedCommand::showSeed);
        dispatcher.register(command);
    }

}
