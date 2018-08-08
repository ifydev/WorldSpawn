package me.ifydev.worldspawn.api.backend

/**
 * @author Innectic
 * @since 08/07/2018
 *
 * Information used to connect to the data storage backend.
 */
data class ConnectionInformation(
        val host: String, val database: String, val port: Int, val username: String, val password: String,
        val meta: Map<String, Any>, val type: Backend.Types)