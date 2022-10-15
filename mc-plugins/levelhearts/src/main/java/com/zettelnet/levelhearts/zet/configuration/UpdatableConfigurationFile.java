package com.zettelnet.levelhearts.zet.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Represents a resourced configuration file that attempts to automatically
 * update itself. The update is not directly performed by this class, but it
 * offers a set of functions to easily add this to a configuration file.
 *
 * @author Zettelkasten
 * @see #update(FileConfiguration)
 */
public abstract class UpdatableConfigurationFile extends
        ResourcedConfigurationFile {

    private String updateFromVersion;
    private String versionKey;

    public UpdatableConfigurationFile(Plugin plugin, String file,
                                      String resource) {
        this(plugin, file, resource, defaultConfiguration());
    }

    public UpdatableConfigurationFile(Plugin plugin, String file,
                                      String resource, FileConfiguration config) {
        this(new File(plugin.getDataFolder(), file), plugin
                .getResource(resource), config);
    }

    public UpdatableConfigurationFile(File file, InputStream resource) {
        this(file, resource, defaultConfiguration());
    }

    public UpdatableConfigurationFile(File file, InputStream resource,
                                      FileConfiguration config) {
        super(file, resource, config);
        this.updateFromVersion = null;
        this.versionKey = defaultVersionKey();
    }

    /**
     * Updates the configuration file. This method should be overridden by all
     * updatable configurations. The method should be called in
     * {@link#loadValues(FileConfiguration)}, it will not be called
     * automatically.
     * <p>
     * An update method can use a <em>switch (version)</em> to refer to all
     * versions, for example:
     *
     * <pre>
     * {@code
     * // Note that the switch String cannot be null and has to be replaced with something else ("unknown")
     * switch (config.getString("config.version", "unknown")) {
     * default:
     * case "unknown":
     * case "0.0.1":
     *   updateStart("0.0.1");
     *   // Changes for update v0.0.1
     * case "0.0.2":
     *   updateStart("0.0.2");
     *   // Changes for update v0.0.2
     * case "0.1.0":
     *   updateStart("0.1.0");
     *   // Changes for update v0.1.0
     * case "0.1.1":
     * case "0.1.2":
     *   // Nothing changed in update v0.1.1
     *   // v0.1.2 is the latest version
     *   updateDone("0.1.2");
     * }
     * </pre>
     *
     * @param config
     * @see #updateStart(String)
     * @see #updateDone(String)
     */
    protected void update(FileConfiguration config) {
    }

    /**
     * Signals that the configuration has started updating from a specific
     * version. Calling this method more then once has no effect, the first
     * version will always be used until {@link #updateDone(String)} is called.
     * <p>
     * This method will call {@link #onUpdateStart(String)} when called first.
     *
     * @param fromVersion The version the update starts from
     * @see #update(FileConfiguration)
     * @see #updateDone(String)
     * @see #onUpdateStart(String)
     */
    protected void updateStart(String fromVersion) {
        if (this.updateFromVersion == null) {
            this.updateFromVersion = fromVersion;
            onUpdateStart(fromVersion);
        }
    }

    /**
     * Called when an update is first started. This can be used as a trigger or
     * notifier when overridden.
     *
     * @param fromVersion The version the update started from
     * @see #updateStart(String)
     */
    protected void onUpdateStart(String fromVersion) {
    }

    /**
     * Signals that the configuration is done updating and is now a specific
     * version. This method can be called even if no update has been started
     * using {@link #updateStart(String)}, but will have no effect.
     * <p>
     * When the configuration was updated, if will change the version of the
     * configuration file to the new one and save it. The trigger
     * {@link #onUpdateDone(String, String)} will be called afterwards.
     *
     * @param toVersion The version this configuration file is now
     * @throws IOException If saving the configuration fails
     * @see #update(FileConfiguration)
     * @see #updateStart(String)
     * @see #onUpdateDone(String, String)
     */
    protected void updateDone(String toVersion) throws IOException {
        if (this.updateFromVersion != null) {
            getConfig().set(this.versionKey, toVersion);
            save();
            onUpdateDone(this.updateFromVersion, toVersion);
            this.updateFromVersion = null;
        }
    }

    /**
     * Called when the configuration was updated. This can be used as a trigger
     * or notifier when overridden.
     *
     * @param fromVersion The version this configuration was updated from
     * @param toVersion   The current version the configuration was updated to
     * @see #updateDone(String)
     */
    protected void onUpdateDone(String fromVersion, String toVersion) {
    }

    /**
     * Gets the current version of the configuration. This uses
     * {@link #getVersionKey()} to find out the version.
     *
     * @return The current version, or <code>"unknown"</code> if no version was
     * found
     * @see #getVersionKey()
     */
    public String getVersion() {
        return getConfig().getString(versionKey, "unknown");
    }

    /**
     * Gets the key used to determine the version of the configuration. Default
     * is "config.version".
     *
     * @return The key where the configuration version can be found
     * @see #getVersion()
     * @see #setVersionKey(String)
     */
    protected String getVersionKey() {
        return versionKey;
    }

    /**
     * Changes the key used to determine the version of the configuration.
     *
     * @param key The new key to be used
     * @see #getVersion()
     * @see #getVersionKey()
     */
    protected void setVersionKey(String key) {
        this.versionKey = key;
    }

    /**
     * Gets the default configuration key to find the version of a configuration
     * file.
     *
     * @return The default key "config.version"
     * @see #getVersionKey()
     * @see #setVersionKey(String)
     * @see #getVersion()
     */
    public static String defaultVersionKey() {
        return "config.version";
    }
}
