package mainDurabilityDisplay;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class DurabilityDisplay
        extends JavaPlugin
        implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, (Plugin) this);
    }


    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand() != null) {
            updateItem(e.getPlayer().getItemInHand(), e.getPlayer());
        }
    }


    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {

            Player p = (Player) e.getEntity();

            if (p.getInventory().getHelmet() != null) {
                updateItem(p.getInventory().getHelmet(), p);
            }
            if (p.getInventory().getChestplate() != null) {
                updateItem(p.getInventory().getChestplate(), p);
            }
            if (p.getInventory().getLeggings() != null) {
                updateItem(p.getInventory().getLeggings(), p);
            }
            if (p.getInventory().getBoots() != null) {
                updateItem(p.getInventory().getBoots(), p);
            }
        }
    }

    //
    //@EventHandler
    //public void PlayerPickupItemEvent(EntityPickupItemEvent e) {
    //    updateItem(e.getItem().getItemStack(), e.getEntity());
    //}


    @EventHandler
    public void CraftItemEvent(CraftItemEvent e) {
        e.setCurrentItem(updateItemNoPlayer(e.getCurrentItem()));
    }


    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent e) {
        for (ItemStack temp : e.getDrops()) {
            temp = updateItemNoPlayer(temp);
        }
    }


    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        ItemStack temp = e.getItemDrop().getItemStack();
        temp = updateItemNoPlayer(e.getItemDrop().getItemStack());
    }

    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent e) {
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = e.getInventory().getContents()).length, b = 0; b < i; ) {
            ItemStack s = arrayOfItemStack[b];

            s = updateItemNoPlayer(s);
            b++;
        }

    }

    public void updateItem(ItemStack item, Player p) {
        Material mat = item.getType();
        int maxDurability = mat.getMaxDurability();

        if (maxDurability > 0) {

            int duraSections = maxDurability / 5;
            ItemMeta temp = item.getItemMeta();

            if (maxDurability - item.getDurability() >= 4 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.DARK_GREEN + "Pristine" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_GREEN + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.DARK_GREEN + "Pristine" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_GREEN + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 4 * duraSections - 1 && maxDurability - item.getDurability() >= 3 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.YELLOW + "Worn" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.YELLOW + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.YELLOW + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.YELLOW + "Worn" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.YELLOW + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.YELLOW + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 3 * duraSections - 1 && maxDurability - item.getDurability() >= 2 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.GOLD + "Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.GOLD + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.GOLD + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.GOLD + "Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.GOLD + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.GOLD + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 2 * duraSections - 1 && maxDurability - item.getDurability() >= 1 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.RED + "Badly Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.RED + "Badly Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 1 * duraSections - 1 && maxDurability - item.getDurability() >= 0) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {

                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.DARK_RED + "Ruined" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {

                    newLore.add(ChatColor.RESET + "[" + ChatColor.DARK_RED + "Ruined" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }
            }
            p.getItemInHand().setItemMeta(temp);
        }
    }


    public ItemStack updateItemNoPlayer(ItemStack item) {
        ItemStack tempStack = item;
        Material mat = item.getType();
        int maxDurability = mat.getMaxDurability();

        if (maxDurability > 0) {

            int duraSections = maxDurability / 5;
            ItemMeta temp = item.getItemMeta();

            if (maxDurability - item.getDurability() >= 4 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.DARK_GREEN + "Pristine" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_GREEN + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.DARK_GREEN + "Pristine" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_GREEN + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 4 * duraSections - 1 && maxDurability - item.getDurability() >= 3 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.YELLOW + "Worn" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.YELLOW + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.YELLOW + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.YELLOW + "Worn" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.YELLOW + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.YELLOW + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 3 * duraSections - 1 && maxDurability - item.getDurability() >= 2 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.GOLD + "Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.GOLD + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.GOLD + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.GOLD + "Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.GOLD + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.GOLD + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 2 * duraSections - 1 && maxDurability - item.getDurability() >= 1 * duraSections) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {
                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.RED + "Badly Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {
                    newLore.add(ChatColor.RESET + "[" + ChatColor.RED + "Badly Damaged" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }

            } else if (maxDurability - item.getDurability() <= 1 * duraSections - 1 && maxDurability - item.getDurability() >= 0) {

                ArrayList<String> newLore = new ArrayList<>();

                if (temp.hasLore()) {

                    List<String> lore = temp.getLore();
                    lore = temp.getLore();
                    lore.set(temp.getLore().size() - 1, ChatColor.RESET + "[" + ChatColor.DARK_RED + "Ruined" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(lore);
                } else {

                    newLore.add(ChatColor.RESET + "[" + ChatColor.DARK_RED + "Ruined" + ChatColor.RESET + "]" + ChatColor.WHITE + "[" + ChatColor.DARK_RED + (maxDurability - item.getDurability()) + ChatColor.WHITE + "/" + ChatColor.DARK_RED + maxDurability + ChatColor.WHITE + "]");
                    temp.setLore(newLore);
                }
            }
            item.setItemMeta(temp);
        }
        return tempStack;
    }
}
