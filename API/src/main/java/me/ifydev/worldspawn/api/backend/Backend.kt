package me.ifydev.worldspawn.api.backend

import me.ifydev.worldspawn.api.backend.backends.SQLBackend
import me.ifydev.worldspawn.api.structures.Location
import me.ifydev.worldspawn.api.util.Tristate
import kotlin.reflect.KClass

/**
 * @author Innectic
 * @since 08/07/2018
 */
abstract class Backend(val info: ConnectionInformation) {

    enum class Types(val backend: KClass<SQLBackend>) {
        MySQL(SQLBackend::class),
        SQLite(SQLBackend::class)
    }

    protected var locations: MutableMap<String, Location> = HashMap()

    fun reload() {
        this.drop()
        this.ensureTables()

        locations = getWorldSpawns(true)
    }
    fun drop() { locations = HashMap() }

    abstract fun ensureTables()
    abstract fun addWorldSpawn(location: Location): Tristate
    abstract fun removeWorldSpawn(world: String): Tristate
    abstract fun getWorldSpawn(world: String): Location?
    abstract fun getWorldSpawns(skipCache: Boolean): MutableMap<String, Location>
}