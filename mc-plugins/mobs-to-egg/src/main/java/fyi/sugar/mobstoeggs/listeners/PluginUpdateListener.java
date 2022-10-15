package fyi.sugar.mobstoeggs.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public class PluginUpdateListener {
    private final JavaPlugin plugin;
    private final String PluginVersion;
    private String SpigotVersion;
    private static final long CHECK_INTERVAL = 360000L;

    public PluginUpdateListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.PluginVersion = plugin.getDescription().getVersion();
    }

    public void checkForUpdate() {
        (new BukkitRunnable() {
            @Override
            public void run() {
                if (!PluginUpdateListener.this.plugin.getConfig().getBoolean("update-nofity")) {
                    return;
                }
                Bukkit.getScheduler().runTaskAsynchronously((Plugin) PluginUpdateListener.this.plugin, () -> {
                    try {
                        HttpsURLConnection connection = (HttpsURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=69425")).openConnection();
                        connection.setRequestMethod("GET");
                        PluginUpdateListener.this.SpigotVersion = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();
                    } catch (IOException e) {
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMobsToEggs | Failed to check updates! Please report this to the Sugarfyi Discord."));

                        e.printStackTrace();

                        cancel();

                        return;
                    }

                    if (PluginUpdateListener.this.PluginVersion.equalsIgnoreCase(PluginUpdateListener.this.SpigotVersion)) {
                        return;
                    }

                    System.out.println("----- MTE VERSION CONTROL -----");

                    System.out.println("A new version of MobsToEggs is available for download at http://mte.sugar.fyi!");

                    System.out.println("Current: " + PluginUpdateListener.this.PluginVersion + " | Latest: " + PluginUpdateListener.this.SpigotVersion);

                    System.out.println("----- MTE VERSION CONTROL -----");
                    Bukkit.getScheduler().runTask(PluginUpdateListener.this.plugin, () -> System.out.println("哈哈哈"));

                    cancel();
                });
            }
        }).runTaskTimer((Plugin) this.plugin, 0L, 360000L);
    }
}


