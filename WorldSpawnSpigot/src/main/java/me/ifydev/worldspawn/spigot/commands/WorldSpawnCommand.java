package me.ifydev.worldspawn.spigot.commands;

import me.ifydev.worldspawn.api.WorldSpawnConstants;
import me.ifydev.worldspawn.spigot.commands.subcommands.BasicCommands;
import me.ifydev.worldspawn.spigot.utils.MiscUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public class WorldSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            // No arguments to do anything with
            sendResponse(WorldSpawnConstants.Responses.ALL, sender);
            return false;
        }

        String first = args[0];

        if (first.equalsIgnoreCase("set")) {
            String[] remaining = MiscUtil.getRemainingArgs(1, args);
            Optional<WorldSpawnConstants.Responses> response = BasicCommands.setWorldSpawn(sender, remaining);

            if (!response.isPresent()) return false;
            sendResponse(response.get(), sender);
            return true;
        }

        sendResponse(WorldSpawnConstants.Responses.ALL, sender);
        return false;
    }

    private void sendResponse(WorldSpawnConstants.Responses message, CommandSender sender) {
        Arrays.stream(message.getMessages()).map(MiscUtil::fixColors).forEach(sender::sendMessage);
    }
}
