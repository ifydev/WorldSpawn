package me.ifydev.worldspawn.database.backends;

import me.ifydev.worldspawn.database.DatabaseHandler;
import me.ifydev.worldspawn.structures.Location;
import me.ifydev.worldspawn.util.Tristate;

import java.util.Optional;

/**
 * @author Innectic
 * @since 08/06/2018
 */
public class SQLBackend extends DatabaseHandler {

    @Override
    public void reload() {

    }

    @Override
    public void drop() {

    }

    @Override
    public Tristate addWorldSpawn(String world, Location location) {
        return null;
    }

    @Override
    public Tristate removeWorldSpawn(String world) {
        return null;
    }

    @Override
    public Optional<Location> getWorldSpawn() {
        return Optional.empty();
    }
}
