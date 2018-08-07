package me.ifydev.worldspawn.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Innectic
 * @since 08/06/2018
 */
public class WorldSpawnConstants {

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
        ADMIN("worldspawn.admin");

        private String permission;
    }
}
