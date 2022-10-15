package org.moreutils.eventListeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.moreutils.MoreUtils;
import org.moreutils.utils.Config;
import org.moreutils.utils.HpUtils;

import java.util.Objects;
import java.util.Random;

/**
 * @author yanni
 */
public class EventListener implements Listener {
    //Register the event listener to begin listening
    public EventListener(MoreUtils plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        HpUtils.setPlayerLoginHp(event);
        //设置用户默认生命值
    }

    @EventHandler
    public void onPlayerAutoLogin(PlayerDeathEvent event) {
        Player p= event.getEntity();
        p.setHealth(20);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        HpUtils.setPlayerJoinHp(event);
        //设置用户默认生命值
    }

    /**
     * 用户复活触发事件
     *
     * @param playerRespawnEvent 复活事件
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent playerRespawnEvent) {
        Player p = playerRespawnEvent.getPlayer();
        Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(MoreUtils.getInstance().getConfig().getInt("player-max-health"));
        p.setHealth(MoreUtils.getInstance().getConfig().getInt("player-max-health"));
    }
    //@EventHandler
    //public void onInteract(PlayerInteractEvent e) {
    //    Player p = e.getPlayer();
    //    if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
    //        String[] item = MoreUtils.getInstance().getConfig().getString("trash.block").split(":");
    //        int id = Integer.parseInt(item[0]);
    //        int durability = Integer.parseInt(item[1]);
    //        if(p.isSneaking()) {
    //            return;
    //        }
    //        if(e.getClickedBlock().getType() == id) {
    //            if(e.getClickedBlock().getMetadata("durability") == durability) {
    //               ItemStack hand=p.getInventory().getItemInMainHand();
    //                if(!(MoreUtils.getInstance().getConfig().getStringList("trash.blacklist-item").contains(hand.getType()+":"+hand.getDurability())) && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
    //                    e.setCancelled(true);
    //                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR, 1));
    //                    String message = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MoreUtils.getInstance().getConfig().getString("trash.message")));
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

    public static ItemStack getSpawnEggFromEntity(EntityType type) {
        ItemStack item;
        MoreUtils.getInstance().getLogger().info(type + "变成了蛋");

        if ("PIG_ZOMBIE".equals(type.toString())) {
            item = new ItemStack(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG);
        } else if ("IRON_GOLEM".equals(type.toString())) {

            item = new ItemStack(Material.IRON_BLOCK, 4);
            return item;
        } else {
            item = new ItemStack(Objects.requireNonNull(Material.getMaterial(type + "_SPAWN_EGG")));
        }


        SpawnEggMeta meta = (SpawnEggMeta) item.getItemMeta();
        //meta.setSpawnedType(type);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onAttackEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity hurter = event.getEntity();
        Random r = new Random();
        int result = r.nextInt(100) + 1;
        int userSetChance = Integer.parseInt(Config.getConfig("to-egg-chance"));
        if (result <= userSetChance) {
            if (damager.getType().equals(EntityType.EGG)) {

                if (hurter instanceof LivingEntity && !(hurter instanceof Player)) {
                    World wor = hurter.getWorld();
                    Location loc = hurter.getLocation();
                    wor.dropItem(loc, getSpawnEggFromEntity(hurter.getType()));
                    //这里把被打到的生物传送到虚空
                    hurter.teleport(new Location(wor, 0, -10, 0));
                }
            }
        } else {
            MoreUtils.getInstance().getLogger().info("砸到了" + hurter + hurter.getClass().getName());
        }
    }
    @EventHandler
    public void onHitEntity(EntityDamageByEntityEvent event){
        Entity playerEntity = event.getDamager();
        Entity hurter = event.getEntity();
        Random r = new Random();
        int result = r.nextInt(100) + 1;
        int userSetChance = Integer.parseInt(Config.getConfig("to-egg-chance"));
        if (result <= userSetChance) {
            if (playerEntity.getType().equals(EntityType.PLAYER)) {
                Player player=(Player) playerEntity;
               PlayerInventory playerInventory=player.getInventory();
               MoreUtils.getInstance().getLogger().info(playerInventory.getItemInMainHand().toString());
               MoreUtils.getInstance().getLogger().info(new ItemStack(Material.EGG).toString());

                 if (playerInventory.getItemInMainHand().getType()==Material.EGG){
                     if (hurter instanceof LivingEntity && !(hurter instanceof Player)) {
                         World wor = hurter.getWorld();
                         Location loc = hurter.getLocation();
                         wor.dropItem(loc, getSpawnEggFromEntity(hurter.getType()));
                         //这里把被打到的生物传送到虚空
                         hurter.teleport(new Location(wor, 0, -10, 0));
                         playerInventory.setItemInMainHand(new ItemStack(Material.EGG,playerInventory.getItemInMainHand().getAmount()-1));
                     }
                 }else{
                     System.out.println("你没有egg");
                 }



            }
        } else {
            MoreUtils.getInstance().getLogger().info("打到了" + hurter + hurter.getClass().getName());
        }
    }

}
