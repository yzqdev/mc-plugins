package org.lores

import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * @author yanni
 */
class Lores : JavaPlugin() {
    override fun onEnable() {
        registerCommands()
        val version = Properties()
        try {
            version.load(getResource("version.properties"))
        } catch (e: Exception) {
            logger.info("error find version")
        }
        logger.info("Lores " + description.version + " (Build " + version.getProperty("Build") + ") is enabled!")
    }

    private fun registerCommands() {
        getCommand("lore")?.setExecutor(LoreCommand(this))
        getCommand("lore")?.tabCompleter = LoreCommand(this)
    }
}