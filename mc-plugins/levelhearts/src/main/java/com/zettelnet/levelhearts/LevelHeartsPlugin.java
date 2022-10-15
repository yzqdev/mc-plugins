package com.zettelnet.levelhearts;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MONTH;

import java.util.Calendar;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zettelnet.levelhearts.configuration.LanguageConfiguration;
import com.zettelnet.levelhearts.configuration.MainConfiguration;
import com.zettelnet.levelhearts.event.LevelHeartsListener;
import com.zettelnet.levelhearts.event.LevelHeartsParticles;
import com.zettelnet.levelhearts.health.HealthConfiguration;
import com.zettelnet.levelhearts.health.HealthFormat;
import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.health.level.HealthLevelLoader;
import com.zettelnet.levelhearts.health.level.HealthLevelManager;
import com.zettelnet.levelhearts.health.level.constant.ConstantHealthLevelLoader;
import com.zettelnet.levelhearts.health.level.exp.ExpHealthLevelLoader;
import com.zettelnet.levelhearts.lib.net.gravitydevelopment.updater.PluginUpdater;
import com.zettelnet.levelhearts.lib.net.gravitydevelopment.updater.Updater;
import com.zettelnet.levelhearts.lib.net.gravitydevelopment.updater.Updater.UpdateResult;
import com.zettelnet.levelhearts.lib.net.gravitydevelopment.updater.Updater.UpdateType;
import com.zettelnet.levelhearts.storage.HealthStorage;
import com.zettelnet.levelhearts.storage.HealthStorageFactory;
import com.zettelnet.levelhearts.storage.HealthStorageManager;
import com.zettelnet.levelhearts.storage.MySQLHealthStorageFactory;
import com.zettelnet.levelhearts.storage.PhysicalHealthStorageFactory;
import com.zettelnet.levelhearts.storage.SQLiteHealthStorageFactory;
import com.zettelnet.levelhearts.zet.configuration.PluginConfigurationFile;

/**
 * This program is licensed under Attribution-NonCommercial-NoDerivatives 4.0
 * International. You are free to share this program under the term of
 * attribution. You may not use this program for commercial purposes and may not
 * create derivatives. To view the full license, check the included license.txt
 * file.
 *
 * @author Zettelkasten
 */
public class LevelHeartsPlugin extends JavaPlugin {

    protected static LevelHeartsPlugin plugin;

    private Logger log;

    private PluginManager pluginManager;
    private LevelHeartsListener listener;
    private LevelHeartsCommands commands;
    private LevelHeartsTabCompleter tabCompleter;
    private DisabledCommands commandsDisabled;
    private LevelHeartsPermissions permissionsManager;
    private HealthManager healthManager;
    private HealthLevelManager healthLevelManager;
    private HealthStorage healthStorage;
    private HealthStorageManager healthStorageManager;
    private HealthFormat healthFormat;
    private LevelHeartsParticles particles;

    private MainConfiguration config;
    private LanguageConfiguration languageConfig;
    private HealthConfiguration healthConfig;

    private Updater updater;

    private boolean christmas;

    public LevelHeartsPlugin() {
        plugin = this;
    }

    public static LevelHeartsPlugin instance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        log = getLogger();
        pluginManager = getServer().getPluginManager();

        // Detect christmas
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(MONTH) == DECEMBER && calendar.get(DAY_OF_MONTH) >= 24) {
            christmas = true;
        } else if (calendar.get(JANUARY) == DECEMBER && calendar.get(DAY_OF_MONTH) <= 6) {
            christmas = true;
        } else {
            christmas = false;
        }

        // Config
        PluginConfigurationFile.setLogger(getLogger());
        config = new MainConfiguration();
        config.load();

        // Permissions
        permissionsManager = new LevelHeartsPermissions();
        permissionsManager.loadValues(config.getConfig());

        // Health Configuration
        healthConfig = new LevelHeartsConfiguration(config, permissionsManager);

        // Health Storage
        HealthStorageFactory physicalStorage = new PhysicalHealthStorageFactory();
        HealthStorageFactory mySQLStorage = new MySQLHealthStorageFactory(this);
        HealthStorageFactory sqliteStorage = new SQLiteHealthStorageFactory(this);

        healthStorageManager = new HealthStorageManager(physicalStorage, log);
        healthStorageManager.addFactory("physical", physicalStorage);
        healthStorageManager.addFactory("mysql", mySQLStorage);
        healthStorageManager.addFactory("sqlite", sqliteStorage);

        healthStorage = healthStorageManager.makeStorage(config.storageMode());
        healthStorage.initialize();

        // Load players
        for (Player player : Bukkit.getOnlinePlayers()) {
            healthStorage.loadPlayer(player);
        }

        // Language
        languageConfig = new LanguageConfiguration();
        languageConfig.load();

        // Health Manager
        healthManager = new LevelHeartsHealthManager(this, healthStorage, healthConfig, permissionsManager);
        healthManager.setHealthTrigger(new LevelHeartsHealthTrigger(this, healthManager));

        HealthLevelLoader expHealthLevel = new ExpHealthLevelLoader(this, healthManager);
        HealthLevelLoader disabledHealthLevel = new ConstantHealthLevelLoader(0);

        healthLevelManager = new HealthLevelManager(expHealthLevel, getLogger());
        healthLevelManager.addLoader("exp", expHealthLevel);
        healthLevelManager.addLoader("disabled", disabledHealthLevel);
        healthManager.setHealthLevel(healthLevelManager.getHealthLevel(config.maxHealthLevelMode()));

        // Health Format
        healthFormat = new LevelHeartsHealthFormat(healthManager, languageConfig);

        // Events
        listener = new LevelHeartsListener();
        pluginManager.registerEvents(listener, this);
        getLogger().fine("Registered events from listener.");

        // Particles
        particles = new LevelHeartsParticles();
        pluginManager.registerEvents(particles, this);

        // Commands
        commands = new LevelHeartsCommands();
        tabCompleter = new LevelHeartsTabCompleter();
        commandsDisabled = new DisabledCommands();
        if (config.commandHealthEnabled()) {
            getCommand("health").setExecutor(commands);
            getCommand("health").setTabCompleter(tabCompleter);
        } else if (getCommand("health").getExecutor() == null) {
            getCommand("health").setExecutor(commandsDisabled);
        }
        if (config.commandMaxHealthEnabled()) {
            getCommand("maxhealth").setExecutor(commands);
            getCommand("maxhealth").setTabCompleter(tabCompleter);
        } else if (getCommand("maxhealth").getExecutor() == null) {
            getCommand("maxhealth").setExecutor(commandsDisabled);
        }
        if (config.commandInfoEnabled()) {
            getCommand("levelhearts").setExecutor(commands);
            getCommand("levelhearts").setTabCompleter(tabCompleter);
        } else if (getCommand("levelhearts").getExecutor() == null) {
            getCommand("levelhearts").setExecutor(commandsDisabled);
        }
        log.fine("Registered commands.");

        // Metrics


        // Update Check
        if (config.updateCheckEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                updater = new PluginUpdater(LevelHeartsPlugin.this, 74928, getFile(), UpdateType.NO_DOWNLOAD,
                        false);
                if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
                    log.info("New version available! (" + updater.getLatestName() + " for "
                            + updater.getLatestGameVersion() + ")");
                    log.info("Download it at " + updater.getLatestFileLink()
                            + " or turn off the update checking in config.yml (autoUpdater.enabled = false).");
                }
            });
        }

        log.info("Enabled successfully.");

        log.fine("Config version: " + plugin.getConfiguration().configVersion());
        log.fine("Plugin version: " + plugin.getVersion());
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            healthStorage.unloadPlayer(player);
        }
        healthStorage.terminate();

        HandlerList.unregisterAll(this);
        getLogger().info("Disabled successfully.");
    }

    public void reload() {
        onDisable();
        onEnable();
    }

    public String getVersion() {
        return getDescription().getVersion();
    }

    public MainConfiguration getConfiguration() {
        return config;
    }

    public LanguageConfiguration getLanguageConfiguration() {
        return languageConfig;
    }

    public LevelHeartsPermissions getPermissionManager() {
        return permissionsManager;
    }

    public HealthManager getHealthManager() {
        return healthManager;
    }

    public HealthStorage getHealthStorage() {
        return healthStorage;
    }

    public HealthStorageManager getHealthStorageManager() {
        return healthStorageManager;
    }

    public HealthLevelManager getHealthLevelManager() {
        return healthLevelManager;
    }

    public HealthFormat getHealthFormat() {
        return healthFormat;
    }

    public String getWebsite() {
        return this.getDescription().getWebsite();
    }



    public Updater getUpdater() {
        return updater;
    }

    public boolean isUpdateAvailable() {
        return updater == null ? false : updater.getResult() == UpdateResult.UPDATE_AVAILABLE;
    }

    public LevelHeartsParticles getParticles() {
        return particles;
    }

    public boolean isChristmas() {
        return christmas;
    }

    public void setChristmas(boolean christmas) {
        this.christmas = christmas;
    }
}
