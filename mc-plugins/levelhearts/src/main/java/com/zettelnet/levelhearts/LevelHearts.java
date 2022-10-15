package com.zettelnet.levelhearts;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import com.zettelnet.levelhearts.configuration.LanguageConfiguration;
import com.zettelnet.levelhearts.configuration.MainConfiguration;
import com.zettelnet.levelhearts.health.HealthFormat;
import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.storage.HealthStorage;

/**
 * @author yanni
 */
public class LevelHearts {

    public static LevelHeartsPlugin getPlugin() {
        return LevelHeartsPlugin.instance();
    }

    public static String getVersion() {
        return getPlugin().getVersion();
    }

    public static File getDataFolder() {
        return getPlugin().getDataFolder();
    }

    public static InputStream getResource(String filename) {
        return getPlugin().getResource(filename);
    }

    public static Logger getLogger() {
        return getPlugin().getLogger();
    }

    public static Logger getLog() {
        return getLogger();
    }

    public static MainConfiguration getConfiguration() {
        return getPlugin().getConfiguration();
    }

    public static LanguageConfiguration getLanguageConfiguration() {
        return getPlugin().getLanguageConfiguration();
    }

    public static LevelHeartsPermissions getPermissionManager() {
        return getPlugin().getPermissionManager();
    }

    public static HealthManager getHealthManager() {
        return getPlugin().getHealthManager();
    }

    public static HealthFormat getHealthFormat() {
        return getPlugin().getHealthFormat();
    }

    public static String getWebsite() {
        return getPlugin().getWebsite();
    }

    public static HealthStorage getHealthStorage() {
        return getPlugin().getHealthStorage();
    }
}
