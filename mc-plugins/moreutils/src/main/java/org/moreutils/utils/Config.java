package org.moreutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.moreutils.MoreUtils;

import java.io.File;
import java.io.IOException;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 1:01
 * @Modified By:
 */
public class Config {
    private static YamlConfiguration config;
    static  File FILE =new File(MoreUtils.getInstance().getDataFolder() + File.separator + "config.yml");



    public static YamlConfiguration getConfig() {
        return config;
    }

    public static void createConfig() {

        configFile();

    }

    public static void createDefaultConfig() {
        if (FILE.exists()) {
            FILE.delete();
        }
        configFile();
    }

    public static void configFile() {
        Bukkit.getConsoleSender().sendMessage("[" + MoreUtils.getInstance().getName() + "] §cCreate Config.yml");
        config = new YamlConfiguration();
        config.set("player-max-health", 20);

        config.set("to-egg-chance",80);
        try {
            config.save(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        if (!FILE.exists()) {
            createConfig();
            return;
        }
        Bukkit.getConsoleSender().sendMessage("[" + MoreUtils.getInstance().getName() + "] §aFind config.yml");

        config = new YamlConfiguration();

        try {
            config.load(FILE);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("[" + MoreUtils.getInstance().getName() + "] §c读取config时发生错误");
        }
    }

    public static String getConfig(String loc) {
        String raw = config.getString(loc);
        if (raw == null || raw.isEmpty()) {
            createConfig();
            raw = config.getString(loc);
            return raw;
        }
        raw = raw.replace("&", "§");
        return raw;
    }

    public static void setConfig(String label, String userVar) {
        config = new YamlConfiguration();
        config.set(label, userVar);
        try {
            config.save(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
