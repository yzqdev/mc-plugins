package org.lores.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.lores.Lores;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 14:05
 * @Modified By:
 */
public class Lore implements TabExecutor {
    Lores lore;

    public Lore(Lores lores) {
        this.lore = lores;
    }

    private enum Action {
        /**
         * 命名
         */
        NAME,
        OWNER, ADD, DELETE, SET, INSERT, CLEAR, UNDO;
    }

    private final String[] subCommands = {"add", "name", "owner", "delete", "set", "insert", "clear", "undo"};
    private static HashMap<String, LinkedList<ItemStack>> undo = new HashMap<>();
    char[] colorCodes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'l', 'n', 'o', 'k', 'm', 'r'};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        int stackSize;
        ItemStack undoneItem;
        LinkedList<ItemStack> list;
        int i, index;
        String name;
        Action action;
        if (!(sender instanceof Player player)) {
            sendHelp(sender);
            return true;
        }
        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = playerInventory.getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            if (meta == null) {
                player.sendMessage("§4The Item you are holding does not support Lore");
                return true;
            }
        }
        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new LinkedList<>();
        }
        try {
            action = Action.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException notEnum) {
            sendHelp(sender);
            return true;
        }
        String id = player.getName() + "'" + item.getType();
        if (action != Action.UNDO) {
            if (!undo.containsKey(id)) {
                undo.put(id, new LinkedList<ItemStack>());
            }
            list = undo.get(id);
            list.addFirst(item.clone());
            while (list.size() > 5) {
                list.removeLast();
            }
        }
        switch (action) {
            default:
                System.out.println("default");

            case NAME:
                if (!sender.hasPermission("lores.name") || args.length < 2) {
                    sendHelp(sender);
                    return true;
                }
                name = concatArgs(sender, args, 1);
                if (name.contains("|")) {
                    index = name.replaceAll("§[0-9a-klmnor]", "").length();
                    for (String s : lore) {
                        index = Math.max(index, s.replaceAll("§[0-9a-klmnor]", "").length());
                    }
                    int spaces = index - name.replaceAll("§[0-9a-klmnor]", "").length() - 1;
                    StringBuilder space = new StringBuilder(" ");
                    for (int j = 1; j < spaces * 1.5D; j++) {
                        space.append(' ');
                    }
                    name = name.replace("|", space.toString());
                }
                meta.setDisplayName(name);
                break;
            case OWNER:
                if (!sender.hasPermission("lores.owner") || args.length < 2) {
                    sendHelp(sender);
                    return true;
                }
                if (!(meta instanceof SkullMeta)) {
                    player.sendMessage("§4You may only set the Owner of a §6Skull");
                    return true;
                }
                ((SkullMeta) meta).setOwner(args[1]);
                break;
            case ADD:
                if (!sender.hasPermission("lores.lore") || args.length < 2) {
                    sendHelp(sender);
                    return true;
                }
                lore.add(concatArgs(sender, args, 1));
                break;
            case DELETE:
                if (!sender.hasPermission("lores.lore")) {
                    sendHelp(sender);
                    return true;
                }
                switch (args.length) {
                    default:
                        System.out.println("default");

                    case 1:
                        if (lore.size() < 1) {
                            player.sendMessage("§4There is nothing to delete!");
                            return true;
                        }
                        lore.remove(lore.size() - 1);
                        break;
                    case 2:
                        try {
                            index = Integer.parseInt(args[1]) - 1;
                        } catch (Exception e) {
                            return false;
                        }
                        if (lore.size() <= index || index < 0) {
                            player.sendMessage("§4Invalid line number!");
                            return true;
                        }
                        lore.remove(index);
                        break;
                }
                return false;
            case SET:
                if (!sender.hasPermission("lores.lore") || args.length < 3) {
                    sendHelp(sender);
                    return true;
                }
                try {
                    index = Integer.parseInt(args[1]) - 1;
                } catch (Exception e) {
                    return false;
                }
                if (lore.size() <= index || index < 0) {
                    player.sendMessage("§4Invalid line number!");
                    return true;
                }
                lore.set(index, concatArgs(sender, args, 2));
                break;
            case INSERT:
                if (!sender.hasPermission("lores.lore") || args.length < 3) {
                    sendHelp(sender);
                    return true;
                }
                try {
                    i = Integer.parseInt(args[1]) - 1;
                } catch (Exception e) {
                    return false;
                }
                if (lore.size() <= i || i < 0) {
                    player.sendMessage("§4Invalid line number!");
                    return true;
                }
                lore.add(i, concatArgs(sender, args, 2));
                break;
            case CLEAR:
                if (!sender.hasPermission("lores.lore") || args.length != 1) {
                    sendHelp(sender);
                    return true;
                }
                lore.clear();
                break;
            case UNDO:
                if (args.length != 1) {
                    return false;
                }
                list = undo.get(id);
                if (list == null) {
                    player.sendMessage("§4You have not yet modified this Item!");
                    return true;
                }
                if (list.size() < 1) {
                    player.sendMessage("§4You cannot continue to undo for this Item!");
                    return true;
                }
                undoneItem = list.removeFirst();
                if (!item.isSimilar(undoneItem) && item.getType() != Material.SKELETON_SKULL) {
                    player.sendMessage("§4You have not yet modified this Item!");
                    return true;
                }
                stackSize = item.getAmount();
                if (undoneItem.getAmount() != stackSize) {
                    undoneItem.setAmount(stackSize);
                }


                playerInventory.setItemInMainHand(undoneItem);
                player.sendMessage("§5The last modification you made on this item has been undone!");
                return true;
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.sendMessage("§5Lore successfully modified!");
        return true;
    }

    private static String concatArgs(CommandSender sender, String[] args, int first) {
        StringBuilder sb = new StringBuilder();
        if (first > args.length) {
            return "";
        }
        for (int i = first; i <= args.length - 1; i++) {
            sb.append(" ");
            sb.append(ChatColor.translateAlternateColorCodes('&', args[i]));
        }
        String string = sb.substring(1);
        char[] charArray = string.toCharArray();
        boolean modified = false;
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '§' && !sender.hasPermission("lores.color." + charArray[i + 1])) {
                charArray[i] = '?';
                modified = true;
            }
        }
        return modified ? String.copyValueOf(charArray) : string;
    }

    private static void sendHelp(CommandSender sender) {
        sender.sendMessage("§e     Lores Help Page:");
        sender.sendMessage("§5Each command will modify the Item in your hand");
        if (sender.hasPermission("lores.color") || sender.hasPermission("lores.format")) {
            sender.sendMessage("§5Use §6& §5to add color with any command");
        }
        if (sender.hasPermission("lores.name")) {
            sender.sendMessage("§2/lore name <custom name> §bSet the new Name of the Item");
        }
        if (sender.hasPermission("lores.owner")) {
            sender.sendMessage("§2/lore owner <player> §bChange the Owner of a Skull");
        }
        if (sender.hasPermission("lores.lore")) {
            sender.sendMessage("§2/lore add <line of text> §bAdd a line to the lore");
            sender.sendMessage("§2/lore set <line #> <line of text> §bChange a line of the lore");
            sender.sendMessage("§2/lore insert <line #> <line of text> §bInsert a line into the lore");
            sender.sendMessage("§2/lore delete [line #] §bDelete a line of the lore (last line by default)");
            sender.sendMessage("§2/lore clear §bClear all lines of the lore");
        }
        sender.sendMessage("§2/lore undo §bUndoes your last modification (up to 5 times)");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
       Bukkit.getLogger().info( args.length+"这是长度");
        //if (args.length == 1) {
        //    return new ArrayList<>();
        //}
        if (args.length == 0) {
            return Arrays.asList(subCommands);
        }
        //筛选所有可能的补全列表，并返回
        return Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }
}
