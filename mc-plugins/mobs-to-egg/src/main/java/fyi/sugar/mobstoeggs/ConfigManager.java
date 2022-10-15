package fyi.sugar.mobstoeggs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class ConfigManager {
    private static Main plugin = Main.getPlugin(Main.class);

    public static FileConfiguration dataConfig;

    public static FileConfiguration settingsConfig;

    public static FileConfiguration messagesConfig;

    public static File dataFile;
    public static File messagesFile;
    public static File settingsFile;

    public static void matchConfig(FileConfiguration tconfig, File file, String flocation) {
        String pluginVer = plugin.mtev;
        String configVer = tconfig.getString("config-version");

        if (!configVer.equalsIgnoreCase(pluginVer)) {
            try {
                InputStream newConfigStream = plugin.getClass().getResourceAsStream(flocation + flocation);
                if (newConfigStream == null) {
                    return;
                }
                InputStreamReader newConfigStreamReader = new InputStreamReader(newConfigStream);
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(newConfigStreamReader);
                Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.YELLOW + "MTE | Updating " + ChatColor.YELLOW + " - v" + file.getName() + " -> v" + configVer);

                for (String field : yamlConfiguration.getConfigurationSection("").getKeys(false)) {
                    if (tconfig.get(field) == null) {
                        tconfig.set(field, yamlConfiguration.get(field));
                    }
                }

                tconfig.set("config-version", plugin.getDescription().getVersion());
                tconfig.save(file);
                newConfigStreamReader.close();
                newConfigStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }


        dataFile = new File(plugin.getDataFolder(), "mobs.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("mobs.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | mobs.yml has been created!");
        }


        settingsFile = new File(plugin.getDataFolder(), "config.yml");
        if (!settingsFile.exists()) {
            plugin.saveResource("config.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | config.yml has been created!");
        }


        settingsConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(settingsFile);
        dataConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(dataFile);

        matchConfig(settingsConfig, settingsFile, "/");
        matchConfig(dataConfig, dataFile, "/");


        String langfile = settingsConfig.getString("language").toLowerCase();
        messagesFile = new File(plugin.getDataFolder(), "/messages/" + langfile + ".yml");
        if (!messagesFile.exists()) {
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.YELLOW + "MTE | No language files found, creating files now...");
            plugin.saveResource("messages/en.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | Created en.yml");
            plugin.saveResource("messages/fr.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | Created fr.yml");
            plugin.saveResource("messages/es.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | Created es.yml");
            plugin.saveResource("messages/zh-s.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | Created zh-s.yml");
            plugin.saveResource("messages/zh-t.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | Created zh-t.yml");
            plugin.saveResource("messages/de.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | Created de.yml");
            messagesFile = new File(plugin.getDataFolder(), "/messages/" + langfile + ".yml");
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.GREEN + "MTE | Language files have been created!");
            if (!messagesFile.exists()) {
                Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | Failed to load the language file '" + ChatColor.RED + "' as it does not exist!");
            }
        }


        messagesConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(messagesFile);
        matchConfig(messagesConfig, messagesFile, "/messages/");
    }


    public FileConfiguration getMobs() {
        if (dataConfig == null) {
            setup();
        }
        return dataConfig;
    }

    public FileConfiguration getSettings() {
        if (settingsConfig == null) {
            setup();
        }
        return settingsConfig;
    }

    public FileConfiguration getMessages() {
        if (messagesConfig == null) {
            setup();
        }
        return messagesConfig;
    }


    public void saveMobs() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | FAILED TO SAVE mobs.yml");
            e.printStackTrace();
        }
    }

    public static void saveSettings() {
        try {
            settingsConfig.save(settingsFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | FAILED TO SAVE config.yml");
            e.printStackTrace();
        }
    }

    public void saveMessages() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | FAILED TO SAVE messages.yml");
            e.printStackTrace();
        }
    }

    public void reloadConfigs() {
        setup();
    }
}


