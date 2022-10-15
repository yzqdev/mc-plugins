package fyi.sugar.mobstoeggs.data;

import fyi.sugar.mobstoeggs.Main;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class CreateCapsuleData {
    private static Main plugin = (Main) Main.getPlugin(Main.class);


    public static void getCapsule(Player player, int amount) {
        ItemStack egg = new ItemStack(Material.valueOf(plugin.cm.getSettings().getString("capsule.type")));
        ItemMeta eggmeta = egg.getItemMeta();
        eggmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.cm.getSettings().getString("capsule.name")));
        eggmeta.setLore((List) plugin.cm.getSettings().getStringList("capsule.lore").stream().map(thelore -> ChatColor.translateAlternateColorCodes('&', thelore)).collect(Collectors.toList()));
        egg.setItemMeta(eggmeta);
        egg.setAmount(amount);
        if (player == null) {
            Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | Failed to find a valid player to give a capsule to!");
            return;
        }
        player.getInventory().addItem(new ItemStack[]{egg});
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("given-capsule").replaceAll("%amount%", Integer.toString(amount))));
    }
}


