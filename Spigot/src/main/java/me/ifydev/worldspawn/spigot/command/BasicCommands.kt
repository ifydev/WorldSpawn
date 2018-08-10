package me.ifydev.worldspawn.spigot.command

import me.ifydev.worldspawn.api.*
import me.ifydev.worldspawn.api.structures.Location
import me.ifydev.worldspawn.api.util.Tristate
import me.ifydev.worldspawn.spigot.convertColors
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Innectic
 * @since 08/09/2018
 */
class BasicCommands {
    companion object {

        fun getSpawns(sender: CommandSender, args: Array<String>): Boolean {
            val backend = WorldSpawnAPI().backend ?: return false

            val worlds = if (args.isNotEmpty()) {
                // CLEANUP
                // HACK
                val location = backend.getWorldSpawn(args[0]) ?: Location(0, 0, 0, "")
                if (location.world == "" && location.x == 0 && location.y == 0 && location.z == 0) {
                    sender.sendMessage(INVALID_WORLD.convertColors())
                    return true
                }
                arrayOf(location)
            } else backend.getWorldSpawns(false).values.toTypedArray()

            worlds.forEach { location ->
                sender.sendMessage(SPAWN_TEMPLATE.replace("<WORLD>", location.world)
                        .replace("<X>", location.x.toString()).replace("<Y>", location.y.toString())
                        .replace("Z", location.z.toString()))
            }
            return true
        }

        fun setSpawn(sender: CommandSender, args: Array<String>): String? {
            val player = sender as? Player ?: return NOT_A_PLAYER
            val backend = WorldSpawnAPI().backend ?: return API_NOT_INITIALIZED

            val location = player.location
            val result = backend.addWorldSpawn(Location(location.blockX, location.blockY, location.blockZ, location.world.name))
            if (result == Tristate.NONE) return COULD_NOT_CONNECT_TO_DATABASE
            else if (result == Tristate.FALSE) return ""
            return WORLD_SPAWN_SET.replace("<WORLD>", location.world.name)
        }

        fun removeSpawn(sender: CommandSender, args: Array<String>): String? {
            val player = sender as? Player ?: return NOT_A_PLAYER
            val backend = WorldSpawnAPI().backend ?: return API_NOT_INITIALIZED

            val location = player.location
            val result = backend.removeWorldSpawn(location.world.name)
            if (result == Tristate.NONE) return COULD_NOT_CONNECT_TO_DATABASE
            else if (result == Tristate.FALSE) return ""
            return WORLD_SPAWN_SET.replace("<WORLD>", location.world.name)
        }
    }
}
