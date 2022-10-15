package org.moreutils.utils

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.inventory.ItemStack
import org.moreutils.MoreUtils
import java.util.*

/**
 * @author   Yangzhengqian
 * @date  2019/12/30 11:11
 * @Modified By:
 */
object HpUtils {
    fun setPlayerJoinHp(event: PlayerJoinEvent) {
        try {
            val p = event.player
            val playerInventory = p.inventory
            val itemStack = ItemStack(Material.IRON_INGOT, 1)
            if (!playerInventory.contains(Material.IRON_INGOT)) {
                playerInventory.addItem(itemStack)
                //如果用户进入游戏就给他64钻石块
            }
          p.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue =
                MoreUtils.get().config.getDouble("player-max-health")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setDefaultHp(hp: String) {
        MoreUtils.get().getLogger().info("血量是" + hp.toDouble())
        for (p in Bukkit.getOnlinePlayers()) {
             p.player!!.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = hp.toDouble()
        }
    }

    fun setPlayerLoginHp(event: PlayerLoginEvent) {
        try {
            val p = event.player
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue =
                MoreUtils.get().config.getDouble("player-max-health")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}