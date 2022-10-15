package com.zettelnet.levelhearts.zet.configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Represents a ConfigurationFile that will be automatically created if it does
 * not exist.
 *
 * @author Zettelkasten
 * @see ConfigurationFile
 */
public abstract class ResourcedConfigurationFile extends ConfigurationFile {

    private final ByteArrayOutputStream resourceBytes;

    /**
     * Convenience constructor for getting a file in the data folder of the
     * plugin and a resource of the plugin-jar. This will use the default
     * configuration (empty {@link YamlConfiguration}).
     *
     * @param plugin   The plugin to get the data folder and the resource from
     * @param file     The file name of the file in the data folder
     * @param resource The resource name in the jar
     * @see ConfigurationFile
     * @see #defaultConfiguration()
     */
    public ResourcedConfigurationFile(Plugin plugin, String file, String resource) {
        this(plugin, file, resource, defaultConfiguration());
    }

    /**
     * Convenience constructor for getting a file in the data folder of the
     * plugin and a resource of the plugin-jar.
     *
     * @param plugin   The plugin to get the data folder and the resource from
     * @param file     The file name of the file in the data folder
     * @param resource The resource name in the jar
     * @param config   The config object it will use to get the values. Note that
     *                 different configuration types may have different parsing, so
     *                 this should be the same every time for each config file
     * @see ConfigurationFile
     */
    public ResourcedConfigurationFile(Plugin plugin, String file, String resource, FileConfiguration config) {
        this(new File(plugin.getDataFolder(), file), plugin.getResource(resource), config);
    }

    /**
     * Constructs a resourced configuration file using the default configuration
     * (empty {@link YamlConfiguration}).
     *
     * @param file     The file the configuration will use
     * @param resource The input stream containing the resource of this configuration
     * @see ConfigurationFile
     * @see #defaultConfiguration()
     */
    public ResourcedConfigurationFile(File file, InputStream resource) {
        this(file, resource, defaultConfiguration());
    }

    /**
     * Constructs a resourced configuration.
     *
     * @param file     The file the configuration will use
     * @param resource The input stream containing the resource of this configuration
     * @param config   The config object it will use to get the values. Note that
     *                 different configuration types may have different parsing, so
     *                 this should be the same every time for each config file
     * @see ConfigurationFile
     */
    public ResourcedConfigurationFile(File file, InputStream resource, FileConfiguration config) {
        super(file, config);
        this.resourceBytes = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = resource.read(buf)) > 0) {
                this.resourceBytes.write(buf, 0, len);
            }
            this.resourceBytes.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the stream used to create the file if it was not found
     *
     * @return The input stream used as resource
     */
    public final InputStream getResource() {
        return new ByteArrayInputStream(this.resourceBytes.toByteArray());
    }

    /**
     * Loads the configuration and the values from the file. This method will
     * call loadValues() afterwards.
     * <p>
     * When the file does not exist, it will call create() to create a new file
     * an call onFileCreate() as a notifier.
     *
     * @see #create()
     * @see ConfigurationFile#loadValues(FileConfiguration)
     */
    @Override
    public void load() throws IOException, InvalidConfigurationException {
        if (!getFile().exists()) {
            create();
            onFileCreate();
        }
        super.load();
    }

    /**
     * Called when load() has created a new file. Note that create() does not
     * call this method.
     * <p>
     * This method can be overridden to create a notifier.
     *
     * @see #load()
     * @see #create()
     */
    protected void onFileCreate() {
    }

    /**
     * Creates a new file for this configuration using the resource input
     * stream. This does not load the values.
     *
     * @throws IOException If the file cannot be created
     */
    public void create() throws IOException {
        getFile().getParentFile().mkdirs();
        InputStream in = getResource();
        OutputStream out = new FileOutputStream(getFile());
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Resets this configuration file by removing it and then reloading it.
     *
     * @throws IOException                   If the file cannot be deleted or cannot be reloaded
     * @throws InvalidConfigurationException If the content of the file is invalid or cannot be parsed
     * @see #load()
     */
    public void reset() throws IOException, InvalidConfigurationException {
        getFile().delete();
        load();
    }
}
