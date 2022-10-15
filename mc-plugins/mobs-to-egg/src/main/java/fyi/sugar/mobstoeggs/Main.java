package fyi.sugar.mobstoeggs;

import fyi.sugar.mobstoeggs.api.MTEAPI;
import fyi.sugar.mobstoeggs.events.CapsuleHitEvent;
import fyi.sugar.mobstoeggs.events.SpawnerInteractEvent;
import fyi.sugar.mobstoeggs.listeners.PluginUpdateListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public MTEAPI mteAPI;
    String mtev = getDescription().getVersion();
    public fyi.sugar.mobstoeggs.Metrics metrics;
    public static Main plugin;
    public Economy econ;
    public fyi.sugar.mobstoeggs.ConfigManager cm;

    @Override
    public void onEnable() {
        this.mteAPI = new MTEAPI();
        plugin = this;
        fyi.sugar.mobstoeggs.ConfigManager.setup();
        this.cm = new fyi.sugar.mobstoeggs.ConfigManager();
        String costType = this.cm.getSettings().getString("cost-type");
        (new PluginUpdateListener(this)).checkForUpdate();
        Bukkit.getPluginManager().registerEvents((Listener) new CapsuleHitEvent(), (Plugin) this);
        Bukkit.getPluginManager().registerEvents((Listener) new SpawnerInteractEvent(), (Plugin) this);
        Bukkit.getPluginCommand("mte").setExecutor(new fyi.sugar.mobstoeggs.CommandManager());
        this.metrics = new fyi.sugar.mobstoeggs.Metrics((Plugin) this);
        String lang = this.cm.getSettings().getString("language");
        System.out.println("Loading MobsToEggs v" + this.mtev + "!");
        System.out.println("Loading language: " + lang);
        System.out.println("Loading cost type: " + costType.toLowerCase());

        if (costType.equals("ECONOMY")) {
            if (getServer().getPluginManager().getPlugin("Vault") == null) {
                System.out.println("MobsToEggs v" + this.mtev + " - Whoops! You cannot use economy features without installing Vault, cost-type has been set to NONE for safety!");
                this.cm.getSettings().set("cost-type", "NONE");
                fyi.sugar.mobstoeggs.ConfigManager.saveSettings();
                return;
            }
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                this.econ = (Economy) rsp.getProvider();
                return;
            }
        }
    }


    @Override
    public void onDisable() {
    }


    public Economy getEconomy() {
        return this.econ;
    }
}


