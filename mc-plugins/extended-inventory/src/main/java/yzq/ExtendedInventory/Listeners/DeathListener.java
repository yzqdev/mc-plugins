 package yzq.ExtendedInventory.Listeners;

 import yzq.ExtendedInventory.ExtendedInventory;
 import yzq.ExtendedInventory.utils.Config;
 import yzq.ExtendedInventory.utils.InventoryPage;
 import yzq.ExtendedInventory.utils.ItemStacks;
 import org.bukkit.configuration.file.FileConfiguration;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.Listener;
 import org.bukkit.event.entity.PlayerDeathEvent;
 import org.bukkit.event.player.PlayerRespawnEvent;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.plugin.Plugin;
 import org.bukkit.scheduler.BukkitRunnable;


 public class DeathListener
   implements Listener
 {
   private ExtendedInventory plugin;
   private static FileConfiguration cfg = Config.getConfigFiles("config.yml");


   public DeathListener(ExtendedInventory inventory) { this.plugin = inventory; }


   @EventHandler(priority = EventPriority.HIGHEST)
   public void onDeath(PlayerDeathEvent e) {
       e.getEntity();
       Player p = e.getEntity();

       if (cfg.getBoolean("Options.KeepInventory")) {
         e.setKeepInventory(true);
         e.getDrops().clear();
         return;
       }
       if (e.getDrops().contains(ItemStacks.NEXT_PAGE.getItem(1))) {
         e.getDrops().remove(ItemStacks.NEXT_PAGE.getItem(1));
       }
       if (e.getDrops().contains(ItemStacks.PREVIOUS_PAGE.getItem(1))) {
         e.getDrops().remove(ItemStacks.PREVIOUS_PAGE.getItem(1));
       }
       if (e.getDrops().contains(ItemStacks.PLACEHOLDER.getItem(InventoryClickListener.getPage(p)))) {
         e.getDrops().remove(ItemStacks.PLACEHOLDER.getItem(InventoryClickListener.getPage(p)));
       }

       if (cfg.getBoolean("Options.DropAllPages")) {
         for (int i = 1; i <= InventoryPage.getMaxPages(p); i++) {
           byte b; int j; ItemStack[] arrayOfItemStack; for (j = (arrayOfItemStack = InventoryPage.getContents(p, i)).length, b = 0; b < j; ) { ItemStack item = arrayOfItemStack[b];
             if (item != null)
 {
                        e.getDrops().add(item);
                    }
             b++; }

           ItemStack[] content = new ItemStack[(p.getInventory().getContents()).length];
           InventoryClickListener.saveData(p, i, content);
         }
       }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   public void onRespawn(PlayerRespawnEvent e) {
     final Player p = e.getPlayer();
     (new BukkitRunnable()
       {
         @Override
         public void run()
         {
           (new InventoryPage(DeathListener.this.plugin, p, 1)).open();
         }
       }).runTaskLater((Plugin)this.plugin, 5L);
   }
 }

