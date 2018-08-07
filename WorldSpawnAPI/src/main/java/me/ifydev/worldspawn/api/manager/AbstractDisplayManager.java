package me.ifydev.worldspawn.api.manager;

import me.ifydev.worldspawn.api.WorldSpawnConstants;

/**
 * @author Innectic
 * @since 08/06/2018
 */
public interface AbstractDisplayManager {

    /**
     * Send an error message to those with `permission`.
     *
     * @param permission the permission required to see the error
     * @param error      the error to be displayed
     * @param report     does this error need to be reported (bug report)?
     */
    void sendErrorMessage(WorldSpawnConstants.Permissions permission, String error, boolean report);
}
