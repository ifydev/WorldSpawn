package me.ifydev.worldspawn.api

import me.ifydev.worldspawn.api.backend.Backend
import me.ifydev.worldspawn.api.backend.ConnectionInformation
import java.util.*
import kotlin.reflect.full.primaryConstructor

/**
 * @author Innectic
 * @since 08/07/2018
 */
class WorldSpawnAPI {
    companion object {
        private lateinit var api: WorldSpawnAPI

        fun get(): Optional<WorldSpawnAPI> {
            return Optional.ofNullable(api)
        }
    }

    private lateinit var info: ConnectionInformation
    private var backend: Backend? = null

    fun initialize(info: ConnectionInformation) {
        this.info = info

        try {
            backend = info.type.backend.primaryConstructor?.call(info)
            backend?.reload()
        } catch (e: Exception) {
            println("Unable to initialize WorldSpawn API!")
            e.printStackTrace()
            return
        }

        api = this
    }
}
