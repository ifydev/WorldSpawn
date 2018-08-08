package me.ifydev.worldspawn.api

import me.ifydev.worldspawn.api.backend.Backend
import me.ifydev.worldspawn.api.backend.ConnectionInformation
import java.util.*
import kotlin.reflect.full.primaryConstructor

/**
 * @author Innectic
 * @since 08/07/2018
 */
typealias ErrorHandler = (String, Array<String>) -> Unit

class WorldSpawnAPI {
    companion object {
        private lateinit var api: WorldSpawnAPI

        fun get(): Optional<WorldSpawnAPI> {
            return Optional.ofNullable(api)
        }
    }

    private var info: ConnectionInformation? = null
    private var backend: Backend? = null
    private lateinit var handler: ErrorHandler

    fun initialize(info: ConnectionInformation?, handler: ErrorHandler) {
        this.info = info
        this.handler = handler

        try {
            backend = info?.type?.backend?.primaryConstructor?.call(info)
            backend?.reload()
        } catch (e: Exception) {
            println("Unable to initialize WorldSpawn API!")
            e.printStackTrace()
            return
        }

        api = this
    }
}
