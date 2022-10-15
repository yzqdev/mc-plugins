package com.zettelnet.levelhearts.zet.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Represents a FileConfiguration which is bound to a File. This is used for
 * easy loading, saving and other file based operations without needing a file
 * passed to the configuration.
 * <p>
 * An instance of this can exist even if the file does not.
 * <p>
 * This class should be used by extending it and creating a method for each
 * value. This allows other classes to access them without direct access to the
 * configuration. Note that using fields instead of methods can cause issues
 * because other classes could change them.
 * <p>
 * The intention of this class is to provide an easy and fast backbone for
 * configuration classes by hooking it directly to a specific file. It was built
 * to support as many configuration types as possible by giving the ability to
 * pass a custom {@link FileConfiguration}.
 *
 * @author Zettelkasten
 */
public abstract class ConfigurationFile {

    private final FileConfiguration config;
    private final File file;

    private Charset charset;

    /**
     * Constructs a new ConfigurationFile with the default configuration type
     * {@link YamlConfiguration}).
     *
     * @param file The file the configuration will use
     * @see #defaultConfiguration();
     */
    public ConfigurationFile(File file) {
        this(file, defaultConfiguration());
    }

    /**
     * Constructs a new ConfigurationFile with a specified configuration. This
     * will not load the configuration.
     *
     * @param file   The file the configuration will save and load to
     * @param config The configuration object it will use to get the values. Note
     *               that different configuration types may have different parsing,
     *               so this should be the same every time for each configuration
     *               file
     */
    public ConfigurationFile(File file, FileConfiguration config) {
        this.file = file;
        this.config = config;

        this.charset = defaultCharset();
    }

    /**
     * Gets the current FileConfiguration containing all loaded values
     *
     * @return The config file
     */
    public final FileConfiguration getConfig() {
        return config;
    }

    /**
     * Gets the file this configuration will load from and save to
     *
     * @return The file
     */
    public final File getFile() {
        return file;
    }

    /**
     * Gets the name of the file as a convenience method. This has the same
     * effect as getFile().getName().
     *
     * @return The file name
     * @see #getFile()
     */
    public String getFileName() {
        return file.getName();
    }

    /**
     * Gets the charset this configuration file is encoded. This does not mean
     * that the file is encoded in the charset, but it will be read and saved in
     * this charset. The default charset is UTF-8.
     *
     * @return The charset
     * @see #setCharset(Charset)
     * @see #defaultCharset()
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Changes the charset of this configuration. This will not convert the file
     * to the new encoding but change the encoding the file will be read.
     *
     * @param charset The new charset
     * @see #getCharset()
     * @see #setCharset(String)
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Changes the charset of this configuration. This will not convert the file
     * to the new encoding but change the encoding the file will be read.
     *
     * @param charsetName The name of the new charset
     * @see #getCharset()
     * @see #setCharset(Charset)
     */
    public void setCharset(String charsetName) {
        setCharset(Charset.forName(charsetName));
    }

    /**
     * Loads values from an {@link Reader} into a {@link FileConfiguration}. All
     * values in the file configuration will be removed before the new values
     * are put in.
     *
     * @param config The configuration. Used to change the type of the file (e.g.
     *               YAML).
     * @param reader A reader reading the content of the configuration
     * @return The same configuration as passed as parameter now containing the
     * values loaded
     * @throws IOException                   If the reader cannot read properly
     * @throws InvalidConfigurationException If the configuration is invalid and cannot be read
     * @see #loadConfiguration(FileConfiguration, File, Charset)
     * @see #loadConfiguration(FileConfiguration, InputStream, Charset)
     */
    public static FileConfiguration loadConfiguration(FileConfiguration config,
                                                      Reader reader) throws IOException, InvalidConfigurationException {
        for (Entry<String, Object> entry : config.getValues(false).entrySet()) {
            config.set(entry.getKey(), null);
        }

        BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader
                : new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }
        config.loadFromString(builder.toString());
        return config;
    }

    /**
     * Loads values from a file into a {@link FileConfiguration}. All values in
     * the file configuration will be removed before the new values are put in.
     *
     * @param config  The configuration. Used to change the type of the file (e.g.
     *                YAML).
     * @param file    The file to load the values from
     * @param charset The charset the file is encoded in
     * @return The same configuration as passed as parameter now containing the
     * values loaded
     * @throws FileNotFoundException         If the file cannot be found
     * @throws IOException                   If the reader cannot read properly
     * @throws InvalidConfigurationException If the configuration is invalid and cannot be read
     * @see #loadConfiguration(FileConfiguration, Reader)
     * @see #loadConfiguration(FileConfiguration, InputStream, Charset)
     */
    public static FileConfiguration loadConfiguration(FileConfiguration config,
                                                      File file, Charset charset) throws FileNotFoundException,
            IOException, InvalidConfigurationException {
        return loadConfiguration(config, new InputStreamReader(
                new FileInputStream(file), charset));
    }

    /**
     * Loads values from an input stream into a {@link FileConfiguration}. All
     * values in the file configuration will be removed before the new values
     * are put in.
     *
     * @param config  The configuration. Used to change the type of the file (e.g.
     *                YAML).
     * @param input   The input stream to get the content from. Useful if no file
     *                can be used (e.g. for resources).
     * @param charset The charset the file is encoded in
     * @return The same configuration as passed as parameter now containing the
     * values loaded
     * @throws IOException                   If the reader cannot read properly
     * @throws InvalidConfigurationException If the configuration is invalid and cannot be read
     * @see #loadConfiguration(FileConfiguration, Reader)
     * @see #loadConfiguration(FileConfiguration, InputStream, Charset)
     */
    public static FileConfiguration loadConfiguration(FileConfiguration config,
                                                      InputStream input, Charset charset) throws IOException,
            InvalidConfigurationException {
        return loadConfiguration(config, new InputStreamReader(input, charset));
    }

    /**
     * Loads the configuration and the values from the file. This method will
     * call {@link #loadValues(FileConfiguration)} afterwards.
     *
     * @throws FileNotFoundException         If the file cannot be found
     * @throws IOException                   If some other error occurs while reading the file
     * @throws InvalidConfigurationException If the content of the file is invalid and cannot be parsed
     * @see #loadValues(FileConfiguration)
     * @see #save()
     */
    public void load() throws FileNotFoundException, IOException,
            InvalidConfigurationException {

        loadConfiguration(config, file, charset);
        loadValues(config);
    }

    /**
     * Loads the values from a loaded file to store them. This method is called
     * after the file is loaded using load().
     *
     * @param config The configuration previously loaded
     * @see #load()
     */
    protected abstract void loadValues(FileConfiguration config);

    /**
     * Saves the configuration to the file. If no configuration is loaded and
     * this method is called, an empty file will be saved.
     *
     * @throws IOException If the file cannot be saved
     * @see #load()
     */
    public void save() throws IOException {
        Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), charset));
        fileWriter.write(config.saveToString());
        fileWriter.close();
    }

    /**
     * Moves the file to another location. If the target file already exists, it
     * will be deleted before moving. This operation does not save or load
     * anything and copy the file using the file system.
     * <p>
     * The file will be changed, so it will not exist after this operation, but
     * the current configuration still is the same so {@link save()} can be used
     * to save it.
     * <p>
     * Note that this method is a standard file operation independent from the
     * configuration.
     *
     * @param toFile Where to move the file to
     * @throws IOException If the move fails or the target file exists but cannot be
     *                     removed
     * @see #load()
     * @see #save()
     */
    public void move(File toFile) throws IOException {
        if (toFile.exists()) {
            toFile.delete();
        }
        if (!file.renameTo(toFile)) {
            throw new IOException("File not moved!");
        }
    }

    /**
     * Copies the file to another location. The target file will be overridden
     * if it exists.
     * <p>
     * Note that this method is a standard file operation independent from the
     * configuration.
     *
     * @param toFile Where to copy the file to
     * @throws IOException If the file cannot be copied or toFile exists and cannot be
     *                     overridden
     * @see #load()
     * @see #save()
     */
    public void copy(File toFile) throws IOException {
        if (toFile.exists()) {
            toFile.delete();
        }
        InputStream is = null;
        OutputStream os = null;
        is = new FileInputStream(file);
        os = new FileOutputStream(toFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }

    /**
     * An convenience method for setting configuration values only if they do
     * not exists. This will not save the file nor use
     * loadValues(FileConfiguration) to reload the values.
     *
     * @param path  The path in the configuration where the value is found
     * @param value The value that will be set if no one exists
     * @return True if no value was found at that path and one was created;
     * false if not
     */
    protected boolean setIfNotExists(String path, Object value) {
        if (!config.contains(path)) {
            config.set(path, value);
            return true;
        }
        return false;
    }

    /**
     * The default configuration that is used when no specific configuration is
     * specified. The default is a new YamlConfiguration().
     *
     * @return The default configuration ({@code new YamlConfiguration()})
     */
    public static FileConfiguration defaultConfiguration() {
        return new YamlConfiguration();
    }

    /**
     * The default configuration that is used when no specific charset is
     * specified. The default is UTF-8.
     *
     * @return The default charset ({@code Charset.forName("UTF-8")})
     */
    public static Charset defaultCharset() {
        return StandardCharsets.UTF_8;
    }
}
