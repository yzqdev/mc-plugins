package org.lores;

import org.bukkit.plugin.java.JavaPlugin;
import org.lores.commands.Lore;

import java.util.Objects;
import java.util.Properties;

/**
 * @author yanni
 */
public class Lores extends JavaPlugin {


    @Override
    public void onEnable() {
        registerCommands();
        Properties version = new Properties();
        try {
            version.load(getResource("version.properties"));
        } catch (Exception e) {
            getLogger().info("error find version");
        }
        getLogger().info("Lores " + getDescription().getVersion() + " (Build " + version.getProperty("Build") + ") is enabled!");
    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("lore")).setExecutor(new Lore(this));
        Objects.requireNonNull(this.getCommand("lore")).setTabCompleter(new Lore(this));
    }
}