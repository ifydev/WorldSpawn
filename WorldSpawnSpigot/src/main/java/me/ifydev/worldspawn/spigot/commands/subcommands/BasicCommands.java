package me.ifydev.worldspawn.spigot.commands.subcommands;

import me.ifydev.worldspawn.api.WorldSpawnConstants;
import me.ifydev.worldspawn.api.util.Result;
import me.ifydev.worldspawn.spigot.WorldSpawnMain;
import me.ifydev.worldspawn.spigot.utils.MiscUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public class BasicCommands {

    public static Optional<WorldSpawnConstants.Responses> setWorldSpawn(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return Optional.of(WorldSpawnConstants.Responses.YOU_ARE_NOT_A_PLAYER);
        if (!sender.hasPermission(WorldSpawnConstants.Permissions.SET.getPermission()))
            return Optional.of(WorldSpawnConstants.Responses.INSUFFICIENT_PERMISSIONS);

        WorldSpawnMain plugin = WorldSpawnMain.getInstance();
        if (!plugin.getAPI().isPresent()) return Optional.of(WorldSpawnConstants.Responses.PLUGIN_NOT_INITIALIZED);

        Player player = (Player) sender;
        me.ifydev.worldspawn.api.structures.Location location;
        if (args.length < 1) {
            Location bukkit = player.getLocation();
            location = new me.ifydev.worldspawn.api.structures.Location(
                    bukkit.getWorld().getName(), bukkit.getBlockX(), bukkit.getBlockY(), bukkit.getBlockZ());
        } else {
            if (args.length < 4) return Optional.of(WorldSpawnConstants.Responses.ARGS_SET);
            if (!MiscUtil.isInt(MiscUtil.getRemainingArgs(2, args))) return Optional.of(WorldSpawnConstants.Responses.LOCATION_MUST_BE_NUMBERS);

            String world = args[0];
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);
            location = new me.ifydev.worldspawn.api.structures.Location(world, x, y, z);
        }

        Result<String, WorldSpawnConstants.DatabaseErrors> result = plugin.getAPI().get().getBackend().addWorldSpawn(location);
        if (result.getErr().isPresent()) {
            // If we couldn't connect to the database then we don't need to report.
            boolean report = result.getErr().get().equals(WorldSpawnConstants.DatabaseErrors.COULD_NOT_CONNECT);
            // Broadcast the error.
            plugin.getAPI().get().getDisplayManager().sendErrorMessage(WorldSpawnConstants.Permissions.ADMIN, result.getErr().get().getFriendly(), report);
            return Optional.empty();
        }

        return Optional.of(WorldSpawnConstants.Responses.SPAWN_SET);
    }
}
