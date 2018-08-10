package me.ifydev.worldspawn.api.backend.backends

import me.ifydev.worldspawn.api.backend.Backend
import me.ifydev.worldspawn.api.backend.ConnectionInformation
import me.ifydev.worldspawn.api.structures.Location
import me.ifydev.worldspawn.api.util.Tristate
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

/**
 * @author Innectic
 * @since 08/07/2018
 */

@Throws(SQLException::class)
fun Connection.prepareStatementAndExecute(sql: String, fillers: Array<Any> = emptyArray()): ResultSet? {
    val statement = this.prepareStatement(sql)
    fillers.forEachIndexed { index, value -> statement.setObject(index + 1, value) }

    val results = statement.executeQuery()

    statement.close()
    return results
}

class SQLBackend(info: ConnectionInformation) : Backend(info) {

    private lateinit var baseConnectionUrl: String
    private var isUsingSQLite = false

    override fun ensureTables() {
        val type: String
        val databaseURL: String

        if (info.meta.containsKey("sqlite")) {
            type = "sqlite"
            val sqliteData = info.meta["sqlite"] as Map<*, *>
            databaseURL = sqliteData["file"] as String
            isUsingSQLite = true
        } else {
            type = "mysql"
            databaseURL = "//$info.host:$info.port"
        }
        baseConnectionUrl = "jdbc:$type:$databaseURL"

        try {
            val connection: Connection? = if (isUsingSQLite) DriverManager.getConnection(baseConnectionUrl)
            else DriverManager.getConnection(baseConnectionUrl, info.username, info.password) ?: return

            connection.use {
                var database = info.database
                if (!isUsingSQLite) {
                    val statement = connection?.prepareStatement("CREATE DATABASE IF NOT EXISTS $database")
                    statement?.execute()
                    statement?.close()
                    database += "."
                } else database = ""

                connection?.prepareStatementAndExecute("CREATE TABLE IF NOT EXISTS ${database}locations (world TEXT NOT NULL UNIQUE, x INTEGER NOT NULL, y INTEGER NOT NULL, z INTEGER NOT NULL)")
            }

        } catch (e: SQLException) {
            e.printStackTrace()
            // TODO: Report errors here
        }
    }

    private fun getConnection(): Connection? {
        try {
            if (isUsingSQLite) return DriverManager.getConnection(baseConnectionUrl)
            val connectionURL = baseConnectionUrl + "/" + info.database
            return DriverManager.getConnection(connectionURL, info.username, info.password)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    override fun addWorldSpawn(location: Location): Tristate {
        val connection = getConnection() ?: return Tristate.NONE

        locations[location.world] = location

        try {
            connection.use { connection.prepareStatementAndExecute("INSERT INTO locations (world, x, y, z) VALUES (?, ?, ?, ?)", arrayOf(location.world, location.x, location.y, location.z)) }
        } catch (e: SQLException) {
            e.printStackTrace()
            // TODO: Error reporting
            return Tristate.FALSE
        }
        return Tristate.TRUE
    }

    override fun removeWorldSpawn(world: String): Tristate {
        locations.remove(world)

        val connection = getConnection() ?: return Tristate.NONE
        try {
            connection.use { connection.prepareStatementAndExecute("DELETE FROM locations WHERE world=?", arrayOf(world)) }
        } catch(e: SQLException) {
            // TODO: Error reporting
            e.printStackTrace()
            return Tristate.FALSE
        }
        return Tristate.TRUE
    }

    override fun getWorldSpawn(world: String): Location? {
        return locations.getOrDefault(world, null)
    }

    override fun getWorldSpawns(skipCache: Boolean): MutableMap<String, Location> {
        if (!skipCache) return locations

        val connection = getConnection() ?: return locations
        try {
            connection.use {
                val results = connection.prepareStatementAndExecute("SELECT * FROM locations") ?: return locations

                while (results.next()) locations[results.getString("world")] = Location(results.getInt("x"), results.getInt("y"), results.getInt("z"), results.getString("world"))
            }
        } catch (e: SQLException) {
            // TODO: error reporting
            e.printStackTrace()
        }
        return locations
    }
}