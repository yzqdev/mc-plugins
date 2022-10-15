package org.health;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;

import org.health.api.HealthBarAPI;

public class MiscListeners implements Listener {
    private final static Plugin    instance                    = Main.plugin;
    private static Scoreboard      fakeSb                      = instance.getServer()
                                                                         .getScoreboardManager()
                                                                         .getNewScoreboard();
    private static Scoreboard      mainSb                      = instance.getServer()
                                                                         .getScoreboardManager()
                                                                         .getMainScoreboard();
    private static boolean         pluginDisabledWhiteTabNames = false;
    private static BukkitScheduler scheduler                   = Bukkit.getScheduler();
    private static boolean         fixTabNames;
    private static boolean         usePlayerPermissions;
    private static boolean         playerEnabled;
    private static int             playerHideDelay;
    private static boolean         playerUseAfter;

    // temp fix
    private static boolean overrideOtherScoreboards;

    // disabled worlds names
    private static boolean      playerUseDisabledWorlds;
    private static List<String> playerDisabledWorlds;

    public static void disableTabNamesFix() {
        pluginDisabledWhiteTabNames = true;
    }

    // only needed for joinEvent
    private static void fixTabName(Player p) {
        if (fixTabNames &&!pluginDisabledWhiteTabNames) {
            if (p.getPlayerListName().startsWith("?")) {
                return;    // is already colored!
            }

            if (p.getName().length() > 14) {
                p.setPlayerListName(p.getName().substring(0, 14));
                p.setPlayerListName(p.getName());
            } else {
                p.setPlayerListName("?f" + p.getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void joinHighest(PlayerJoinEvent event) {

        // always check this on all the events!
        if (!playerEnabled) {
            return;
        }

        final Player p = event.getPlayer();

        // eventually update the scoreboard
        updateScoreboard(p, p.getWorld().getName().toLowerCase());

        // update the health bars
        scheduler.scheduleSyncDelayedTask(instance,
                                          new Runnable() {
                                              public void run() {
                                                  updatePlayer(p);
                                              }
                                          },
                                          1L);

        // update notifications
        Updater.UpdaterHandler.notifyIfUpdateWasFound(p, "healthbar.update");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void joinLowest(PlayerJoinEvent event) {

        // always check this on all the events!
        if (!playerEnabled) {
            return;
        }

        // to let other plugins override the tab name
        try {
            fixTabName(event.getPlayer());
        } catch (Exception ex) {}
    }

    public static void loadConfiguration() {
        FileConfiguration config = instance.getConfig();

        usePlayerPermissions     = config.getBoolean(Configuration.Nodes.USE_PLAYER_PERMISSIONS.getNode());
        fixTabNames              = config.getBoolean(Configuration.Nodes.FIX_TAB_NAMES.getNode());
        playerHideDelay          = config.getInt(Configuration.Nodes.PLAYERS_AFTER_DELAY.getNode());
        playerEnabled            = config.getBoolean(Configuration.Nodes.PLAYERS_ENABLE.getNode());
        playerUseAfter           = config.getBoolean(Configuration.Nodes.PLAYERS_AFTER_ENABLE.getNode());
        playerUseDisabledWorlds  = config.getBoolean(Configuration.Nodes.PLAYERS_WORLD_DISABLING.getNode());
        overrideOtherScoreboards = config.getBoolean(Configuration.Nodes.OVERRIDE_OTHER_SCOREBOARD.getNode());

        if (playerUseDisabledWorlds) {
            playerDisabledWorlds = Arrays.asList(instance.getConfig()
                                                         .getString(
                                                             Configuration.Nodes.PLAYERS_DISABLED_WORLDS.getNode())
                                                         .toLowerCase()
                                                         .replace(" ", "")
                                                         .split(","));
        }

        Player[] playerlist = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        // 这里改了
        if (playerlist.length != 0) {
            for (Player p : playerlist) {
                updatePlayer(p);
                updateScoreboard(p, p.getWorld().getName().toLowerCase());
                fixTabName(p);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if ((DamageListener.mobHideDelay == 0) && DamageListener.mobEnabled) {
            for (Entity entity : event.getChunk().getEntities()) {
                if ((entity instanceof LivingEntity) && (entity.getType() != EntityType.PLAYER)) {
                    DamageListener.parseMobHit((LivingEntity) entity, true);
                }
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (!DamageListener.mobEnabled) {
            return;
        }

        for (Entity entity : event.getChunk().getEntities()) {
            if ((entity instanceof LivingEntity) && (entity.getType() != EntityType.PLAYER)) {
                DamageListener.hideBar((LivingEntity) entity);
            }
        }
    }

    // restores the name of a mob if a player tries to open his inventory
    @EventHandler(
        ignoreCancelled = true,
        priority        = EventPriority.HIGHEST
    )
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        if ((event.getPlayer().getItemInHand().getType() == Material.NAME_TAG) && (entity instanceof LivingEntity)) {
            final LivingEntity mob = ((LivingEntity) entity);

            if ((DamageListener.mobHideDelay == 0L) && HealthBarAPI.mobHasBar(mob)) {
                scheduler.scheduleSyncDelayedTask(instance,
                                                  new Runnable() {
                                                      public void run() {
                                                          DamageListener.parseMobHit(mob, true);
                                                      }
                                                  },
                                                  20L);
            }

            DamageListener.hideBar(mob);
            mob.setCustomNameVisible(false);

            return;
        }

        if (entity instanceof Villager) {
            Villager villager = (Villager) entity;

            if (villager.isAdult() && HealthBarAPI.mobHasBar(villager)) {
                DamageListener.hideBar(villager);

                if (DamageListener.mobHideDelay == 0L) {
                    DamageListener.parseMobHit(villager, true);
                }
            }
        } else if (entity instanceof Horse) {
            final Horse horse = (Horse) entity;

            // check if can have inventory
            if ((horse.getVariant() == Variant.DONKEY) || (horse.getVariant() == Variant.MULE)) {
                if (HealthBarAPI.mobHasBar(horse)) {
                    DamageListener.hideBar(horse);

                    if (DamageListener.mobHideDelay == 0L) {
                        scheduler.scheduleSyncDelayedTask(Main.plugin,
                                                          new Runnable() {
                                                              public void run() {
                                                                  DamageListener.parseMobHit(horse, true);
                                                              }
                                                          },
                                                          1L);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true,
        priority        = EventPriority.HIGHEST
    )
    public void onVehicleEnter(VehicleEnterEvent event) {
        Vehicle vehicle = event.getVehicle();

        if (vehicle instanceof Horse) {
            final Horse horse = (Horse) vehicle;

//          the horse may have an inventory (opening is client side), and it doesn't display the name when ridden...so hide it!
            if (HealthBarAPI.mobHasBar(horse)) {
                DamageListener.hideBar(horse);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true,
        priority        = EventPriority.HIGHEST
    )
    public void onVehicleLeave(VehicleExitEvent event) {
        Vehicle vehicle = event.getVehicle();

        if (vehicle instanceof Horse) {
            final Horse horse = (Horse) vehicle;

            // restore the health bar when the player leaves
            if (DamageListener.mobHideDelay == 0L) {
                scheduler.scheduleSyncDelayedTask(instance,
                                                  new Runnable() {
                                                      public void run() {
                                                          DamageListener.parseMobHit(horse, true);
                                                      }
                                                  },
                                                  1L);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerRespawn(PlayerRespawnEvent event) {

        // always check this on all the events!
        if (!playerEnabled) {
            return;
        }

        final Player player = event.getPlayer();

        updateScoreboard(player, player.getWorld().getName().toLowerCase());
        scheduler.scheduleSyncDelayedTask(instance,
                                          new Runnable() {
                                              public void run() {
                                                  updatePlayer(player);
                                              }
                                          },
                                          1L);
    }

    @EventHandler(
        ignoreCancelled = true,
        priority        = EventPriority.HIGH
    )
    public void playerTeleport(final PlayerTeleportEvent event) {

        // always check this on all the events!
        if (!playerEnabled) {
            return;
        }

        final Player player = event.getPlayer();

        // teleport in the same world
        if (event.getFrom().getWorld() == event.getTo().getWorld()) {
            scheduler.scheduleSyncDelayedTask(instance,
                                              new Runnable() {
                                                  public void run() {
                                                      if (overrideOtherScoreboards) {
                                                          updateScoreboard(player,
                                                                           player.getWorld().getName().toLowerCase());
                                                      }

                                                      updatePlayer(player);
                                                  }
                                              },
                                              1L);

            // teleport in a different world
        } else {
            scheduler.scheduleSyncDelayedTask(instance,
                                              new Runnable() {
                                                  public void run() {

                                                      // whatever happens, update should be done for the right world
                                                      updatePlayer(player);
                                                  }
                                              },
                                              1L);

            if (overrideOtherScoreboards) {

                // schedule only if override is set to true
                scheduler.scheduleSyncDelayedTask(instance,
                                                  new Runnable() {
                                                      public void run() {

                                                          // whatever happens, update should be done for the right world
                                                          updateScoreboard(player,
                                                                           event.getTo()
                                                                                .getWorld()
                                                                                .getName()
                                                                                .toLowerCase());
                                                      }
                                                  },
                                                  1L);
            } else {
                updateScoreboard(player, event.getTo().getWorld().getName().toLowerCase());
            }
        }
    }

    private static void updatePlayer(final Player p) {

        // first off, update health below
        PlayerBar.updateHealthBelow(p);

        // if the plugin uses health on the tag, and the delay is 0, set it
        if (playerUseAfter && (playerHideDelay == 0)) {
            PlayerBar.setHealthSuffix(p, p.getHealth(), p.getMaxHealth());
        }
    }

    // just for the teleport event
    private static void updateScoreboard(Player p, String worldName) {
        if (!p.isOnline()) {
            return;
        }

        // permission check
        if (usePlayerPermissions) {
            if (!p.hasPermission("healthbar.see.onplayer")) {
                try {
                    p.setScoreboard(fakeSb);
                } catch (Exception ex) {}

                return;
            }
        }

        // world check
        if (playerUseDisabledWorlds) {
            if (playerDisabledWorlds.contains(worldName)) {
                try {
                    p.setScoreboard(fakeSb);
                } catch (Exception ex) {}

                return;
            }
        }

        // player is in correct world and with permissions
        try {
            p.setScoreboard(mainSb);
        } catch (Exception ex) {}
    }

    // end of the class
}


//~ Formatted by Jindent --- http://www.jindent.com
