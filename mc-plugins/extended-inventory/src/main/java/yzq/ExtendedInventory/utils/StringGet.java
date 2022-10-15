package yzq.ExtendedInventory.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Objects;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2020/1/20 14:59
 * @Modified By:
 */
public class StringGet {


    public static String getString(String path) {

        YamlConfiguration sqlcfg = Config.getConfigFiles("MySQL.yml");
        YamlConfiguration cfg = Config.getConfigFiles("config.yml");
        String prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(cfg.getString("Prefix")));
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Locale.getLangFiles().getString(path))).replace("%PREFIX%", prefix);
    }
    public static String getPrefix(){

        YamlConfiguration sqlcfg = Config.getConfigFiles("MySQL.yml");
        YamlConfiguration cfg = Config.getConfigFiles("config.yml");
        YamlConfiguration mcfg = Config.getConfigFiles(Locale.getLang());
        String prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(cfg.getString("Prefix")));

        return prefix;
    }
}
