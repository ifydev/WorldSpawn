package me.ifydev.worldspawn.spigot

import me.ifydev.worldspawn.api.WorldSpawnAPI
import me.ifydev.worldspawn.api.library.LibraryHandler
import me.ifydev.worldspawn.spigot.command.WorldSpawnCommand
import me.ifydev.worldspawn.spigot.events.PlayerSwitchWorldEvent
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

    lateinit var api: WorldSpawnAPI

    override fun onEnable() {
        LibraryHandler().loadLibraries(logger)

        var initTime = System.currentTimeMillis()

        api = WorldSpawnAPI()
        api.initialize(verifyConnectionInformation(), { permission, messages ->
            Bukkit.getOnlinePlayers().filter { player -> player.hasPermission(permission) }.forEach {
                player -> messages.map {msg -> msg.convertColors() }.forEach { msg -> player.sendMessage(msg) } } })

        initTime = System.currentTimeMillis() - initTime
        logger.info("Initialized WorldSpawn API in ${initTime / 1000} ($initTime ms) seconds!")

        registerListeners()
        registerCommands()
    }

    override fun onDisable() {

    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(PlayerSwitchWorldEvent(), this)
    }

    private fun registerCommands() {
        getCommand("worldspawn").executor = WorldSpawnCommand()
    }

    companion object {
        fun getInstance(): WorldSpawnMain = getPlugin(WorldSpawnMain::class.java)
    }
}