package me.ifydev.worldspawn.spigot.util

import me.ifydev.worldspawn.api.backend.Backend
import me.ifydev.worldspawn.api.backend.ConnectionInformation
import me.ifydev.worldspawn.api.backend.backends.SQLBackend
import me.ifydev.worldspawn.spigot.WorldSpawnMain
import java.util.*

/**
 * @author Innectic
 * @since 08/07/2018
 */
fun verifyConnectionInformation(): ConnectionInformation? {
    val plugin = WorldSpawnMain.getInstance()

    val type = Backend.Types.findType(plugin.config.getString("storage", "sqlite")) ?: return null
    var info: ConnectionInformation? = null

    if (type.backend == SQLBackend::class) {
        if (type.name.equals("mysql", true)) {
            if (plugin.config.getString("connection.host") == null) return null
            if (plugin.config.getString("connection.database") == null) return null
            if (plugin.config.getString("connection.port") == null) return null
            if (plugin.config.getString("connection.username") == null) return null
            if (plugin.config.getString("connection.password") == null) return null

            info = ConnectionInformation(
                    plugin.config.getString("connection.host"),
                    plugin.config.getString("connection.database"),
                    plugin.config.getInt("connection.port"),
                    plugin.config.getString("connection.username"),
                    plugin.config.getString("connection.password"),
                    HashMap(),
                    type)
        } else if (type.name.equals("sqlite", true)) {
            if (plugin.config.getString("connection.file") == null) return null

            val sqliteMeta: MutableMap<String, Any> = HashMap()
            sqliteMeta["file"] = plugin.dataFolder.toString() + "/" + plugin.config.getString("connection.file")
            val meta: MutableMap<String, Any> = HashMap()
            meta["sqlite"] = sqliteMeta

            info = ConnectionInformation("", "", 0, "", "", meta, type)
        }
    }
    return info
}