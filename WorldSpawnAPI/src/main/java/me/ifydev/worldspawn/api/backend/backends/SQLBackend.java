package me.ifydev.worldspawn.api.backend.backends;

import me.ifydev.worldspawn.api.WorldSpawnAPI;
import me.ifydev.worldspawn.api.WorldSpawnConstants;
import me.ifydev.worldspawn.api.backend.Backend;
import me.ifydev.worldspawn.api.backend.ConnectionInformation;
import me.ifydev.worldspawn.api.structures.Location;
import me.ifydev.worldspawn.api.util.Result;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 08/06/2018
 */
public class SQLBackend extends Backend {

    private boolean isUsingSQLite = false;
    private String baseConnectionUrl;

    public SQLBackend(ConnectionInformation connectionInformation) {
        super(connectionInformation);

        String type;
        String databaseURL;

        if (connectionInformation.getMeta().containsKey("sqlite")) {
            type = "sqlite";
            Map sqliteData = (Map) connectionInformation.getMeta().get("sqlite");
            databaseURL = (String) sqliteData.get("file");
            isUsingSQLite = true;
        } else {
            type = "mysql";
            databaseURL = "//" + connectionInformation.getUrl() + ":" + connectionInformation.getPort();
        }
        baseConnectionUrl = "jdbc:" + type + ":" + databaseURL;
    }

    /**
     * Get a connection to the mysql server.
     *
     * @return an optional connection, filled if successful.
     */
    private Optional<Connection> getConnection() {
        try {
            if (isUsingSQLite) return Optional.ofNullable(DriverManager.getConnection(baseConnectionUrl));
            String connectionURL = baseConnectionUrl + "/" + connectionInformation.getDatabase();
            return Optional.ofNullable(DriverManager.getConnection(connectionURL, connectionInformation.getUsername(), connectionInformation.getPassword()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void ensureTables() {
        try {
            Connection connection;
            if (isUsingSQLite) connection = DriverManager.getConnection(baseConnectionUrl);
            else connection = DriverManager.getConnection(baseConnectionUrl, connectionInformation.getUsername(), connectionInformation.getPassword());
            if (connection == null) {
                WorldSpawnAPI.get().ifPresent(api -> api.getDisplayManager().sendErrorMessage(WorldSpawnConstants.Permissions.ADMIN, WorldSpawnConstants.DatabaseErrors.COULD_NOT_CONNECT.getFriendly(), false));
                return;
            }

            String database = connectionInformation.getDatabase();

            if (!isUsingSQLite) {
                PreparedStatement statement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS " + database);
                statement.execute();
                statement.close();
                database += ".";
            } else database = "";

            PreparedStatement locations = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + database + "locations (world TEXT, x INTEGER, y INTEGER, z INTEGER)");
            locations.execute();
            locations.close();

            connection.close();
        } catch (SQLException e) {
            WorldSpawnAPI.get().ifPresent(api -> api.getDisplayManager().sendErrorMessage(WorldSpawnConstants.Permissions.ADMIN, WorldSpawnConstants.DatabaseErrors.SQL_ERROR.getFriendly(), true));
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        this.drop();
        ensureTables();

        this.locations = getWorldSpawns();
    }

    @Override
    public void drop() {
        this.locations = new HashMap<>();
    }

    @Override
    public Result<String, WorldSpawnConstants.DatabaseErrors> addWorldSpawn(Location location) {
        // Update the cache.
        this.locations.put(location.getWorld(), location);

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) return Result.fromErr(WorldSpawnConstants.DatabaseErrors.COULD_NOT_CONNECT);

        try {
            PreparedStatement statement = connection.get().prepareStatement("INSERT INTO locations (world, x, y, z) VALUES (?, ?, ?, ?)");
            statement.setString(1, location.getWorld());
            statement.setInt(2, location.getX());
            statement.setInt(3, location.getY());
            statement.setInt(4, location.getZ());

            statement.execute();
            statement.close();
            connection.get().close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fromErr(WorldSpawnConstants.DatabaseErrors.SQL_ERROR);
        }

        return Result.fromSuccess("");
    }

    @Override
    public Result<String, WorldSpawnConstants.DatabaseErrors> removeWorldSpawn(String world) {
        this.locations.remove(world);

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) return Result.fromErr(WorldSpawnConstants.DatabaseErrors.COULD_NOT_CONNECT);

        try {
            PreparedStatement statement = connection.get().prepareStatement("DELETE FROM locations WHERE world=?");
            statement.setString(1, world);

            statement.execute();
            statement.close();
            connection.get().close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fromErr(WorldSpawnConstants.DatabaseErrors.SQL_ERROR);
        }

        return null;
    }

    @Override
    public Optional<Location> getWorldSpawn(String world) {
        return Optional.ofNullable(this.locations.getOrDefault(world, null));
    }

    @Override
    public Map<String, Location> getWorldSpawns() {
        Map<String, Location> spawns = new HashMap<>();

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            WorldSpawnAPI.get().ifPresent(api -> api.getDisplayManager().sendErrorMessage(WorldSpawnConstants.Permissions.ADMIN, WorldSpawnConstants.DatabaseErrors.COULD_NOT_CONNECT.getFriendly(), false));
            return spawns;
        }

        try {
            PreparedStatement statement = connection.get().prepareStatement("SELECT * FROM locations");
            ResultSet results = statement.executeQuery();

            while (results.next()) spawns.put(results.getString("world"), new Location(results.getString("world"),
                    results.getInt("x"), results.getInt("y"), results.getInt("z")));

            results.close();
            statement.close();
            connection.get().close();
        } catch (SQLException e) {
            WorldSpawnAPI.get().ifPresent(api -> api.getDisplayManager().sendErrorMessage(WorldSpawnConstants.Permissions.ADMIN, WorldSpawnConstants.DatabaseErrors.COULD_NOT_CONNECT.getFriendly(), false));
            e.printStackTrace();
        }

        return spawns;
    }
}
