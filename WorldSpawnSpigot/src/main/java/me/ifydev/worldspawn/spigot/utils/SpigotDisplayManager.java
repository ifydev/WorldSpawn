package me.ifydev.worldspawn.spigot.utils;

import me.ifydev.worldspawn.api.WorldSpawnConstants;
import me.ifydev.worldspawn.api.manager.AbstractDisplayManager;
import org.bukkit.Bukkit;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public class SpigotDisplayManager implements AbstractDisplayManager {

    @Override
    public void sendErrorMessage(WorldSpawnConstants.Permissions permission, String error, boolean report) {
        Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(permission.getPermission())).forEach(p -> p.sendMessage(""));  // TODO
    }
}
