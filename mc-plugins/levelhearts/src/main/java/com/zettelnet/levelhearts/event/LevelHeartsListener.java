package com.zettelnet.levelhearts.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.zettelnet.levelhearts.LevelHearts;
import com.zettelnet.levelhearts.LevelHeartsPlugin;
import com.zettelnet.levelhearts.configuration.LanguageConfiguration;
import com.zettelnet.levelhearts.health.HealthCallable;
import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.zet.event.EventRunnable;

public class LevelHeartsListener implements Listener {

    private final LevelHeartsPlugin plugin;
    private final LanguageConfiguration lang;

    private final HealthManager healthManager;

    public LevelHeartsListener() {
        this.plugin = LevelHearts.getPlugin();
        this.lang = LevelHearts.getLanguageConfiguration();
        this.healthManager = LevelHearts.getHealthManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        // load player
        if (!LevelHearts.getHealthStorage().loadPlayer(player)) {
            // player has not played before -> maxhealth needs to be reset!
            healthManager.setMaxHealthAsynchronously(player, () -> healthManager.getHealthConfiguration().getMaxHealthBase(player));
        }

        // trigger
        healthManager.getHealthTrigger().onPlayerJoin(player);

        // AutoUpdater
        if (player.isOp() && plugin.isUpdateAvailable()) {
            lang.sendUpdateAvailable(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // trigger
        healthManager.getHealthTrigger().onPlayerQuit(player);

        // unload
        LevelHearts.getHealthStorage().unloadPlayer(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // trigger
        healthManager.getHealthTrigger().onPlayerRespawn(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerHealthChangeMonitor(PlayerHealthChangeEvent event) {
        if (event.isHealthChanged()) {
            for (EventRunnable<PlayerHealthChangeEvent> runnable : event.getMonitors()) {
                runnable.run(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMaxHealthChangeMonitor(PlayerMaxHealthChangeEvent event) {
        if (event.isMaxHealthChanged()) {
            for (EventRunnable<PlayerMaxHealthChangeEvent> runnable : event.getMonitors()) {
                runnable.run(event);
            }
        }
    }
}
