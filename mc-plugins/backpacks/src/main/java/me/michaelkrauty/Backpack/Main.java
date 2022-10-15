package me.michaelkrauty.Backpack;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created on 7/6/2014.
 *
 * @author michaelkrauty
 */
public class Main extends JavaPlugin implements Listener {

    public static Main main;

    public static ArrayList<Backpack> backpacks = new ArrayList<>();

    public HashMap<Player, String> open = new HashMap<>();

    public HashMap<UUID, Integer> cooldowns = new HashMap<>();

    public static Economy economy = null;

    public static Config config;
    public static Locale locale;

    public static SQL sql = null;

    public static int cost = 0;
    public static int cooldown = 0;

    @Override
    public void onEnable() {
        main = this;
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("backpack").setExecutor(new BackpackCommand(this));
        config = new Config(this);
        locale = new Locale(this);
        if (config.getBoolean("checkupdate")) {
            //checkUpdate();
        }
        cooldown = config.getInt("cooldown");
        try {
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                if (setupEconomy()) {
                    cost = config.getInt("cost");
                }
            }
        } catch (NullPointerException e) {
            getLogger().info("Vault is not installed on this server. Economy features disabled.");
        }

        if (config.getString("data").equalsIgnoreCase("mysql") || config.getString("data").equalsIgnoreCase("sql")) {
            sql = new SQL(this);
        } else if (!config.getString("data").equalsIgnoreCase("flatfile") && !config.getString("data").equalsIgnoreCase("file")) {
            getLogger().warning("unrecognized data format: " + config.getString("data") + ". Must either be \"flatfile\" or \"mysql\". Using flatfile...");
        }

        try {
            Metrics metrics = new me.michaelkrauty.Backpack.Metrics(this);
            Metrics.Graph graph = metrics.createGraph("Backpacks");
            graph.addPlotter(new Metrics.Plotter() {
                @Override
                public int getValue() {
                    return backpacks.size();
                }
            });
            metrics.start();
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Couldn't start metrics: " + e.getMessage());
        }
        if (sql == null) {
            File file = new File(getDataFolder(), "backpacks");
            if (!file.exists()) {
                file.mkdir();
            }
        }
        cooldowns = new Cooldowns(this).getCooldowns();
        loadBackpacks();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Map.Entry<UUID, Integer> entry : cooldowns.entrySet()) {
                if (entry.getValue() < 1) {
                    cooldowns.remove(entry.getKey());
                } else {
                    cooldowns.put(entry.getKey(), entry.getValue() - 1);
                }
            }
        }, 20, 20);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    @Override
    public void onDisable() {
        Cooldowns cd = new Cooldowns(this);
        for (Map.Entry<UUID, Integer> entry : cooldowns.entrySet()) {
            cd.setCooldown(entry.getKey(), entry.getValue());
        }
        cd.save();
        for (Map.Entry<Player, String> entry : open.entrySet()) {
            entry.getKey().closeInventory();
        }
        backpacks.clear();
        if (sql != null) {
            sql.closeConnection();
        }
    }

    //private void checkUpdate() {
    //    new Updater(this, 83139, this.getFile(), Updater.UpdateType.DEFAULT, true);
    //}

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null) {
                if (event.getItem().getType() == Material.CHEST) {
                    if (event.getItem().getItemMeta().getLore() != null) {
                        if (event.getItem().getItemMeta().getLore().get(0) != null) {
                            if (main.getBackpack(event.getItem().getItemMeta().getLore().get(0)) != null) {
                                event.setCancelled(true);
                                event.getPlayer().openInventory(getBackpack(event.getItem().getItemMeta().getLore().get(0)).getInventory());
                                open.put(event.getPlayer(), event.getItem().getItemMeta().getLore().get(0));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (open.get(event.getPlayer()) != null) {
            String uuid = open.get(event.getPlayer());
            open.remove(event.getPlayer());
            for (ItemStack item : event.getPlayer().getInventory().getContents()) {
                if (item != null) {
                    if (item.getType() == Material.CHEST) {
                        if (item.getItemMeta().getLore() != null) {
                            if (item.getItemMeta().getLore().get(0) != null) {
                                if (item.getItemMeta().getLore().get(0).equals(uuid)) {
                                    getBackpack(uuid).setInventory(event.getInventory());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getItemMeta() != null) {
                if (event.getCurrentItem().getItemMeta().getLore() != null) {
                    if (event.getCurrentItem().getItemMeta().getLore().get(0) != null) {
                        Backpack b2;
                        if ((b2 = getBackpack(event.getCurrentItem().getItemMeta().getLore().get(0))) != null) {
                            Player player = (Player) event.getViewers().get(0);
                            if (event.getAction() == InventoryAction.PICKUP_HALF) {
                                if (event.getViewers() == null) {
                                    main.getLogger().log(Level.SEVERE, "List of backpack viewers is null!");
                                    main.getLogger().log(Level.SEVERE, "Backpack UUID: " + b2.getUUID().toString());
                                    return;
                                }
                                if (b2.getInventory().getViewers().size() != 0) {
                                    event.getWhoClicked().getServer().getPlayer(event.getWhoClicked().getUniqueId()).sendMessage(ChatColor.RED + "Someone else already has that backpack open.");
                                    event.setCancelled(true);
                                    return;
                                }
                                if (open.get(player) != null) {
                                    Backpack b1 = getBackpack(open.get(player));
                                    b1.setInventory(player.getOpenInventory().getTopInventory());
                                    open.remove(player);
                                }
                                player.getOpenInventory().close();
                                player.openInventory(b2.getInventory());
                                open.put(player, b2.getUUID());
                                event.setCancelled(true);
                                return;
                            }
                            if (open.get(player) != null) {
                                if (open.get(player).equals(event.getCurrentItem().getItemMeta().getLore().get(0))) {
                                    player.sendMessage(new String[]{ChatColor.RED + "You can't move a backpack while it's open", ChatColor.GRAY + "(this is to prevent dividing by zero)"});
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemDespawn(ItemDespawnEvent event) {
        if (!event.isCancelled()) {
            if (event.getEntity() != null) {
                if (event.getEntity().getItemStack() != null) {
                    if (event.getEntity().getItemStack().getItemMeta() != null) {
                        if (event.getEntity().getItemStack().getItemMeta().getLore() != null) {
                            if (event.getEntity().getItemStack().getItemMeta().getLore().get(0) != null) {
                                if (getBackpack(event.getEntity().getItemStack().getItemMeta().getLore().get(0)) != null) {
                                    deleteBackpack(event.getEntity().getItemStack().getItemMeta().getLore().get(0));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Backpack getBackpack(String uuid) {
        for (Backpack backpack : backpacks) {
            if (backpack.getUUID().equals(uuid)) {
                return backpack;
            }
        }
        return null;
    }

    public void deleteBackpack(String uuid) {
        if (getBackpack(uuid) != null) {
            Backpack backpack = getBackpack(uuid);
            backpacks.remove(backpack);
            if (backpack.getFile().exists()) {
                backpack.getFile().delete();
            }
            if (sql != null) {
                final String finalUUID = uuid;
                getServer().getScheduler().scheduleAsyncDelayedTask(this, () -> sql.removeBackpack(finalUUID));
            }
        }
    }

    public void loadBackpacks() {
        getLogger().info("Loading backpacks...");
        if (!backpacks.isEmpty()) {
            backpacks.clear();
        }
        if (sql != null) {
            sql.loadAllBackpacks();
        }
        for (File file : new File(getDataFolder() + "/backpacks").listFiles()) {
            try {
                if (main.getBackpack(file.getName()) == null) {
                    backpacks.add(new Backpack(this, file.getName()));
                }
            } catch (NullPointerException e) {
                getLogger().info("Couldn't load backpack: " + file.getName() + " (NullPointerException)");
            }
        }
        getLogger().info("Loaded " + backpacks.size() + " backpacks.");
    }

    public void saveBackpacks() {
        getLogger().info("Saving backpacks...");
        for (Backpack backpack : backpacks) {
            backpack.save();
        }
        getLogger().info("Saved " + backpacks.size() + " backpacks.");
    }

    public static String color(String str) {
        return str.replace("&0", "§0")
                .replace("&1", "§1")
                .replace("&2", "§2")
                .replace("&3", "§3")
                .replace("&4", "§4")
                .replace("&5", "§5")
                .replace("&6", "§6")
                .replace("&7", "§7")
                .replace("&8", "§8")
                .replace("&9", "§9")
                .replace("&a", "§a")
                .replace("&b", "§b")
                .replace("&c", "§c")
                .replace("&d", "§d")
                .replace("&e", "§e")
                .replace("&f", "§f")
                .replace("&k", "§k")
                .replace("&l", "§l")
                .replace("&m", "§m")
                .replace("&n", "§n")
                .replace("&o", "§o")
                .replace("&r", "§r");
    }

    /**
     * Credit for the following two methods:
     * https://gist.github.com/graywolf336/8153678
     */

    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}