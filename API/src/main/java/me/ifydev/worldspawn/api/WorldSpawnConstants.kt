package me.ifydev.worldspawn.api

/**
 * @author Innectic
 * @since 08/09/2018
 */
const val WORLDSPAWN_PREFIX = "&a&lWorldSpawn> "
const val PROFILE_VERSION = 1

const val PERMISSION_GET_SPAWNS = "worldspawn.spawn.get"
const val PREMISSION_SET_SPAWN = "worldspawn.spawn.set"
const val PERMISSION_REMOVE_SPAWN = "worldspawn.spawn.remove"

const val HELP_HEADER = "&e================== &a&lWorldSpawn Help &e=================="
const val HELP_FOOTER = "&e====================================================="
val HELP_RESPONSE: Array<String> = arrayOf(
        "/worldspawn set",
        "/worldspawn get [world]",
        "/worldspawn remove"
)
val WORLDSPAWN_ERROR: Array<String> = arrayOf(
        "&c&lError encountered: <ERROR_TYPE>",
        "&c&lShould this be reported?: <SHOULD_REPORT>"
)

const val API_NOT_INITIALIZED = "$WORLDSPAWN_PREFIX&c&lPlugin is not initialized!"
const val INVALID_WORLD = "$WORLDSPAWN_PREFIX&c&lInvalid world!"
const val NOT_A_PLAYER = "$WORLDSPAWN_PREFIX&c&lYou're not a player!"
const val COULD_NOT_CONNECT_TO_DATABASE = "$WORLDSPAWN_PREFIX&c&lCould not connect to the data handler."
const val YOU_DONT_HAVE_PERMISSION = "$WORLDSPAWN_PREFIX&c&lYou don't have permission for this command!"

const val WORLD_SPAWN_SET = "${WORLDSPAWN_PREFIX}Spawn for <WORLD> has been set!"
const val WORLD_SPAWN_REMOVED = "${WORLDSPAWN_PREFIX}Spawn removed for <WORLD>!"
const val SPAWN_TEMPLATE = "$WORLDSPAWN_PREFIX&e&l<WORLD> - X: <X>, Y: <Y>, Z: <Z>"
