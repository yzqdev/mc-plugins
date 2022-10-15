package com.zettelnet.levelhearts.zet.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * An convenience class representing an updatable configuration file but
 * catching all exceptions and printing error messages.
 * <p>
 * It is preferred to extend this class when creating a configuration file
 * because it takes care of error handling.
 *
 * @author Zettelkasten
 */
public abstract class PluginConfigurationFile extends UpdatableConfigurationFile {

    private static Logger log = Bukkit.getLogger();

    private boolean updateSilently;

    public PluginConfigurationFile(Plugin plugin, String file, String resource) {
        this(plugin, file, resource, defaultConfiguration());
    }

    public PluginConfigurationFile(Plugin plugin, String file, String resource, FileConfiguration config) {
        this(new File(plugin.getDataFolder(), file), plugin.getResource(resource), config);
    }

    public PluginConfigurationFile(File file, InputStream resource) {
        this(file, resource, defaultConfiguration());
    }

    public PluginConfigurationFile(File file, InputStream resource, FileConfiguration config) {
        super(file, resource, config);
        this.updateSilently = false;
    }

    public static FileConfiguration loadConfiguration(FileConfiguration config, Reader reader) {
        try {
            return ConfigurationFile.loadConfiguration(config, reader);
        } catch (IOException e) {
            log.warning("Failed to load configuration file using reader:");
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            log.warning("Failed to load configuration file using reader. Configuration invalid:");
            e.printStackTrace();
        }
        return config;
    }

    public static FileConfiguration loadConfiguration(FileConfiguration config, File file, Charset charset) {
        try {
            return ConfigurationFile.loadConfiguration(config, file, charset);
        } catch (FileNotFoundException e) {
            log.warning("Failed to load configuration file using reader. File not found:");
            e.printStackTrace();
        } catch (IOException e) {
            log.warning(String.format("Failed to load configuration file %s:", file.getName()));
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            log.warning(String.format("Failed to load configuration file %s. Configuration invalid:", file.getName()));
            e.printStackTrace();
        }
        return config;
    }

    public static FileConfiguration loadConfiguration(FileConfiguration config, InputStream input, Charset charset) {
        try {
            return ConfigurationFile.loadConfiguration(config, input, charset);
        } catch (IOException e) {
            log.warning("Failed to load configuration file using stream:");
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            log.warning("Failed to load configuration file using stream. Configuration invalid:");
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public void load() {
        try {
            super.load();
        } catch (FileNotFoundException e) {
            log.warning(String.format("Failed to load configuration file %s! File not found:", getFileName()));
            e.printStackTrace();
        } catch (IOException e) {
            log.warning(String.format("Failed to load configuration file %s:", getFileName()));
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            log.warning(String.format("Failed to load configuration file %s! The configuration is invalid:", getFileName()));
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            super.save();
        } catch (IOException e) {
            log.warning(String.format("Failed to save configuration file %s:", getFileName()));
            e.printStackTrace();
        }
    }

    @Override
    public void create() {
        try {
            super.create();
        } catch (IOException e) {
            log.warning(String.format("Failed to create configuration file %s:", getFileName()));
            e.printStackTrace();
        }
    }

    /**
     * Prints a message when the file was created.
     */
    @Override
    protected void onFileCreate() {
        log.info(String.format("Created new configuration file %s from defaults.", getFileName()));
    }

    @Override
    public void reset() {
        try {
            super.reset();
        } catch (IOException e) {
            log.warning(String.format("Failed to reset configuration file %s:", getFileName()));
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            log.warning(String.format("Failed to reset configuration file %s! The configuration is invalid:", getFileName()));
            e.printStackTrace();
        }
    }

    @Override
    public void move(File newFile) {
        try {
            super.move(newFile);
        } catch (IOException e) {
            log.warning(String.format("Failed to move file %s to %s:", getFileName(), newFile.getName()));
            e.printStackTrace();
        }
    }

    @Override
    public void copy(File newFile) {
        try {
            super.copy(newFile);
        } catch (IOException e) {
            log.warning(String.format("Failed to copy file %s to %s:", getFileName(), newFile.getName()));
            e.printStackTrace();
        }
    }

    @Override
    protected void onUpdateStart(String startVersion) {
        if (!updateSilently) {
            log.warning(String.format("Configuration file %s is outdatet (v%s)!", getFileName(), startVersion));
            log.info(String.format("Attempting to update %s ...", getFileName()));
        }
    }

    @Override
    protected void updateDone(String toVersion) {
        try {
            super.updateDone(toVersion);
        } catch (IOException e) {
            log.warning(String.format("Failed to save updated configuration file %s to version %s:", getFileName(), toVersion));
            e.printStackTrace();
        }
    }

    @Override
    protected void onUpdateDone(String startVersion, String endVersion) {
        if (!updateSilently) {
            log.info(String.format("Configuration file %s has been updatet automatically from v%s to v%s!", getFileName(), startVersion, endVersion));
            log.info("Please check if the update was successful; a backup has been created.");
        }
    }

    public static void setLogger(Logger logger) {
        log = logger;
    }

    protected static Logger getLogger() {
        return log;
    }

    protected void setUpdateSilently(boolean updateSilently) {
        this.updateSilently = updateSilently;
    }

    public boolean isUpdateSilently() {
        return updateSilently;
    }
}
