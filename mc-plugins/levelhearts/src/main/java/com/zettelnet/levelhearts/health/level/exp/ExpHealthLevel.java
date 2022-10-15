package com.zettelnet.levelhearts.health.level.exp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.health.level.HealthLevel;

/**
 * Represents a {@link HealthLevel} getter that returns health levels based on
 * the experience level of the player (using {@link Player#getLevel()}).
 *
 * @author Zettelkasten
 * @see Player#getLevel()
 */
public class ExpHealthLevel implements HealthLevel, Listener {

    private final HealthManager healthManager;

    public ExpHealthLevel(Plugin plugin, HealthManager healthManager) {
        this.healthManager = healthManager;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        healthManager.getHealthTrigger().onLevelChange(event.getPlayer(), event.getOldLevel(), event.getNewLevel());
    }

    @Override
    public int get(Player player) {
        return player.getLevel();
    }

    @Override
    public String getIdentifier() {
        return "exp";
    }
}
