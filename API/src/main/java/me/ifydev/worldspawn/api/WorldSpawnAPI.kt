package me.ifydev.worldspawn.api

import java.util.*

/**
 * @author Innectic
 * @since 08/07/2018
 */
class WorldSpawnAPI {
    companion object {
        private val api = null

        fun get(): Optional<WorldSpawnAPI> {
            return Optional.ofNullable(api)
        }
    }

    fun initialize() {

    }
}
