package fyi.sugar.mobstoeggs.events;

import fyi.sugar.mobstoeggs.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class SpawnerInteractEvent implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpawnerClickWithEgg(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();


        if (item != null && item.getType().name().contains("_SPAWN_EGG")) {

            String mobtype = item.getType().name().replace("_SPAWN_EGG", "").toUpperCase();
            mobtype = mobtype.replace("ZOMBIE_PIGMAN", "PIG_ZOMBIE");
            mobtype = mobtype.replace("MOOSHROOM", "MUSHROOM_COW");

            if (item != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                    block.getType().equals(Material.SPAWNER)) {

                if (!this.plugin.cm.getSettings().getBoolean("allow-spawner-changing") &&
                        this.plugin.cm.getSettings().getBoolean("use-permissions") == true &&
                        !player.hasPermission("mobstoeggs.spawner." + mobtype.toLowerCase())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + " " + ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")));
                    event.setCancelled(true);


                    return;
                }

                if (!this.plugin.cm.getSettings().getBoolean("use-global-values") &&
                        !this.plugin.cm.getMobs().getBoolean(mobtype.replace("_", " ") + ".change-spawner")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + " " + ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")));
                    event.setCancelled(true);
                    return;
                }
                return;
            }
        }
    }
}


