package yzq.ExtendedInventory.Listeners;

import yzq.ExtendedInventory.ExtendedInventory;
import yzq.ExtendedInventory.SQL.MySQL;
import yzq.ExtendedInventory.utils.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class InventoryClickListener implements Listener {
    private ExtendedInventory plugin;
    public static HashMap<String, Integer> PAGES = new HashMap<>();

    private static Integer normalPages = 5;
    private static Integer maxPages = 8;

    public InventoryClickListener(ExtendedInventory inventory) {
        this.plugin = inventory;

        FileConfiguration cfg = Config.getConfigFiles("config.yml");
        normalPages = cfg.getInt("Options.SiteLimit");
        maxPages = cfg.getInt("Options.PremiumSiteLimit");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().equals(ItemStacks.NEXT_PAGE.getItem(1))) {
                    e.setCancelled(true);
                    int currentPages = PAGES.containsKey(p.getUniqueId().toString()) ? PAGES.get(p.getUniqueId().toString()) : 1;
                    if ((!p.hasPermission("MorePages") && currentPages == normalPages.intValue()) || currentPages == maxPages.intValue()) {
                        p.sendMessage(StringGet.getString("Messages.NoMorePages"));
                        return;
                    }

                    try {
                        saveData(p, currentPages, null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    (new InventoryPage(this.plugin, p, currentPages + 1)).open();
                    PAGES.put(p.getUniqueId().toString(), currentPages + 1);
                    p.sendMessage(StringGet.getString("Messages.SwitchPage").replace("%PAGE%", String.valueOf(currentPages + 1)));
                    return;
                }
                if (e.getCurrentItem().equals(ItemStacks.PREVIOUS_PAGE.getItem(1))) {
                    e.setCancelled(true);
                    boolean flag = !PAGES.containsKey(p.getUniqueId().toString()) || (PAGES.containsKey(p.getUniqueId().toString()) && PAGES.get(p.getUniqueId().toString()) == 1);
                    if (flag) {
                        p.sendMessage(StringGet.getString("Messages.AlreadyOnFirstPage"));
                        return;
                    }
                    int currentPages = PAGES.get(p.getUniqueId().toString());
                    saveData(p, currentPages, null);
                    (new InventoryPage(this.plugin, p, currentPages - 1)).open();
                    PAGES.put(p.getUniqueId().toString(), currentPages - 1);
                    p.sendMessage(StringGet.getString("Messages.SwitchPage").replace("%PAGE%", String.valueOf(currentPages - 1)));
                    //Bukkit.getConsoleSender().sendMessage("当前页"+currentPages);
                    //Config.getConfigFiles("config.yml").getString("InventoryItems.PlaceholderItems.Name").replace("%currentPage%", String.valueOf(currentPages));
                    return;
                }
                if (e.getCurrentItem().equals(ItemStacks.PLACEHOLDER.getItem(getPage(p)))) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent e) {
        saveData((Player) e.getPlayer(), getPage((Player) e.getPlayer()), null);
    }


    public static Integer getPage(Player player) {
        return PAGES.getOrDefault(player.getUniqueId().toString(), 1);
    }


    public static void saveData(Player player, int page, ItemStack[] data) {
        Inventory dInv = Bukkit.createInventory(null, 36);
        for (int i = 9; i < player.getInventory().getSize(); i++) {
            Bukkit.getConsoleSender().sendMessage("i is"+i);
            if (i != ItemStacks.NEXT_PAGE.getInvSlot() && i != ItemStacks.PREVIOUS_PAGE.getInvSlot() && !JoinListener.slots.contains(i) &&
                    player.getInventory().getItem(i) != null) {
                try {


                    dInv.setItem(i, player.getInventory().getItem(i));
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("error is");
                    e.printStackTrace();
                }
            }
        }
        ItemStack[] cc = (data == null) ? dInv.getContents() : data;
        String serialized = SaveInventory.itemStackArrayToBase64(cc);
        if (ExtendedInventory.sql) {
            ResultSet rs = null;
            Statement insert = null;
            PreparedStatement selectSt = null;
            PreparedStatement updateSt = null;

            try {
                selectSt = MySQL.getConnection().prepareStatement("SELECT * FROM EXTENDED_INVENTORY WHERE UUID= ?");
                updateSt = MySQL.getConnection().prepareStatement("UPDATE EXTENDED_INVENTORY SET INVENTORYS= ? WHERE UUID= ?");
                selectSt.setString(1, player.getUniqueId().toString());
                rs = selectSt.executeQuery();
                if (rs.next() && rs.getString("UUID") != null) {
                    String[] oldDATA = new String[maxPages + 1];
                    String[] hasDATA = rs.getString("INVENTORYS").split("@");
                    for (int i = 0; i < hasDATA.length; i++) {
                        oldDATA[i] = hasDATA[i];
                    }
                    oldDATA[page] = serialized;
                    String newDATA = "";
                    for (int i = 0; i < oldDATA.length - 1; i++) {
                        newDATA = String.valueOf(newDATA) + oldDATA[i] + "@";
                    }
                    if (newDATA.endsWith("@")) {
                        newDATA = newDATA.substring(0, newDATA.length() - 1);
                    }
                    updateSt.setString(1, newDATA);
                    updateSt.setString(2, player.getUniqueId().toString());
                    updateSt.executeUpdate();
                    return;
                }
                insert = MySQL.getConnection().createStatement();
                String DATA = "";
                for (int i = 1; i <= page; i++) {
                    DATA = String.valueOf(DATA) + "@";
                }
                DATA = String.valueOf(DATA) + serialized;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (updateSt != null) {
                        updateSt.close();
                    }
                    if (selectSt != null) {
                        selectSt.close();
                    }
                    if (insert != null) {
                        insert.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            try {
                if (updateSt != null) {
                    updateSt.close();
                }
                if (selectSt != null) {
                    selectSt.close();
                }
                if (insert != null) {
                    insert.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return;
        }
        if (cc.length > 0) {
            Config.saveDataFiles(Config.getFile("/Util", "Data.dat"), "Data." + player.getUniqueId().toString() + ".Pages." + page, serialized);
        }
    }
}

