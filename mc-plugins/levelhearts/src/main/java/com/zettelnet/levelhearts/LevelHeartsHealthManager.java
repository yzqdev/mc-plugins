package com.zettelnet.levelhearts;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import com.zettelnet.levelhearts.event.PlayerHealthChangeEvent;
import com.zettelnet.levelhearts.event.PlayerMaxHealthChangeEvent;
import com.zettelnet.levelhearts.health.HealthCalculator;
import com.zettelnet.levelhearts.health.HealthCallable;
import com.zettelnet.levelhearts.health.HealthConfiguration;
import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.health.HealthPermissions;
import com.zettelnet.levelhearts.health.HealthTrigger;
import com.zettelnet.levelhearts.health.level.HealthLevel;
import com.zettelnet.levelhearts.storage.HealthStorage;

public class LevelHeartsHealthManager implements HealthManager {

    private final Plugin plugin;
    private final PluginManager pluginManager;
    private final BukkitScheduler scheduler;

    private final HealthStorage storage;
    private final HealthCalculator calculator;
    private HealthLevel level;
    private HealthTrigger trigger;

    private final HealthConfiguration config;
    private final HealthPermissions perms;

    public LevelHeartsHealthManager(Plugin plugin, HealthStorage storage,
                                    HealthConfiguration config, HealthPermissions perms) {
        this.plugin = plugin;
        Server server = plugin.getServer();
        this.pluginManager = server.getPluginManager();
        this.scheduler = server.getScheduler();

        this.storage = storage;
        this.calculator = new LevelHeartsHealthCalculator(config);

        this.config = config;
        this.perms = perms;
    }

    public HealthStorage getHealthStorage() {
        return storage;
    }

    @Override
    public HealthCalculator getHealthCalculator() {
        return calculator;
    }

    @Override
    public HealthConfiguration getHealthConfiguration() {
        return config;
    }

    @Override
    public HealthPermissions getHealthPermissions() {
        return perms;
    }

    @Override
    public HealthLevel getHealthLevel() {
        return level;
    }

    @Override
    public void setHealthLevel(HealthLevel level) {
        this.level = level;
    }

    @Override
    public HealthTrigger getHealthTrigger() {
        return trigger;
    }

    @Override
    public void setHealthTrigger(HealthTrigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public double getHealth(Player player) {
        if (player != null) {
            return storage.getHealth(player);
        } else {
            return config.getMaxHealthBase(player);
        }
    }

    @Override
    public double getMaxHealth(Player player) {
        if (player != null) {
            return storage.getMaxHealth(player);
        } else {
            return config.getMaxHealthBase(player);
        }
    }

    private class HealthChangeTask implements Runnable {

        private final PlayerHealthChangeEvent event;

        private HealthChangeTask(PlayerHealthChangeEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            pluginManager.callEvent(event);

            if (!event.isCancelled() && event.isHealthChanged()) {
                // change health
                storage.setHealth(event.getPlayer(), event.getNewHealth());
            }
        }
    }

    @Override
    public PlayerHealthChangeEvent setHealth(final Player player, final double health) {
        if (!perms.canChangeHealth(player)) {
            return null;
        }

        double oldHealth = getHealth(player);

        // construct event
        final PlayerHealthChangeEvent event = new PlayerHealthChangeEvent(player, oldHealth, health);

        // perform change
        scheduler.runTask(plugin, new HealthChangeTask(event));

        // return the event so callers can add notifiers etc.
        return event;
    }

    @Override
    public PlayerHealthChangeEvent setHealthAsynchronously(final Player player, final HealthCallable health) {
        if (!perms.canChangeHealth(player)) {
            return null;
        }

        double oldHealth = getHealth(player);

        // construct event
        final PlayerHealthChangeEvent event = new PlayerHealthChangeEvent(player, oldHealth);

        // run an async task that calculates the new max health and then
        // performs the change
        scheduler.runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if (event.isIncreaseOnly() && event.getHealthChange() < 0) {
                    // increase only
                    return;
                }

                // calculate health
                event.setNewHealth(health.call());

                // perform change
                scheduler.runTask(plugin, new HealthChangeTask(event));
            }
        });

        // return the event so callers can add notifiers etc.
        return event;

    }

    @Override
    public PlayerHealthChangeEvent restoreHealth(Player player) {
        return setHealth(player, getMaxHealth(player));
    }

    private class MaxHealthChangeTask implements Runnable {

        private final PlayerMaxHealthChangeEvent event;

        private MaxHealthChangeTask(PlayerMaxHealthChangeEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            pluginManager.callEvent(event);

            if (!event.isCancelled() && event.isMaxHealthChanged()) {
                if (event.isIncreaseOnly() && event.getMaxHealthChange() < 0) {
                    // increase only
                    return;
                }

                // Change max health
                storage.setMaxHealth(event.getPlayer(), event.getNewMaxHealth());

                // Restore full health if enabled and max health is
                // increased
                if (event.isRestoreFullHealth() && event.getMaxHealthChange() > 0) {
                    restoreHealth(event.getPlayer());
                }
            }
        }
    }

    @Override
    public PlayerMaxHealthChangeEvent setMaxHealth(final Player player, double maxHealth) {
        if (!perms.canChangeMaxHealth(player)) {
            return null;
        }

        double oldMaxHealth = getMaxHealth(player);

        // construct event
        final PlayerMaxHealthChangeEvent event = new PlayerMaxHealthChangeEvent(player, oldMaxHealth, maxHealth, perms.canRestoreHealth(player));

        // perform change
        scheduler.runTask(plugin, new MaxHealthChangeTask(event));

        // return the event so callers can add notifiers etc.
        return event;
    }

    @Override
    public PlayerMaxHealthChangeEvent setMaxHealthAsynchronously(final Player player, final HealthCallable maxHealth) {
        if (!perms.canChangeMaxHealth(player)) {
            return null;
        }

        double oldMaxHealth = getMaxHealth(player);

        // construct event
        final PlayerMaxHealthChangeEvent event = new PlayerMaxHealthChangeEvent(player, oldMaxHealth, perms.canRestoreHealth(player));

        // run an async task that calculates the new max health and then
        // performs the change
        scheduler.runTaskAsynchronously(plugin, () -> {
            // calculate max health
            event.setNewMaxHealth(maxHealth.call());

            // perform change
            scheduler.runTask(plugin, new MaxHealthChangeTask(event));
        });

        return event;

    }

    @Override
    public PlayerMaxHealthChangeEvent recalculateMaxHealth(final Player player) {
        return setMaxHealthAsynchronously(player, () -> calculator.getMaxHealthAtLevel(level.get(player), player));
    }
}
