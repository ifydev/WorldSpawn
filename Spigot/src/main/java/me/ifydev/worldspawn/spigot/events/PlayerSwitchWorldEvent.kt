package me.ifydev.worldspawn.spigot.events

import me.ifydev.worldspawn.api.structures.Location
import me.ifydev.worldspawn.spigot.WorldSpawnMain
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent

/**
 * @author Innectic
 * @since 08/08/2018
 */
class PlayerSwitchWorldEvent : Listener {

    @EventHandler
    fun onPlayerSwitchWorldEvent(event: PlayerChangedWorldEvent) {
        if (event.player == null) return
        if (event.from == null) return

        val plugin = WorldSpawnMain.getInstance()
        val location: Location = plugin.api.backend?.getWorldSpawn(event.from.name) ?: return

        event.player.teleport(org.bukkit.Location(Bukkit.getWorld(location.world), location.x.toDouble(), location.y.toDouble(), location.z.toDouble()))
    }
}