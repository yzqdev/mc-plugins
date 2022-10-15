package org.moreutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.moreutils.MoreUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author   Yangzhengqian
 * @date  2019/12/30 11:11
 * @Modified By:
 */
public class HpUtils {

    public static void setPlayerJoinHp(PlayerJoinEvent event) {
        try {
            Player p = event.getPlayer();
            PlayerInventory playerInventory = p.getInventory();
            ItemStack itemStack = new ItemStack(Material.IRON_INGOT, 1);
            if (!playerInventory.contains(Material.IRON_INGOT)) {
                playerInventory.addItem(itemStack);
                //如果用户进入游戏就给他64钻石块
            }
            Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(MoreUtils.getInstance().getConfig().getInt("player-max-health"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setDefaultHp(@NotNull String hp) {
        MoreUtils.getInstance().getLogger().info("血量是" + Double.parseDouble(hp));

        for (@NotNull Player p : Bukkit.getOnlinePlayers()) {

            Objects.requireNonNull(p.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(Double.parseDouble(hp));
        }
    }

    public static void setPlayerLoginHp(PlayerLoginEvent event) {
        try {
            Player p = event.getPlayer();

            Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(MoreUtils.getInstance().getConfig().getInt("player-max-health"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
