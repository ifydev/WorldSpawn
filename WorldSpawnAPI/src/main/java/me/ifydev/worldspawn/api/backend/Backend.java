package me.ifydev.worldspawn.api.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ifydev.worldspawn.api.WorldSpawnConstants;
import me.ifydev.worldspawn.api.backend.backends.SQLBackend;
import me.ifydev.worldspawn.api.structures.Location;
import me.ifydev.worldspawn.api.util.Result;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 08/06/2018
 */
@RequiredArgsConstructor
public abstract class Backend {

    @AllArgsConstructor
    @Getter
    public enum Types {
        MySQL(SQLBackend.class),
        SQLite(SQLBackend.class);

        private Class<? extends Backend> backend;

        public static Optional<Types> findType(String type) {
            return Arrays.stream(values()).filter(handler -> handler.name().equalsIgnoreCase(type)).findFirst();
        }
    }

    protected final ConnectionInformation connectionInformation;
    @Getter protected Map<String, Location> locations = new HashMap<>();

    /**
     * Reload all cached values.
     */
    public abstract void reload();

    /**
     * Drop all cached values.
     *
     * This is generally called when doing a cache purge, but may be used in many other situations.
     */
    public abstract void drop();

    public abstract Result<String, WorldSpawnConstants.DatabaseErrors> addWorldSpawn(Location location);
    public abstract Result<String, WorldSpawnConstants.DatabaseErrors> removeWorldSpawn(String world);
    public abstract Optional<Location> getWorldSpawn(String world);
    public abstract Map<String, Location> getWorldSpawns();
}
