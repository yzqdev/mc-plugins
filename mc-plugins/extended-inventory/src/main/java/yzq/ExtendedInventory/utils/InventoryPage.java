package yzq.ExtendedInventory.utils;

import yzq.ExtendedInventory.ExtendedInventory;
import yzq.ExtendedInventory.Listeners.JoinListener;
import yzq.ExtendedInventory.SQL.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class InventoryPage {
    Player player;
    int site;
    ExtendedInventory plugin;
    FileConfiguration cfg;

    public InventoryPage(ExtendedInventory plugin, Player player, int site) {
        this.player = player;
        this.site = site;
        this.plugin = plugin;
    }

    public void open() {
        ItemStack[] cc = getContents(this.player, this.site);
        ItemStack[] armor = this.player.getInventory().getArmorContents();

        HashMap<Integer, ItemStack> HASH = new HashMap<>();
        for (int i = 0; i <= 8; i++) {
            if (this.player.getInventory().getItem(i) != null) {
                HASH.put(i, this.player.getInventory().getItem(i));
            }
        }
        this.player.getInventory().clear();
        if (cc != null) {
            this.player.getInventory().setContents(cc);
        }
        for (int i = 0; i <= 8; i++) {
            this.player.getInventory().setItem(i, HASH.get(i));
        }
        this.player.getInventory().setItem(ItemStacks.NEXT_PAGE.getInvSlot(), ItemStacks.NEXT_PAGE.getItem(1));
        this.player.getInventory().setItem(ItemStacks.PREVIOUS_PAGE.getInvSlot(), ItemStacks.PREVIOUS_PAGE.getItem(1));
        for (int b : JoinListener.slots) {
            this.player.getInventory().setItem(b, ItemStacks.PLACEHOLDER.getItem(this.site));
        }
        this.player.getInventory().setArmorContents(armor);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> InventoryPage.this.player.updateInventory(), 5L);
    }

    public static int getMaxPages(Player player) {
        int pages = 1;
        if (!ExtendedInventory.sql) {
            FileConfiguration Dcfg = Config.getDataFiles("/Util", "Data.dat");
            if (Dcfg.getString("Data." + player.getUniqueId().toString() + ".Pages") != null) {
                pages = Objects.requireNonNull(Dcfg.getConfigurationSection("Data." + player.getUniqueId().toString() + ".Pages")).getKeys(false).size();
            }
            return pages;
        }
        ResultSet rs = null;
        PreparedStatement st = null;

        try {
            st = MySQL.getConnection().prepareStatement("SELECT * FROM EXTENDED_INVENTORY WHERE UUID= ?");
            st.setString(1, player.getUniqueId().toString());
            rs = st.executeQuery();
            if (rs.next() && rs.getString("UUID") != null) {
                String[] pg = rs.getString("INVENTORYS").split("@");
                pages = pg.length;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return pages;
    }

    public static ItemStack[] getContents(Player player, int page) {
        ItemStack[] cc = new ItemStack[(player.getInventory().getContents()).length];
        if (!ExtendedInventory.sql) {
            FileConfiguration Dcfg =Config.getDataFiles("/Util", "Data.dat");

            if (Dcfg.getString("Data." + player.getUniqueId().toString() + ".Pages." + page) != null) {
                String DATA = Dcfg.getString("Data." + player.getUniqueId().toString() + ".Pages." + page);
                cc = SaveInventory.itemStackArrayFromBase64(DATA);
            }
            return cc;
        }
        ResultSet rs = null;
        PreparedStatement st = null;

        try {
            st = MySQL.getConnection().prepareStatement("SELECT * FROM EXTENDED_INVENTORY WHERE UUID= ?");
            st.setString(1, player.getUniqueId().toString());
            rs = st.executeQuery();
            if (rs.next() && rs.getString("UUID") != null) {
                String[] pages = rs.getString("INVENTORYS").split("@");
                if (pages.length > page) {
                    String DATA = pages[page];
                    cc = SaveInventory.itemStackArrayFromBase64(DATA);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cc;
    }
}

