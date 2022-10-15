package com.zettelnet.levelhearts.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import com.zettelnet.levelhearts.LevelHearts;
import com.zettelnet.levelhearts.zet.configuration.PluginConfigurationFile;

public class MainConfiguration extends PluginConfigurationFile {

    private Logger log;

    private boolean configForceUpdate;
    private String configVersion;

    private boolean metricsEnabled;

    private String storageMode;
    private String storageDatabase;
    private String storageTable;
    private String storageUsername;
    private String storagePassword;

    private boolean updateCheckEnabled;
    private boolean updateCheckInformOperators;

    private String maxHealthLevelMode;
    private double maxHealthDefault;
    private double maxHealthLimit;
    private int maxHealthLevelInterval;
    private int maxHealthStartLevel;
    private boolean maxHealthResetLevelChange;
    private boolean maxHealthResetLogin;
    private boolean maxHealthResetDeath;
    private boolean maxHealthRestoreHealth;
    private boolean maxHealthParticleEffectDecrease;

    private boolean maxHealthParticleEffectIncrease;
    private Map<Set<World>, Boolean> worldGroups;
    private Map<World, Boolean> worlds;

    private String chatLanguage;
    private boolean chatChangeMessagesEnabled;
    private boolean chatCommandMessagesEnabled;

    private boolean commandMaxHealthEnabled;
    private boolean commandHealthEnabled;
    private boolean commandInfoEnabled;

    public MainConfiguration() {
        super(LevelHearts.getPlugin(), "config.yml", "config.yml");
        this.log = LevelHearts.getLogger();
    }

    @Override
    protected void loadValues(FileConfiguration config) {
        update(config);

        configVersion = config.getString("config.version",
                LevelHearts.getVersion());
        configForceUpdate = config.getBoolean("config.forceUpdate", false);

        metricsEnabled = config.getBoolean("metricsEnabled", true);

        storageMode = config.getString("storage.mode", "sqlite");
        storageDatabase = config.getString("storage.database", "localhost:3306/database");
        storageTable = config.getString("storage.table", "Health");
        storageUsername = config.getString("storage.username", "root");
        storagePassword = config.getString("storage.password", "");

        updateCheckEnabled = config.getBoolean("updateCheck.enabled", true);
        updateCheckInformOperators = config.getBoolean(
                "updateCheck.informOperators", true);

        maxHealthLevelMode = config.getString("maxHealth.levelMode", "exp");
        maxHealthDefault = config.getDouble("maxHealth.default", 20);
        maxHealthLimit = config.getDouble("maxHealth.limit", 60);
        maxHealthLevelInterval = config.getInt("maxHealth.levelInterval", 5);
        maxHealthStartLevel = config.getInt("maxHealth.startLevel", 0);
        maxHealthResetLevelChange = config.getBoolean(
                "maxHealth.reset.levelChange", false);
        maxHealthResetLogin = config.getBoolean("maxHealth.reset.login", false);
        maxHealthResetDeath = config.getBoolean("maxHealth.reset.death", false);
        maxHealthRestoreHealth = config.getBoolean("maxHealth.restoreHealth",
                true);
        maxHealthParticleEffectIncrease = config.getBoolean(
                "maxHealth.particleEffect.increase", true);
        maxHealthParticleEffectDecrease = config.getBoolean(
                "maxHealth.particleEffect.decrease", true);

        loadWorldGroups(config);

        chatLanguage = config.getString("chat.language", "zhCN");
        chatChangeMessagesEnabled = config.getBoolean(
                "chat.changeMessagesEnabled", true);
        chatCommandMessagesEnabled = config.getBoolean(
                "chat.commandMessagesEnabled", true);

        commandMaxHealthEnabled = config.getBoolean(
                "commands.maxHealthEnabled", true);
        commandHealthEnabled = config
                .getBoolean("commands.healthEnabled", true);
        commandInfoEnabled = config.getBoolean("commands.infoEnabled", true);
    }

    @Override
    public void update(FileConfiguration config) {
        if (!config.getBoolean("config.autoUpdate", true)) {
            return;
        }
        String version = config.getString("config.version", "unknown");
        switch (version) {
            default:
            case "unknown":
            case "0.1":
            case "0.1.0":
            case "1.7.2-R0.1":
            case "0.1.1":
            case "1.7.2-R0.2":
                updateStart("0.1");

                double defaultMaxHealth = config.getDouble("default_maxhealth", 20);
                int maxHealthIncreaseLevelInterval = config.getInt(
                        "maxhealth_increase_level_interval", 5);
                double maximumMaxHealth = config.getDouble("maximum_maxhealth", 60);
                boolean resetMaxHealthOnLogin = config.getBoolean(
                        "reset_maxhealth_on_login", false);
                boolean resetMaxHealthOnDeath = config.getBoolean(
                        "reset_maxhealth_on_death", false);
                boolean restoreFullHealthOnMaxHealthChange = config.getBoolean(
                        "restore_full_health_on_maxhealth_change", true);
                boolean changeMaxHealthOnLevelChange = config.getBoolean(
                        "change_maxhealth_on_level_change", true);

                move(new File(getFile().getParentFile(), getFile().getName()
                        + ".old"));
                create();
                config.set("maxHealth.default", defaultMaxHealth);
                save();
                config.set(
                        "maxHealth.levelInterval",
                        changeMaxHealthOnLevelChange ? maxHealthIncreaseLevelInterval
                                : 0);
                config.set("maxHealth.limit", maximumMaxHealth);
                config.set("maxHealth.reset.login", resetMaxHealthOnLogin);
                config.set("maxHealth.reset.death", resetMaxHealthOnDeath);
                config.set("maxHealth.restoreHealth",
                        restoreFullHealthOnMaxHealthChange);
                config.set("config.version", "1.0.0");
            case "1.0":
            case "1.0.0":
            case "1.7.9-R0.1":
            case "1.1":
            case "1.1.0":
            case "1.7.9-R0.2":
                updateStart("1.1");
                setIfNotExists("config.autoUpdate", true);
                setIfNotExists("metricsEnabled", true);
                if (setIfNotExists("updateCheck.enabled", true)) {
                    log.warning("This plugin checks dev.bukkit.org for updates. It will not download anything to your server. You can disable this in config.yml / updateCheck.enabled = false.");
                }
                setIfNotExists("updateCheck.informOperators", true);
                setIfNotExists("maxHealth.particleEffect.increase", true);
                setIfNotExists("maxHealth.particleEffect.decrease", true);
            case "1.2":
            case "1.2.0":
                updateStart("1.2");
                log.warning("The encoding of LevelHearts files changed globally to UTF-8. This might have caused issues with a few characters, but it should be all fine.");
            case "1.2.1":
                updateStart("1.2.1");
                setIfNotExists("maxHealth.reset.levelChange", false);
                setIfNotExists("chat.changeMessagesEnabled", true);
                setIfNotExists("chat.commandMessagesEnabled", true);
            case "1.2.2":
            case "1.2.3":
            case "1.2.4":
                updateStart("1.2.4");
                setIfNotExists("storage.mode", "sqlite");
                setIfNotExists("storage.database", "localhost:3306/database");
                setIfNotExists("storage.table", "Health");
                setIfNotExists("storage.username", "root");
                setIfNotExists("storage.password", "");
            case "1.3":
            case "1.3.1":
            case "1.3.2":
                updateStart("1.3.2");
                setIfNotExists("maxHealth.levelMode", "exp");
            case "1.4":
            case "1.4.1":
            case "1.4.2":
            case "1.4.3":
            case "1.4.4":
                updateStart("1.4.4");
                setIfNotExists("maxHealth.startLevel", 0);
            case "1.4.5":
            case "1.4.6":
            case "1.4.7":
            case "1.4.8":
                updateDone("1.4.8");
        }
    }

    private void loadWorldGroups(FileConfiguration config) {
        worlds = new HashMap<>();
        worldGroups = new HashMap<>();
        for (String str : config.getStringList("worldGroups")) {
            String[] split = str.replaceAll(" ", "").split(":");

            String enabledStr = split[1];
            boolean enabled = false;
            switch (enabledStr.toLowerCase()) {
                case "enabled", "true", "yes", "on" -> enabled = true;
                case "disabled", "false", "no", "off" -> enabled = false;
                default -> log.warning("Enabled value for world group \"" + split[0]
                        + "\" cannot be read. Falling back to disabled.");
            }

            String[] worldsSplit = split[0].split(", ");
            Set<World> worlds = new HashSet<World>();
            for (String worldStr : worldsSplit) {
                World world = Bukkit.getWorld(worldStr);
                if (world != null) {
                    worlds.add(world);
                    this.worlds.put(world, enabled);
                }
            }
            if (!worlds.isEmpty()) {
                worldGroups.put(worlds, enabled);
            }
        }
    }

    public boolean configForceUpdate() {
        return configForceUpdate;
    }


    public String configVersion() {
        return configVersion;
    }


    public boolean metricsEnabled() {
        return metricsEnabled;
    }


    public String storageMode() {
        return storageMode;
    }

    public String storageDatabase() {
        return storageDatabase;
    }

    public String storageTable() {
        return storageTable;
    }

    public String storageUsername() {
        return storageUsername;
    }

    public String storagePassword() {
        return storagePassword;
    }

    public boolean updateCheckEnabled() {
        return updateCheckEnabled;
    }


    public boolean updateCheckInformOperators() {
        return updateCheckInformOperators;
    }


    public String maxHealthLevelMode() {
        return maxHealthLevelMode;
    }

    public double maxHealthDefault() {
        return maxHealthDefault;
    }


    public int maxHealthLevelInterval() {
        return maxHealthLevelInterval;
    }


    public int maxHealthStartLevel() {
        return maxHealthStartLevel;
    }

    public double maxHealthLimit() {
        return maxHealthLimit;
    }


    public boolean maxHealthResetLevelChange() {
        return maxHealthResetLevelChange;
    }

    public boolean maxHealthResetLogin() {
        return maxHealthResetLogin;
    }


    public boolean maxHealthResetDeath() {
        return maxHealthResetDeath;
    }


    public boolean maxHealthRestoreHealth() {
        return maxHealthRestoreHealth;
    }


    public boolean maxHealthParticleEffectIncrease() {
        return maxHealthParticleEffectIncrease;
    }

    public boolean maxHealthParticleEffectDecrease() {
        return maxHealthParticleEffectDecrease;
    }


    public Map<Set<World>, Boolean> worldGroups() {
        return worldGroups;
    }

    public boolean isWorldEnabled(World world) {
        if (!worlds.containsKey(world)) {
            return true;
        }
        return worlds.get(world);
    }

    public String chatLanguage() {
        return chatLanguage;
    }


    public boolean chatChangeMessagesEnabled() {
        return chatChangeMessagesEnabled;
    }


    public boolean chatCommandMessagesEnabled() {
        return chatCommandMessagesEnabled;
    }


    public boolean commandMaxHealthEnabled() {
        return commandMaxHealthEnabled;
    }


    public boolean commandHealthEnabled() {
        return commandHealthEnabled;
    }


    public boolean commandInfoEnabled() {
        return commandInfoEnabled;
    }


}
