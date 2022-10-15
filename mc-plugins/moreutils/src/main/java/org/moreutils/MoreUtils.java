package org.moreutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.moreutils.commands.AmazeEgg;
import org.moreutils.commands.Hp;
import org.moreutils.commands.LoadConf;
import org.moreutils.eventListeners.EventListener;
import org.moreutils.utils.Config;

import java.io.File;
import java.util.Objects;
import java.util.Properties;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/29 19:45
 * @Modified By:
 */
public class MoreUtils extends JavaPlugin {
    private static MoreUtils INSTANCE;

    public static MoreUtils getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.loadConfig();
        registerCommands();

        Properties version = new Properties();
        try {
            version.load(getResource("version.properties"));
        } catch (Exception ex) {
            getLogger().info("载入失败");
        }


        getLogger().info("Lores " + getDescription().getVersion() + " (Build " + version.getProperty("Build") + ") is enabled!");
        new EventListener(this);
    }

    private void registerCommands() {

        Objects.requireNonNull(this.getCommand("hp")).setExecutor(new Hp(this));
        Objects.requireNonNull(this.getCommand("hp")).setTabCompleter(new Hp(this));
        Objects.requireNonNull(this.getCommand("mu")).setExecutor(new LoadConf(this));
        Objects.requireNonNull(this.getCommand("mu")).setTabCompleter(new LoadConf(this));
        Objects.requireNonNull(this.getCommand("toegg")).setExecutor(new AmazeEgg(this));
        Objects.requireNonNull(this.getCommand("toegg")).setTabCompleter(new AmazeEgg(this));
    }

    private void loadConfigurationFiles() {
    }

    public void createConfig() {
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveDefaultConfig();
            saveResource("test/test.yml", true);
            say(ChatColor.YELLOW + "无法找到config.yml,正在创建");
            /**
             * 读取
             * this.getDataFolder()方法返回插件配置文件夹的File对象
             * File biuConfigFile = new File(this.getDataFolder(), "biu.yml");
             *  对于在插件配置文件夹创建一个新的文件夹存放配置文件
             * File biuConfigFile = new File(this.getDataFolder(), "test/biu.yml");
             * FileConfiguration biuConfig = YamlConfiguration.loadConfiguration(biuConfigFile);
             * biuConfigFile.get.......
             * biuConfigFile.set....... //set完了记得保存!
             * //保存
             * biuConfig.save(biuConfigFile);
             */
        }
        try {

            reloadConfig();
            say(ChatColor.YELLOW + "成功加载config" + getDataFolder());
        } catch (Exception e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            say(ChatColor.RED + "无法读取config");
        }
    }

    public void say(String s) {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(s);
    }
}
