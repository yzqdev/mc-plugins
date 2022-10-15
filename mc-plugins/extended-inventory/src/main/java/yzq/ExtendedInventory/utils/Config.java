package yzq.ExtendedInventory.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import yzq.ExtendedInventory.ExtendedInventory;
import yzq.ExtendedInventory.SQL.MySQL;

import java.io.*;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 1:01
 * @Modified By:
 */
public class Config extends YamlConfiguration {
    static File FILE = new File(ExtendedInventory.getInstance().getDataFolder() + File.separator + "config.yml");


    private static YamlConfiguration config = new YamlConfiguration();
    public Config(File file) {
        try {
            loadData(file);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }
    public static void reload() {
        try {
            config.load(FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void createConfig() {

        configFile();

    }

    public static void createDefaultConfig() {
        if (FILE.exists()) {
            FILE.delete();
            configFile();
        } else {
            configFile();
        }
    }


    public static void configFile() {
        Bukkit.getConsoleSender().sendMessage("[" + ExtendedInventory.getInstance().getName() + "] §cCreate Config.yml");

        ExtendedInventory.getInstance().saveDefaultConfig();
    }

    /**
     * 生成其他配置文件
     * @param plugin
     * @param name
     */
    public static void loadResourceFile(JavaPlugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name);

        if (file.exists()) {
            return;
        }
        try {
            ExtendedInventory.getInstance().saveResource(name, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置默认配置文件
     */
    public static void loadDefaultConfig() {
        if (!FILE.exists()) {
            createConfig();
            return;
        }
        Bukkit.getConsoleSender().sendMessage("[" + ExtendedInventory.getInstance().getName() + "] §aFind config.yml");

        config = new YamlConfiguration();

        try {
            config.load(FILE);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("[" + ExtendedInventory.getInstance().getName() + "] §c读取config时发生错误");
        }
    }

    /**
     * 获取配置文件内容
     * @param fileName
     * @return
     */
    public static YamlConfiguration getConfigFiles(String fileName) {

        File file = new File(ExtendedInventory.getInstance().getDataFolder(), fileName);
        return loadConfiguration(file);
    }

    /**
     * 获取配置文件字段值
     * @param loc
     * @return
     */
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

    /**
     * 设置配置文件字段值
     * @param label
     * @param userVar
     * @return
     */
    public static boolean setConfig(String label, String userVar) {
        config = new YamlConfiguration();
        config.set(label, userVar);
        try {
            config.save(FILE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取sql数据库
     */
    public static void readSQLData() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File(ExtendedInventory.getInstance().getDataFolder(), "MySQL.yml"));
        MySQL.host = cfg.getString("MySQL.Host");
        MySQL.port = cfg.getString("MySQL.Port");
        MySQL.database = cfg.getString("MySQL.Database");
        MySQL.username = cfg.getString("MySQL.Username");
        MySQL.passwort = cfg.getString("MySQL.Password");
    }

    public static File getFile(String path, String name) {
        return new File("plugins/ExtendedInventory" + path, name);
    }

    /**
     * 保存数据文件
     * @param file
     * @param path
     * @param obj
     */
    public static void saveDataFiles(File file, String path, Object obj) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(path, obj);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据包
     * @param file
     * @throws IOException
     * @throws InvalidConfigurationException
     */
    public void loadData(File file) throws IOException, InvalidConfigurationException {
        if (file.exists()) {
            load(new InputStreamReader(new FileInputStream(file)));
        }

    }

    /**
     * 获取数据文件内容
     * @param path
     * @param fileName
     * @return
     */
    public static FileConfiguration getDataFiles(String path, String fileName)  {
       return new Config(getFile(path,fileName));
    }
}
