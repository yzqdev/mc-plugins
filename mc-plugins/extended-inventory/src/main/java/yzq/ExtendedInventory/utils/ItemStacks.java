package yzq.ExtendedInventory.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import yzq.ExtendedInventory.ExtendedInventory;


public enum ItemStacks {
    //这是placeholder
    PLACEHOLDER("InventoryItems.PlaceholderItems", true),
    NEXT_PAGE("InventoryItems.NextPage", true),
    PREVIOUS_PAGE("InventoryItems.PreviousPage", true);

    File FILE =new File(ExtendedInventory.getInstance().getDataFolder() + File.separator + "config.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(FILE);
    public String name;
    public List<String> lore;

    ItemStacks(String path, boolean value) {
        this.name = value ? ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.cfg.getString(path + ".Name").replace("%currentPage%", ""))) : "§r";
        this.lore = value ? new ArrayList<>(Arrays.asList(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.cfg.getString(path + ".Lore"))).split("//"))) : new ArrayList<>();
        this.itemid = Objects.requireNonNull(this.cfg.getString(path + ".ItemID")).split(":");
        this.invslot = value ? this.cfg.getInt(String.valueOf(path) + ".InventorySlot") : 0;
    }

    public String[] itemid;
    public int amount;
    public int invslot;

    public int getInvSlot() {
        return this.invslot - 1;
    }


    public ItemStack getItem(int amount) {
        return getItem(this.itemid, this.name, this.lore, amount);
    }


    public static ItemStack getItem(String[] ID, String NAME, List<String> LORE, int amount) {
        ////int id = Integer.parseInt(ID[0]);
        //int byt = 0;
        //if (ID.length > 1) {
        //    byt = Integer.parseInt(ID[1]);
        //}

        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(String.valueOf(ID[0]))), amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(NAME);

        if (LORE.size() == 1 && "".equals(LORE.get(0))) {
            LORE = new ArrayList<>();
        }
        itemMeta.setLore(LORE);

        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}


