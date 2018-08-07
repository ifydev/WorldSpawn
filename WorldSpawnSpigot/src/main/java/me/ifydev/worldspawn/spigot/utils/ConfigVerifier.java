package me.ifydev.worldspawn.spigot.utils;

import me.ifydev.worldspawn.api.backend.Backend;
import me.ifydev.worldspawn.api.backend.ConnectionInformation;
import me.ifydev.worldspawn.spigot.WorldSpawnMain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public class ConfigVerifier {

    public static Optional<ConnectionInformation> getConnectionInformation() {
        WorldSpawnMain plugin = WorldSpawnMain.getInstance();

        Optional<Backend.Types> type = Backend.Types.findType(plugin.getConfig().getString("storage", "sqlite"));
        if (!type.isPresent()) return Optional.empty();

        ConnectionInformation connectionInformation;

        if (type.get() == Backend.Types.MySQL) {
            if (plugin.getConfig().getString("connection.host") == null) return Optional.empty();
            if (plugin.getConfig().getString("connection.database") == null) return Optional.empty();
            if (plugin.getConfig().getString("connection.port") == null) return Optional.empty();
            if (plugin.getConfig().getString("connection.username") == null) return Optional.empty();
            if (plugin.getConfig().getString("connection.password") == null) return Optional.empty();

            connectionInformation = new ConnectionInformation(
                    plugin.getConfig().getString("connection.host"),
                    plugin.getConfig().getString("connection.database"),
                    plugin.getConfig().getInt("connection.port"),
                    plugin.getConfig().getString("connection.username"),
                    plugin.getConfig().getString("connection.password"),
                    new HashMap<>(),
                    type.get());
        } else if (type.get() == Backend.Types.SQLite) {
            if (plugin.getConfig().getString("connection.file") == null) return Optional.empty();

            Map<String, Object> sqliteMeta = new HashMap<>();
            sqliteMeta.put("file", plugin.getDataFolder() + "/" + plugin.getConfig().getString("connection.file"));
            Map<String, Object> meta = new HashMap<>();
            meta.put("sqlite", sqliteMeta);

            connectionInformation = new ConnectionInformation("", "", 0, "", "", meta, type.get());
        } else return Optional.empty();

        return Optional.of(connectionInformation);
    }
}
