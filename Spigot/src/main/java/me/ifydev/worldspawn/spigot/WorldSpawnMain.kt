package me.ifydev.worldspawn.spigot

import me.ifydev.worldspawn.api.WorldSpawnAPI
import me.ifydev.worldspawn.api.library.LibraryHandler
import me.ifydev.worldspawn.spigot.util.verifyConnectionInformation
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Innectic
 * @since 08/07/2018
 */
fun String.convertColors(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

class WorldSpawnMain : JavaPlugin() {

    private lateinit var api: WorldSpawnAPI

    override fun onEnable() {
        LibraryHandler().loadLibraries(logger)

        var initTime = System.currentTimeMillis()

        api = WorldSpawnAPI()
        api.initialize(verifyConnectionInformation(), { permission, messages ->
            Bukkit.getOnlinePlayers().filter { player -> player.hasPermission(permission) }.forEach {
                player -> messages.map {msg -> msg.convertColors() }.forEach { msg -> player.sendMessage(msg) } } })

        initTime = System.currentTimeMillis() - initTime
        logger.info("Initialized WorldSpawn API in ${initTime / 1000} ($initTime ms) seconds!")
    }

    override fun onDisable() {

    }

    companion object {
        fun getInstance(): WorldSpawnMain = getPlugin(WorldSpawnMain::class.java)
    }
}