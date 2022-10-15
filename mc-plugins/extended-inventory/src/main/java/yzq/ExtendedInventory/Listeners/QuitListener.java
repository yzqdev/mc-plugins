package yzq.ExtendedInventory.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener
        implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        InventoryClickListener.saveData(p, InventoryClickListener.getPage(p), null);
        if (InventoryClickListener.PAGES.containsKey(p.getUniqueId().toString())) {
            InventoryClickListener.PAGES.remove(p.getUniqueId().toString());
        }
    }
}


