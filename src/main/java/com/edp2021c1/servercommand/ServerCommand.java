package com.edp2021c1.servercommand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.GameMode;

import java.util.LinkedList;

public class ServerCommand implements ModInitializer {

    private void registerCommands(final CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager
                        .literal("suicide")
                        .executes(this::suicide)
        );

        dispatcher.register(
                CommandManager
                        .literal("clearitem")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .executes(this::clearItem)
        );

        dispatcher.register(
                CommandManager
                        .literal("tpt")
                        .then(
                                CommandManager
                                        .argument("player", EntityArgumentType.player())
                                        .executes(this::teleportTo)
                        )
        );

        dispatcher.register(
                CommandManager
                        .literal("c")
                        .executes(context -> changeGameMode(context, GameMode.SPECTATOR))
        );

        dispatcher.register(
                CommandManager
                        .literal("s")
                        .executes(context -> changeGameMode(context, GameMode.SURVIVAL))
        );
    }

    private int suicide(final CommandContext<ServerCommandSource> context) {
        val player = context.getSource().getPlayer();
        if (player != null) {
            player.kill();
            return 0;
        }
        return -1;
    }

    private int clearItem(final CommandContext<ServerCommandSource> context) {
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

    private int teleportTo(final CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        val source = context.getSource();
        val p1     = source.getPlayer();
        if (p1 == null) {
            return -1;
        }
        val p2 = (ServerPlayerEntity) EntityArgumentType.getEntity(context, "player");
        if (p2 != null) {
            p1.teleport(p2.getWorld(), p2.getX(), p2.getY(), p2.getZ(),p2.getYaw(),p2.getPitch());
            source.sendFeedback(Text.translatable("command.servercommand.tpt.success", p1.getName(), p2.getName()), true);
            return 0;
        }
        return 1;
    }

    private int changeGameMode(final CommandContext<ServerCommandSource> context, final GameMode gameMode) {
        val player = context.getSource().getPlayer();
        if (player != null) {
            player.changeGameMode(gameMode);
            return 0;
        }
        return -1;
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher));
    }

}
