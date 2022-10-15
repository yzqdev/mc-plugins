package fyi.sugar.mobstoeggs.events;

import fyi.sugar.mobstoeggs.Main;
import fyi.sugar.mobstoeggs.data.EggDetermineData;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;


public class CapsuleHitEvent
        implements Listener {
    private static Main plugin = (Main) Main.getPlugin(Main.class);
    Economy economy = plugin.getEconomy();

    public static HashSet<UUID> eggstore = new HashSet<>();

    @EventHandler
    public void onBabyChickenSpawn(PlayerEggThrowEvent event) {
        event.setHatching(plugin.cm.getSettings().getBoolean("spawn-baby-chickens"));
    }

    @EventHandler
    public static void onCapsuleLaunchEvent(ProjectileLaunchEvent event) {
        ProjectileSource launcher = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        EntityType capsule = CapsuleThrowEvent.onCapsuleProjectile(projectile.getType());

        if (!projectile.getType().toString().toUpperCase().equals(plugin.cm.getSettings().getString("capsule.type"))) {
            return;
        }
        if (!projectile.getType().equals(capsule)) {
            return;
        }
        if (!(launcher instanceof Player)) {
            return;
        }

        ItemStack theegg = ((HumanEntity) launcher).getInventory().getItemInMainHand();
        if (!plugin.cm.getSettings().getString("capsule.name").equalsIgnoreCase("") &&
                theegg.getItemMeta().getDisplayName() != null &&
                !theegg.getItemMeta().getDisplayName().toString().equals(ChatColor.translateAlternateColorCodes('&', plugin.cm.getSettings().getString("capsule.name")))) {
            return;
        }


        if (!plugin.cm.getSettings().getStringList("capsule.lore").isEmpty()) {
            if (theegg.getItemMeta().hasLore()) {
                if (!theegg.getItemMeta().getLore().equals(plugin.cm.getSettings().getStringList("capsule.lore").stream().map(thelore -> ChatColor.translateAlternateColorCodes('&', thelore)).collect(Collectors.toList()))) {
                    return;
                }
            } else if (!theegg.getItemMeta().hasLore()) {
                return;
            }
        }
        eggstore.add(((HumanEntity) launcher).getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEndermanHitEvent(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        Entity entity = event.getHitEntity();

        if (!(entity instanceof org.bukkit.entity.Enderman)) {
            return;
        }
        if (!projectile.getType().toString().toUpperCase().equals(plugin.cm.getSettings().getString("capsule.type"))) {
            return;
        }

        EntityType capsule = CapsuleThrowEvent.onCapsuleProjectile(projectile.getType());


        if (!projectile.getType().equals(capsule)) {
            return;
        }

        if (!projectile.getType().toString().toUpperCase().equals(plugin.cm.getSettings().getString("capsule.type"))) {
            return;
        }

        if (!(projectile instanceof Projectile)) {
            return;
        }

        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) projectile.getShooter();


        if (!(entity instanceof org.bukkit.entity.LivingEntity)) {
            if (eggstore.contains(player.getUniqueId())) {
                eggstore.remove(player.getUniqueId());
            }
            return;
        }
        if (!(entity instanceof org.bukkit.entity.Damageable)) {
            if (eggstore.contains(player.getUniqueId())) {
                eggstore.remove(player.getUniqueId());
            }

            return;
        }
        if (entity.getType().equals(EntityType.PLAYER)) {
            if (eggstore.contains(player.getUniqueId())) {
                eggstore.remove(player.getUniqueId());
            }
            return;
        }
        if (!eggstore.contains(player.getUniqueId())) {
            return;
        }
        eggstore.remove(player.getUniqueId());

        if (entity.getCustomName() != null &&
                !plugin.cm.getSettings().getBoolean("catch-customname-mobs")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-custom").replaceAll("%mobname%", entity.getCustomName())));

            return;
        }

        String gname = entity.getType().toString().toLowerCase();
        gname = gname.replace("_", " ");

        if (plugin.cm.getSettings().getBoolean("use-permissions") &&
                !player.hasPermission("mobstoeggs.catch." + entity.getType().toString().toLowerCase())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-denied").replaceAll("%mobname%", gname)));


            return;
        }

        String mname = entity.getType().toString().toUpperCase();
        mname = mname.replace("_", " ");

        if (!plugin.cm.getSettings().getBoolean("use-global-values")) {
            if (!plugin.cm.getMobs().getBoolean(mname + ".enabled")) {
                return;
            }
            double catchChance = plugin.cm.getMobs().getInt(mname + ".catch-chance");
            if (Math.random() * 100.0D <= catchChance) {


                if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ECONOMY")) {
                    Player oplayer = Bukkit.getPlayer(player.getUniqueId());
                    double mprice = plugin.cm.getMobs().getDouble(mname + ".price");

                    if (plugin.econ.getBalance((OfflinePlayer) oplayer) >= mprice) {
                        EconomyResponse r = plugin.econ.withdrawPlayer((OfflinePlayer) oplayer, mprice);
                        if (r.transactionSuccess()) {
                            String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                            String money = String.valueOf(mprice);
                            ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                            ecosuccess = ecosuccess.replace("%money%", money);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                        } else {
                            return;
                        }
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        String money = String.valueOf(mprice);
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        ecofail = ecofail.replace("%money%", money);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);


                        return;
                    }
                } else if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ITEM")) {
                    String mprice = plugin.cm.getMobs().getString(mname + ".price");
                    if (!mprice.contains("-")) {
                        Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | Using ITEM but could not find ITEM_NAME-amount for entity: " + ChatColor.RED + " in mobs.yml!");
                        return;
                    }
                    String[] itemp = mprice.split("-");
                    String itemname = itemp[0].toUpperCase();
                    int itemamount = Integer.parseInt(itemp[1]);

                    ItemStack item = new ItemStack(Material.valueOf(itemname));
                    item.setAmount(itemamount);

                    if (player.getInventory().containsAtLeast(item, itemamount)) {
                        player.getInventory().removeItem(new ItemStack[]{item});
                        String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                        ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecosuccess = ecosuccess.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecofail = ecofail.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);
                        return;
                    }
                }
            } else {
                return;
            }
        } else {
            double catchChance = plugin.cm.getSettings().getInt("global-catch-chance");
            if (Math.random() * 100.0D <= catchChance) {


                if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ECONOMY")) {
                    Player oplayer = Bukkit.getPlayer(player.getUniqueId());
                    double mprice = plugin.cm.getSettings().getDouble("global-cost");
                    if (plugin.econ.getBalance((OfflinePlayer) oplayer) >= mprice) {
                        EconomyResponse r = plugin.econ.withdrawPlayer((OfflinePlayer) oplayer, mprice);
                        if (r.transactionSuccess()) {
                            String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                            String money = String.valueOf(mprice);
                            ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                            ecosuccess = ecosuccess.replace("%money%", money);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                        } else {
                            return;
                        }
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        String money = String.valueOf(mprice);
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        ecofail = ecofail.replace("%money%", money);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);


                        return;
                    }
                } else if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ITEM")) {
                    String mprice = plugin.cm.getSettings().getString("global-cost");
                    if (!mprice.contains("-")) {
                        Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | Using ITEM but could not find ITEM_NAME-amount for global-cost!");
                        return;
                    }
                    String[] itemp = mprice.split("-");
                    String itemname = itemp[0].toUpperCase();
                    int itemamount = Integer.parseInt(itemp[1]);

                    ItemStack item = new ItemStack(Material.valueOf(itemname));
                    item.setAmount(itemamount);

                    if (player.getInventory().containsAtLeast(item, itemamount)) {
                        player.getInventory().removeItem(new ItemStack[]{item});
                        String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                        ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecosuccess = ecosuccess.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecofail = ecofail.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);
                        return;
                    }
                }
            } else {
                return;
            }
        }
        EntityTraitsEvent.onEntityTraits(new EggDetermineData(player, (Entity) projectile, entity));
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public static void onCapsuleHitEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        EntityType capsule = CapsuleThrowEvent.onCapsuleProjectile(damager.getType());


        if (!damager.getType().equals(capsule)) {
            return;
        }

        if (!damager.getType().toString().toUpperCase().equals(plugin.cm.getSettings().getString("capsule.type"))) {
            return;
        }

        if (!(damager instanceof Projectile)) {
            return;
        }

        if (!(((Projectile) damager).getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) ((Projectile) damager).getShooter();


        if (!(entity instanceof org.bukkit.entity.LivingEntity)) {
            if (eggstore.contains(player.getUniqueId())) {
                eggstore.remove(player.getUniqueId());
            }
            return;
        }
        if (!(entity instanceof org.bukkit.entity.Damageable)) {
            if (eggstore.contains(player.getUniqueId())) {
                eggstore.remove(player.getUniqueId());
            }

            return;
        }
        if (entity.getType().equals(EntityType.PLAYER)) {
            if (eggstore.contains(player.getUniqueId())) {
                eggstore.remove(player.getUniqueId());
            }
            return;
        }
        if (!eggstore.contains(player.getUniqueId())) {
            return;
        }
        eggstore.remove(player.getUniqueId());

        if (entity instanceof Ageable &&
                !((Ageable) entity).isAdult() &&
                !plugin.cm.getSettings().getBoolean("catch-baby-mobs")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-baby")));


            return;
        }


        if (entity instanceof Tameable && (
                (Tameable) entity).isTamed() &&
                !(entity instanceof org.bukkit.entity.SkeletonHorse) &&
                !plugin.cm.getSettings().getBoolean("catch-tamed-mobs")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-tamed")));


            return;
        }


        if (entity instanceof Sheep && (
                (Sheep) entity).isSheared() &&
                !plugin.cm.getSettings().getBoolean("catch-sheared-mobs")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-sheared")));


            return;
        }

        if (entity.getCustomName() != null) {
            if (!plugin.cm.getSettings().getBoolean("catch-customname-mobs")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-custom").replaceAll("%mobname%", entity.getCustomName())));
                return;
            }
            if (plugin.cm.getSettings().getBoolean("remove-custom-name")) {
                entity.setCustomName("");
            }
        }

        String gname = entity.getType().toString().toLowerCase();
        gname = gname.replace("_", " ");

        if (plugin.cm.getSettings().getBoolean("use-permissions") &&
                !player.hasPermission("mobstoeggs.catch." + entity.getType().toString().toLowerCase())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-denied").replaceAll("%mobname%", gname)));


            return;
        }

        String mname = entity.getType().toString().toUpperCase();
        mname = mname.replace("_", " ");

        if (!plugin.cm.getSettings().getBoolean("use-global-values")) {
            if (!plugin.cm.getMobs().getBoolean(mname + ".enabled")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("try-catch-denied").replaceAll("%mobname%", gname)));

                return;
            }
            double catchChance = plugin.cm.getMobs().getInt(mname + ".catch-chance");
            if (Math.random() * 100.0D <= catchChance) {


                if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ECONOMY")) {
                    Player oplayer = Bukkit.getPlayer(player.getUniqueId());
                    double mprice = plugin.cm.getMobs().getDouble(mname + ".price");

                    if (plugin.econ.getBalance((OfflinePlayer) oplayer) >= mprice) {
                        EconomyResponse r = plugin.econ.withdrawPlayer((OfflinePlayer) oplayer, mprice);
                        if (r.transactionSuccess()) {
                            String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                            String money = String.valueOf(mprice);
                            ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                            ecosuccess = ecosuccess.replace("%money%", money);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                        } else {
                            return;
                        }
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        String money = String.valueOf(mprice);
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        ecofail = ecofail.replace("%money%", money);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);


                        return;
                    }
                } else if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ITEM")) {
                    String mprice = plugin.cm.getMobs().getString(mname + ".price");
                    if (!mprice.contains("-")) {
                        Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | Using ITEM but could not find ITEM_NAME-amount for entity: " + ChatColor.RED + " in mobs.yml!");
                        return;
                    }
                    String[] itemp = mprice.split("-");
                    String itemname = itemp[0].toUpperCase();
                    int itemamount = Integer.parseInt(itemp[1]);

                    ItemStack item = new ItemStack(Material.valueOf(itemname));
                    item.setAmount(itemamount);

                    if (player.getInventory().containsAtLeast(item, itemamount)) {
                        player.getInventory().removeItem(new ItemStack[]{item});
                        String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                        ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecosuccess = ecosuccess.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecofail = ecofail.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);
                        return;
                    }
                }
            } else {
                return;
            }
        } else {
            double catchChance = plugin.cm.getSettings().getInt("global-catch-chance");
            if (Math.random() * 100.0D <= catchChance) {


                if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ECONOMY")) {
                    Player oplayer = Bukkit.getPlayer(player.getUniqueId());
                    double mprice = plugin.cm.getSettings().getDouble("global-cost");
                    if (plugin.econ.getBalance((OfflinePlayer) oplayer) >= mprice) {
                        EconomyResponse r = plugin.econ.withdrawPlayer((OfflinePlayer) oplayer, mprice);
                        if (r.transactionSuccess()) {
                            String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                            String money = String.valueOf(mprice);
                            ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                            ecosuccess = ecosuccess.replace("%money%", money);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                        } else {
                            return;
                        }
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        String money = String.valueOf(mprice);
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        ecofail = ecofail.replace("%money%", money);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);


                        return;
                    }
                } else if (plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ITEM")) {
                    String mprice = plugin.cm.getSettings().getString("global-cost");
                    if (!mprice.contains("-")) {
                        Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "MTE | Using ITEM but could not find ITEM_NAME-amount for global-cost!");
                        return;
                    }
                    String[] itemp = mprice.split("-");
                    String itemname = itemp[0].toUpperCase();
                    int itemamount = Integer.parseInt(itemp[1]);

                    ItemStack item = new ItemStack(Material.valueOf(itemname));
                    item.setAmount(itemamount);

                    if (player.getInventory().containsAtLeast(item, itemamount)) {
                        player.getInventory().removeItem(new ItemStack[]{item});
                        String ecosuccess = plugin.cm.getMessages().getString("economy-success");
                        ecosuccess = ecosuccess.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecosuccess = ecosuccess.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecosuccess));
                    } else {
                        String ecofail = plugin.cm.getMessages().getString("economy-fail");
                        ecofail = ecofail.replace("%mobname%", entity.getName());
                        itemname = itemname.replace("_", " ");
                        ecofail = ecofail.replace("%money%", "x" + itemamount + " " + StringUtils.capitalize(itemname));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ecofail));

                        RefundCostEvent.getRefundEvent(player, 1);
                        return;
                    }
                }
            } else {
                return;
            }
        }
        EntityTraitsEvent.onEntityTraits(new EggDetermineData(player, damager, entity));
    }
}


