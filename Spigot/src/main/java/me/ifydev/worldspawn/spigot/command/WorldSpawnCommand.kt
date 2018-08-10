package me.ifydev.worldspawn.spigot.command

import me.ifydev.worldspawn.api.API_NOT_INITIALIZED
import me.ifydev.worldspawn.api.HELP_FOOTER
import me.ifydev.worldspawn.api.HELP_HEADER
import me.ifydev.worldspawn.api.HELP_RESPONSE
import me.ifydev.worldspawn.spigot.convertColors
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * @author Innectic
 * @since 08/08/2018
 */
class WorldSpawnCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, name: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(HELP_HEADER.convertColors())
            HELP_RESPONSE.forEach { response -> sender.sendMessage(response.convertColors()) }
            sender.sendMessage(HELP_FOOTER.convertColors())
            return false
        }

        val first = args[0]
        when {
            first.equals("get", true) -> {
                if (BasicCommands.getSpawns(sender, args.copyOfRange(1, args.size))) return true
                sender.sendMessage(API_NOT_INITIALIZED.convertColors())
                return false
            }
            first.equals("set", true) -> {
                sender.sendMessage(BasicCommands.setSpawn(sender, args.copyOfRange(1, args.size))?.convertColors() ?: return false)
                return true
            }
            first.equals("remove", true) -> {
                sender.sendMessage(BasicCommands.removeSpawn(sender, args.copyOfRange(1, args.size))?.convertColors() ?: return false)
                return true
            }
            else -> return false
        }

    }
}