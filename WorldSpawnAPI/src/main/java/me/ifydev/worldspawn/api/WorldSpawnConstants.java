package me.ifydev.worldspawn.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Innectic
 * @since 08/06/2018
 */
public class WorldSpawnConstants {

    public static int API_VERSION = 1;

    @AllArgsConstructor
    @Getter
    public enum APIErrors {
        TYPE_DOES_NOT_EXIST("Backend type does not exist."),
        FAILED_TO_INITIALIZE("Failed to initialize backend <BACKEND>.");

        private String friendly;

        public APIErrors fill(String template, String value) {
            friendly = getFriendly().replace(template, value);
            return this;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum DatabaseErrors {
        COULD_NOT_CONNECT("Could not connect to database."),
        SQL_ERROR("Internal SQL error.");

        private String friendly;
    }

    @AllArgsConstructor
    @Getter
    public enum Permissions {
        ADMIN("worldspawn.admin"),
        SET("worldspawn.set"),
        REMOVE("worldspawn.remove"),
        GET("worldspawn.get");

        private String permission;
    }

    private static String NOT_ENOUGH_ARGS_BASE = "&c&lNot enough arguments! ";

    @Getter
    public enum Responses {
        ALL("&e================== &a&1WorldSpawn Responses &e==================",
                "&a&l/worldspawn set [world] [x] [y] [z]",
                "&a&l/worldspawn remove <world>",
                "&a&l/worldspawn get [world...]",
                "&e====================================================="),
        ARGS_SET(NOT_ENOUGH_ARGS_BASE + "&e&l/worldspawn set [world] [x] [y] [z]"),
        ARGS_REMOVE(NOT_ENOUGH_ARGS_BASE + "&e&l/worldspawn remove <world>"),

        INVALID_WORLD("&c&lInvalid world: &c&l<WORLD>!"),
        LOCATION_MUST_BE_NUMBERS("&c&lLocation must be integers."),
        YOU_ARE_NOT_A_PLAYER("&c&lYou must be a player to run this command!"),
        INSUFFICIENT_PERMISSIONS("&c&lInsufficient permissions!"),
        INTERNAL_ERROR("&c&lInternal error. Please check the console for more details."),

        PLUGIN_NOT_INITIALIZED("&c&lPlugin is not initialized!"),
        SPAWN_SET("&e&lSpawn for <WORLD> has been set.");

        private String[] messages;

        Responses(String... messages) {
            this.messages = messages;
        }
    }
}
