package me.ifydev.worldspawn.spigot;

import me.ifydev.worldspawn.api.WorldSpawnAPI;
import me.ifydev.worldspawn.api.backend.ConnectionInformation;
import me.ifydev.worldspawn.spigot.utils.ConfigVerifier;
import me.ifydev.worldspawn.spigot.utils.SpigotDisplayManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public class WorldSpawnMain extends JavaPlugin {

    private WorldSpawnAPI api;

    @Override
    public void onEnable() {
        Optional<ConnectionInformation> connectionInformation = ConfigVerifier.getConnectionInformation();
        if (!connectionInformation.isPresent()) {
            getLogger().severe("Could not find connection information in configuration. Please verify the options.");
            return;
        }

        api = new WorldSpawnAPI(connectionInformation.get(), new SpigotDisplayManager());
    }

    @Override
    public void onDisable() {
    }

    public static WorldSpawnMain getInstance() {
        return WorldSpawnMain.getPlugin(WorldSpawnMain.class);
    }

    public Optional<WorldSpawnAPI> getAPI() {
        return Optional.ofNullable(api);
    }
}
