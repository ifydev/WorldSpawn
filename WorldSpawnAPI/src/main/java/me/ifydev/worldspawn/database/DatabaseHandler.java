package me.ifydev.worldspawn.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ifydev.worldspawn.structures.Location;
import me.ifydev.worldspawn.util.Tristate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 08/06/2018
 */
@RequiredArgsConstructor
public abstract class DatabaseHandler {

    @Getter protected Map<String, Location> locations = new HashMap<>();

    public abstract void reload();
    public abstract void drop();

    public abstract Tristate addWorldSpawn(String world, Location location);
    public abstract Tristate removeWorldSpawn(String world);
    public abstract Optional<Location> getWorldSpawn();
}
