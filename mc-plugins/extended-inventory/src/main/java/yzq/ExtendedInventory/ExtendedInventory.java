package yzq.ExtendedInventory;

import org.bukkit.configuration.file.YamlConfiguration;
import yzq.ExtendedInventory.Commands.CommandEI;
import yzq.ExtendedInventory.Listeners.DeathListener;
import yzq.ExtendedInventory.Listeners.InventoryClickListener;
import yzq.ExtendedInventory.Listeners.JoinListener;
import yzq.ExtendedInventory.Listeners.QuitListener;
import yzq.ExtendedInventory.SQL.MySQL;
import yzq.ExtendedInventory.utils.Config;
import yzq.ExtendedInventory.utils.Locale;
import yzq.ExtendedInventory.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public class ExtendedInventory extends JavaPlugin {

    public String prefix;
    public static boolean sql = false;
    private static ExtendedInventory INSTANCE;
    public static Config config;
    public static Locale locale;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getConsoleSender().sendMessage("ExtendedInventory"+Locale.getLangFiles().getString("hasEnabled"));
        Config.loadDefaultConfig();
        locale = new Locale(this);
        Config.loadResourceFile(this, "MySQL.yml");
        YamlConfiguration cfg = Config.getConfigFiles("config.yml");

        YamlConfiguration sqlcfg = Config.getConfigFiles("MySQL.yml");
      YamlConfiguration mcfg = Config.getConfigFiles(Locale.getLang());
//加载其他配置文件

        if (cfg == null || cfg.getString("Prefix") == null) {
            getServer().reload();
            return;
        }
        this.prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(cfg.getString("Prefix")));
        if (sqlcfg != null && sqlcfg.getBoolean("MySQL.Enable")) {
            Config.readSQLData();
            MySQL.perform();
            if (!MySQL.isConnected()) {
                Bukkit.getConsoleSender().sendMessage(this.prefix + " §cCould not connect to MySQL!");
                Bukkit.getConsoleSender().sendMessage(this.prefix + " §cPlease check your login data and try again!");
                Bukkit.getServer().getPluginManager().disablePlugin(this);
                return;
            }
            sql = true;
        }
        //注册命令和监听
        registerClasses();
Bukkit.getConsoleSender().sendMessage(ExtendedInventory.getInstance().getDataFolder() + "/lang");
Bukkit.getConsoleSender().sendMessage("到达了这里");
        if (cfg.getBoolean("CheckForUpdates") && !UpdateChecker.check()) {
            Bukkit.getConsoleSender().sendMessage(this.prefix + " §cA new update is available!");
            Bukkit.getConsoleSender().sendMessage(this.prefix + " §cPlease update your plugin!");
            Bukkit.getConsoleSender().sendMessage(this.prefix + " §cYou will get no support for this version!!");
        }
        Bukkit.getConsoleSender().sendMessage(this.prefix + " §aThe plugin was successfully loaded!");
    }

    public static ExtendedInventory getInstance() {
        return INSTANCE;
    }


    private void registerClasses() {
        Objects.requireNonNull(this.getCommand("ei")).setExecutor(new CommandEI(this));
        Objects.requireNonNull(this.getCommand("ei")).setTabCompleter(new CommandEI(this));

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
    }


    @Override
    public void onDisable() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            InventoryClickListener.saveData(all, InventoryClickListener.getPage(all), null);
        }
    }



}


