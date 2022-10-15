package org.moreutils.eventListeners

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SpawnEggMeta
import org.moreutils.MoreUtils
import org.moreutils.utils.Config
import org.moreutils.utils.HpUtils
import java.util.*

/**
 * @author yanni
 */
class EventListener(plugin: MoreUtils) : Listener {
    //Register the event listener to begin listening
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        HpUtils.setPlayerLoginHp(event)
        //设置用户默认生命值
    }

    @EventHandler
    fun onPlayerAutoLogin(event: PlayerDeathEvent) {
        val p = event.entity
        p.health = 20.0
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        HpUtils.setPlayerJoinHp(event)
        //设置用户默认生命值
    }

    /**
     * 用户复活触发事件
     *
     * @param playerRespawnEvent 复活事件
     */
    @EventHandler
    fun onPlayerRespawn(playerRespawnEvent: PlayerRespawnEvent) {
        val p = playerRespawnEvent.player
         p.getAttribute(Attribute.GENERIC_MAX_HEALTH )?.baseValue  =
      MoreUtils.get().config.getDouble("player-max-health")
        p.health = MoreUtils.get().config.getDouble("player-max-health")
    }

    @EventHandler
    fun onAttackEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val hurter = event.entity
        val r = Random()
        val result = r.nextInt(100) + 1
        val userSetChance = Config.getConfig("to-egg-chance")!!.toInt()
        if (result <= userSetChance) {
            if (damager.type == EntityType.EGG) {
                if (hurter is LivingEntity && hurter !is Player) {
                    val wor = hurter.getWorld()
                    val loc = hurter.getLocation()
                    wor.dropItem(loc, getSpawnEggFromEntity(hurter.getType()))
                    //这里把被打到的生物传送到虚空
                    hurter.teleport(Location(wor, 0.0, -10.0, 0.0))
                }
            }
        } else {
            MoreUtils.get().logger.info("砸到了" + hurter + hurter.javaClass.name)
        }
    }

    @EventHandler
    fun onHitEntity(event: EntityDamageByEntityEvent) {
        val playerEntity = event.damager
        val hurter = event.entity
        val r = Random()
        val result = r.nextInt(100) + 1
        val userSetChance = Config.getConfig("to-egg-chance")?.toInt()
        if (result <= userSetChance!!) {
            if (playerEntity.type == EntityType.PLAYER) {
                val player = playerEntity as Player
                val playerInventory = player.inventory
                MoreUtils.get().logger.info(playerInventory.itemInMainHand.toString())
                MoreUtils.get().logger.info(ItemStack(Material.EGG).toString())
                if (playerInventory.itemInMainHand.type == Material.EGG) {
                    if (hurter is LivingEntity && hurter !is Player) {
                        val wor = hurter.getWorld()
                        val loc = hurter.getLocation()
                        wor.dropItem(loc, getSpawnEggFromEntity(hurter.getType()))
                        //这里把被打到的生物传送到虚空
                        hurter.teleport(Location(wor, 0.0, -10.0, 0.0))
                        playerInventory.setItemInMainHand(
                            ItemStack(
                                Material.EGG,
                                playerInventory.itemInMainHand.amount - 1
                            )
                        )
                    }
                } else {
                    println("你没有egg")
                }
            }
        } else {
            MoreUtils.get().logger.info("打到了" + hurter + hurter.javaClass.name)
        }
    }

    companion object {
        //@EventHandler
        //public void onInteract(PlayerInteractEvent e) {
        //    Player p = e.getPlayer();
        //    if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
        //        String[] item = MoreUtils.get().getConfig().getString("trash.block").split(":");
        //        int id = Integer.parseInt(item[0]);
        //        int durability = Integer.parseInt(item[1]);
        //        if(p.isSneaking()) {
        //            return;
        //        }
        //        if(e.getClickedBlock().getType() == id) {
        //            if(e.getClickedBlock().getMetadata("durability") == durability) {
        //               ItemStack hand=p.getInventory().getItemInMainHand();
        //                if(!(MoreUtils.get().getConfig().getStringList("trash.blacklist-item").contains(hand.getType()+":"+hand.getDurability())) && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
        //                    e.setCancelled(true);
        //                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR, 1));
        //                    String message = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MoreUtils.get().getConfig().getString("trash.message")));
        //                    p.sendMessage(message);
        //                }
        //            }
        //        }
        //    }
        //}
        /**
         * 砸蛋获取刷怪蛋
         * @author yanni
         * @param type 类型
         * @return ItemStack 返回生成的刷怪蛋
         */
        fun getSpawnEggFromEntity(type: EntityType): ItemStack {
            val item: ItemStack
            MoreUtils.get().getLogger().info(type.toString() + "变成了蛋")
            if ("PIG_ZOMBIE" == type.toString()) {
                item = ItemStack(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG)
            } else if ("IRON_GOLEM" == type.toString()) {
                item = ItemStack(Material.IRON_BLOCK, 4)
                return item
            } else {
                item = ItemStack( Material.getMaterial(type.toString() + "_SPAWN_EGG")!!)
            }
            val meta = item.itemMeta as SpawnEggMeta?
            //meta.setSpawnedType(type);
            item.itemMeta = meta
            return item
        }
    }
}