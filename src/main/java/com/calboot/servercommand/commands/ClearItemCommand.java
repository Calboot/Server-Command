package com.calboot.servercommand.commands;

import com.calboot.servercommand.ServerCommandSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;

import java.util.LinkedList;

public final class ClearItemCommand {

    private static int clearItem(final CommandContext<ServerCommandSource> context) {
        val source  = context.getSource();
        val targets = new LinkedList<ItemEntity>();
        source.getServer().getWorlds().forEach(world -> targets.addAll(world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), itemEntity -> true)));
        if (targets.isEmpty()) {
            source.sendFeedback(Text.translatable("command.servercommand.clearitem.none"), true);
            return 0;
        }
        targets.forEach(Entity::kill);
        if (targets.size() == 1) {
            source.sendFeedback(Text.translatable("command.servercommand.clearitem.single"), true);
            return 1;
        } else {
            source.sendFeedback(Text.translatable("command.servercommand.clearitem.multiple", targets.size()), true);
            return targets.size();
        }
    }

    public static void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        val command = CommandManager
                .literal("clearitem")
                .requires(source -> ServerCommandSettings.commandClearItem)
                .executes(ClearItemCommand::clearItem);
        dispatcher.register(command);
    }

}
