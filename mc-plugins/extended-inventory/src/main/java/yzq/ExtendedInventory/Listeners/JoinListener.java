package yzq.ExtendedInventory.Listeners;

import yzq.ExtendedInventory.ExtendedInventory;
import yzq.ExtendedInventory.utils.Config;
import yzq.ExtendedInventory.utils.InventoryPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class JoinListener implements Listener {
    private ExtendedInventory plugin;
    public static List<Integer> slots = Collections.synchronizedList(new ArrayList<>());

    public JoinListener(ExtendedInventory inventory) {
        this.plugin = inventory;

        FileConfiguration cfg = Config.getConfigFiles("config.yml");
        for (String sSlots : cfg.getStringList("InventoryItems.PlaceholderItems.InventorySlots")) {
            if (isDigit(sSlots)) {
                slots.add(Integer.parseInt(sSlots) - 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        (new BukkitRunnable() {
            @Override
            public void run() {
                (new InventoryPage(JoinListener.this.plugin, p, InventoryClickListener.getPage(p))).open();
            }
        }).runTaskLater(this.plugin, 5L);
    }

    private boolean isDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isAlphabetic(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}


