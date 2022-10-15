/*
 * Copyright (C) 2019 Bukkit Commons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.colormotd;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.*;
import org.colormotd.commands.CommandColor;
import org.colormotd.utils.Config;
import org.colormotd.utils.ConfigManager;
import org.colormotd.utils.FaviconList;
import org.colormotd.utils.Firewall;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.colormotd.hook.HookPlaceholder;
import org.colormotd.listeners.LoginListener;
import org.colormotd.listeners.MotdListener;

import java.util.Objects;

/**
 * @author yanni
 */
@Getter
public final class ColorMotd extends JavaPlugin {
    private ConfigManager configManager;
    private ProtocolManager protocolManager;
    private HookPlaceholder hookPlaceholder;
    public static boolean usePlaceHolderAPI;
    private Firewall firewall;
    final String prefix = ChatColor.AQUA + "[" + ChatColor.GOLD + "ColorMOTD-Bukkit" + ChatColor.AQUA + "] " + ChatColor.GREEN;

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        if (!pm.isPluginEnabled("ProtocolLib") ||
                (protocolManager = ProtocolLibrary.getProtocolManager()) == null || protocolManager.isClosed()) {
            getLogger().warning("Unable to load ProtocolLib!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().info("Loaded " + ProtocolLibrary.getPlugin().getDescription().getFullName());

        this.configManager = new ConfigManager(getDataFolder().toPath());
        this.configManager.saveDefaultResources();
        if(!getDataFolder().toPath().resolve("config.json").toFile().exists()) {
            saveResource("config.json", false);
            saveResource("config.example.json", false);
        }
        this.configManager.loadConfig();
        this.configManager.loadFavicons();
        firewall = new Firewall(config());
        hookPlaceholder = new HookPlaceholder(config());
registerClasses();
        this.protocolManager.addPacketListener(new MotdListener(this));
        pm.registerEvents(new LoginListener(this), this);
        if (Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null && config().isUsePlaceHolderAPI()) {
            usePlaceHolderAPI = true;
        }
        MetricsLite metrics = new MetricsLite(this);
    }

    @Override
    public void onDisable() {
        if (protocolManager != null) {
            protocolManager.removePacketListeners(this);
        }
    }
    private void registerClasses() {
        Objects.requireNonNull(this.getCommand("colormotd")).setExecutor(new CommandColor(this));
        Objects.requireNonNull(this.getCommand("colormotd")).setTabCompleter(new CommandColor(this));


    }
    public ConfigManager configManager() {
        return configManager;
    }

    public Config config() {
        return configManager().getConfig();
    }

    public FaviconList favicons() {
        return configManager().getFavicons();
    }

}
