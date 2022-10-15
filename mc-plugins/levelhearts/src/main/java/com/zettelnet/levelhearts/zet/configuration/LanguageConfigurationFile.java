package com.zettelnet.levelhearts.zet.configuration;

import java.io.File;
import java.io.InputStream;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.zettelnet.levelhearts.zet.chat.ChatMessage;
import com.zettelnet.levelhearts.zet.chat.FormatOption;
import com.zettelnet.levelhearts.zet.chat.FormattedChatMessage;
import com.zettelnet.levelhearts.zet.chat.MessageValueMap;

public abstract class LanguageConfigurationFile extends PluginConfigurationFile {

    private final MessageValueMap formatOptions;

    private final FileConfiguration defaultConfiguration;
    private boolean defaultConfigurationLoaded;

    public LanguageConfigurationFile(Plugin plugin, String file, String resource) {
        this(plugin, file, resource, defaultConfiguration());
    }

    public LanguageConfigurationFile(Plugin plugin, String file, String resource, FileConfiguration config) {
        this(new File(plugin.getDataFolder(), file), plugin.getResource(resource), config);
    }

    public LanguageConfigurationFile(File file, InputStream resource) {
        this(file, resource, defaultConfiguration());
    }

    public LanguageConfigurationFile(File file, InputStream resource, FileConfiguration config) {
        super(file, resource, config);

        this.formatOptions = new MessageValueMap();
        this.formatOptions.put("aqua", ChatColor.AQUA.toString());
        this.formatOptions.put("black", ChatColor.BLACK.toString());
        this.formatOptions.put("blue", ChatColor.BLUE.toString());
        this.formatOptions.put("bold", ChatColor.BOLD.toString());
        this.formatOptions.put("darkAqua", ChatColor.DARK_AQUA.toString());
        this.formatOptions.put("darkBlue", ChatColor.DARK_BLUE.toString());
        this.formatOptions.put("darkGray", ChatColor.DARK_GRAY.toString());
        this.formatOptions.put("darkGreen", ChatColor.DARK_GREEN.toString());
        this.formatOptions.put("darkPurple", ChatColor.DARK_PURPLE.toString());
        this.formatOptions.put("darkRed", ChatColor.DARK_RED.toString());
        this.formatOptions.put("gold", ChatColor.GOLD.toString());
        this.formatOptions.put("gray", ChatColor.GRAY.toString());
        this.formatOptions.put("green", ChatColor.GREEN.toString());
        this.formatOptions.put("italic", ChatColor.ITALIC.toString());
        this.formatOptions.put("lightPurple", ChatColor.LIGHT_PURPLE.toString());
        this.formatOptions.put("magic", ChatColor.MAGIC.toString());
        this.formatOptions.put("red", ChatColor.RED.toString());
        this.formatOptions.put("reset", ChatColor.RESET.toString());
        this.formatOptions.put("strikethrough", ChatColor.STRIKETHROUGH.toString());
        this.formatOptions.put("underline", ChatColor.UNDERLINE.toString());
        this.formatOptions.put("white", ChatColor.WHITE.toString());
        this.formatOptions.put("yellow", ChatColor.YELLOW.toString());

        this.formatOptions.put("heart", new FormatOption("\u2764", "", ""));
        this.formatOptions.put("heartSmall", new FormatOption("\u2665", "", ""));
        this.formatOptions.put("heartWhite", new FormatOption("\u2661", "", ""));
        this.formatOptions.put("heartRotated", new FormatOption("\u2765", "", ""));
        this.formatOptions.put("heartExclamation", new FormatOption("\u2763", "", ""));

        this.formatOptions.put("br", new FormatOption("\n", System.lineSeparator(), System.lineSeparator()));
        this.formatOptions.put("...", new FormatOption("\u2026", "...", "..."));
        this.formatOptions.put(" ", new FormatOption("  "));

        this.defaultConfiguration = defaultConfiguration();
        this.defaultConfigurationLoaded = false;
    }

    /**
     * 设置语言
     *
     * @return string
     */
    public abstract String getLanguage();

    public void loadDefaults() {
        defaultConfigurationLoaded = true;
        loadConfiguration(this.defaultConfiguration, getResource(), getCharset());
    }

    public FileConfiguration getDefaultConfig() {
        return this.defaultConfiguration;
    }

    @Override
    public void load() {
        if (!defaultConfigurationLoaded) {
            loadDefaults();
        }
        super.load();
    }

    @Override
    protected void loadValues(FileConfiguration config) {
        if (!config.contains(getLanguage())) {
            getLogger().warning(
                    "Language " + getLanguage() + " not found in confguration file " + getFileName() + ". Falling back to default values. "
                            + "To create a new language just copy, rename and edit an existing language.");
        }
        loadValues(config, this.defaultConfiguration);
    }

    protected abstract void loadValues(FileConfiguration paramFileConfiguration1, FileConfiguration paramFileConfiguration2);

    protected ChatMessage load(String path, String defaultValue) {
        path = getLanguage() + "." + path;
        String value = getConfig().getString(path);
        if (value == null) {
            value = getDefaultConfig().getString(path, defaultValue);
        }
        return new FormattedChatMessage(value, defaultValue, this.formatOptions);
    }


    public MessageValueMap getFormatOptions() {
        return formatOptions;
    }

    protected void addFormatOption(String name, CharSequence option) {
        this.formatOptions.put(name, option);
    }
}