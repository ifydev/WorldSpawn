package me.ifydev.worldspawn.api;

import lombok.Getter;
import me.ifydev.worldspawn.api.backend.Backend;
import me.ifydev.worldspawn.api.backend.ConnectionInformation;
import me.ifydev.worldspawn.api.manager.AbstractDisplayManager;
import me.ifydev.worldspawn.api.util.Result;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author Innectic
 * @since 08/06/2018
 */
public class WorldSpawnAPI {

    private static WorldSpawnAPI api = null;

    @Getter private Backend backend;
    @Getter private AbstractDisplayManager displayManager;

    public WorldSpawnAPI(Backend.Types type, ConnectionInformation connectionInformation, AbstractDisplayManager displayManager) {
        this.displayManager = displayManager;

        constructBackend(type, connectionInformation).ifFilled((backend) -> {
            this.backend = backend;
            System.out.printf("Using %s as the data backend.\n", type.name());

            backend.reload();
        });

        WorldSpawnAPI.api = this;
    }

    private Result<Backend, WorldSpawnConstants.APIErrors> constructBackend(Backend.Types type, ConnectionInformation connectionInformation) {
        try {
            return Result.fromSuccess(type.getBackend().getConstructor(ConnectionInformation.class).newInstance(connectionInformation));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            return Result.fromErr(WorldSpawnConstants.APIErrors.FAILED_TO_INITIALIZE.fill("<BACKEND>", type.name()));
        }
    }

    public static Optional<WorldSpawnAPI> get() {
        return Optional.ofNullable(api);
    }
}
