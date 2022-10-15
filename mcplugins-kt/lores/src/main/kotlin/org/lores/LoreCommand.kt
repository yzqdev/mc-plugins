package org.lores

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.SkullMeta

import java.util.*
import java.util.stream.Collectors

/**
 * @author yzqde
 * @date time 2022/10/6 0:58
 * @modified By:
 *
 */
class LoreCommand(var lores: Lores) : TabExecutor {
    lateinit var lore: Lores

    private enum class Action {
        /**
         * 命名
         */
        NAME, OWNER, ADD, DELETE, SET, INSERT, CLEAR, UNDO
    }

    var subCommands = arrayOf("add", "name", "owner", "delete", "set", "insert", "clear", "undo")
    var undo: MutableMap<String, LinkedList<ItemStack>> = mutableMapOf()
    var colorCodes = charArrayOf(
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'l',
        'n',
        'o',
        'k',
        'm',
        'r'
    )

    private fun concatArgs(sender: CommandSender, args: Array<String>, first: Int): String {
        val sb = StringBuilder()
        if (first > args.size) {
            return ""
        }
        for (i in first..args.size - 1) {
            sb.append(" ")
            sb.append(ChatColor.translateAlternateColorCodes('&', args[i]))
        }
        val string = sb.substring(1)
        val charArray = string.toCharArray()
        var modified = false
        for (i in charArray.indices) {
            if (charArray[i] == '§' && !sender.hasPermission("lores.color." + charArray[i + 1])) {
                charArray[i] = '?'
                modified = true
            }
        }
        return if (modified) String(charArray) else string
    }


    private fun sendHelp(sender: CommandSender) {
        sender.sendMessage("§e     Lores Help Page:")
        sender.sendMessage("§5Each command will modify the Item in your hand")
        if (sender.hasPermission("lores.color") || sender.hasPermission("lores.format")) {
            sender.sendMessage("§5Use §6& §5to add color with any command")
        }
        if (sender.hasPermission("lores.name")) {
            sender.sendMessage("§2/lore name <custom name> §bSet the new Name of the Item")
        }
        if (sender.hasPermission("lores.owner")) {
            sender.sendMessage("§2/lore owner <player> §bChange the Owner of a Skull")
        }
        if (sender.hasPermission("lores.lore")) {
            sender.sendMessage("§2/lore add <line of text> §bAdd a line to the lore")
            sender.sendMessage("§2/lore set <line #> <line of text> §bChange a line of the lore")
            sender.sendMessage("§2/lore insert <line #> <line of text> §bInsert a line into the lore")
            sender.sendMessage("§2/lore delete [line #] §bDelete a line of the lore (last line by default)")
            sender.sendMessage("§2/lore clear §bClear all lines of the lore")
        }
        sender.sendMessage("§2/lore undo §bUndoes your last modification (up to 5 times)")
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        Bukkit.getLogger().info(args.size.toString() + "这是长度")
        //if (args.length == 1) {
        //    return new ArrayList<>();
        //}
        return if (args.isEmpty()) {
            mutableListOf(*subCommands)
        } else Arrays.stream(subCommands).filter { s: String ->
            s.startsWith(
                args[0]
            )
        }.collect(Collectors.toList())
        //筛选所有可能的补全列表，并返回
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        var stackSize: Int
        var undoneItem: ItemStack
        var list: LinkedList<ItemStack>
        var i: Int
        var index: Int
        var name: String
        var action: Action
        if (sender !is Player) {
            sendHelp(sender)
            return true
        }
        val playerInventory: PlayerInventory = sender.inventory
        val item = playerInventory.itemInMainHand
        var meta = item.itemMeta
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.type)
            if (meta == null) {
                sender.sendMessage("§4The Item you are holding does not support Lore")
                return true
            }
        }
        if (args.size < 1) {
            sendHelp(sender)
            return true
        }
        var lore = meta.lore
        if (lore == null) {
            lore = LinkedList()
        }
        try {
            action = Action.valueOf(args[0].uppercase(Locale.getDefault()))
        } catch (notEnum: IllegalArgumentException) {
            sendHelp(sender)
            return true
        }
        val id: String = sender.getName() + "'" + item.type
        if (action != Action.UNDO) {
            if (!undo.containsKey(id)) {
                undo[id] = LinkedList()
            }
            list = undo[id]!!
            list.addFirst(item.clone())
            while (list.size > 5) {
                list.removeLast()
            }
        }
        when (action) {
            Action.NAME -> {
                if (!sender.hasPermission("lores.name") || args.size < 2) {
                    sendHelp(sender)
                    return true
                }
                name = concatArgs(sender, args, 1)
                if (name.contains("|")) {
                    index = name.replace("§[0-9a-klmnor]".toRegex(), "").length
                    for (s in lore) {
                        index = Math.max(index, s.replace("§[0-9a-klmnor]".toRegex(), "").length)
                    }
                    val spaces = index - name.replace("§[0-9a-klmnor]".toRegex(), "").length - 1
                    val space = java.lang.StringBuilder(" ")
                    var j = 1
                    while (j < spaces * 1.5) {
                        space.append(' ')
                        j++
                    }
                    name = name.replace("|", space.toString())
                }
                meta.setDisplayName(name)
            }

            Action.OWNER -> {
                if (!sender.hasPermission("lores.owner") || args.size < 2) {
                    sendHelp(sender)
                    return true
                }
                if (meta !is SkullMeta) {
                    sender.sendMessage("§4You may only set the Owner of a §6Skull")
                    return true
                }
                (meta as SkullMeta).owner = args[1]
            }

            Action.ADD -> {
                if (!sender.hasPermission("lores.lore") || args.size < 2) {
                    sendHelp(sender)
                    return true
                }
                lore.add(concatArgs(sender, args, 1))
            }

            Action.DELETE -> {
                if (!sender.hasPermission("lores.lore")) {
                    sendHelp(sender)
                    return true
                }
                when (args.size) {
                    1 -> {
                        if (lore.size < 1) {
                            sender.sendMessage("§4There is nothing to delete!")
                            return true
                        }
                        lore.removeAt(lore.size - 1)
                    }

                    2 -> {
                        index = try {
                            args[1].toInt() - 1
                        } catch (e: Exception) {
                            return false
                        }
                        if (lore.size <= index || index < 0) {
                            sender.sendMessage("§4Invalid line number!")
                            return true
                        }
                        lore.removeAt(index)
                    }

                    else -> {
                        println("default")
                        if (lore.size < 1) {
                            sender.sendMessage("§4There is nothing to delete!")
                            return true
                        }
                        lore.removeAt(lore.size - 1)
                    }
                }
                return false
            }

            Action.SET -> {
                if (!sender.hasPermission("lores.lore") || args.size < 3) {
                    sendHelp(sender)
                    return true
                }
                index = try {
                    args[1].toInt() - 1
                } catch (e: java.lang.Exception) {
                    return false
                }
                if (lore.size <= index || index < 0) {
                    sender.sendMessage("§4Invalid line number!")
                    return true
                }
                lore[index] = concatArgs(sender, args, 2)
            }

            Action.INSERT -> {
                if (!sender.hasPermission("lores.lore") || args.size < 3) {
                    sendHelp(sender)
                    return true
                }
                i = try {
                    args[1].toInt() - 1
                } catch (e: java.lang.Exception) {
                    return false
                }
                if (lore.size <= i || i < 0) {
                    sender.sendMessage("§4Invalid line number!")
                    return true
                }
                lore.add(i, concatArgs(sender, args, 2))
            }

            Action.CLEAR -> {
                if (!sender.hasPermission("lores.lore") || args.size != 1) {
                    sendHelp(sender)
                    return true
                }
                lore.clear()
            }
            Action.UNDO->{
                if (args.size != 1) {
                    return false
                }
                list =  undo[id]!!
                if (list == null) {
                    sender.sendMessage("§4You have not yet modified this Item!")
                    return true
                }
                if (list.size < 1) {
                    sender.sendMessage("§4You cannot continue to undo for this Item!")
                    return true
                }
                undoneItem = list.removeFirst()
                if (!item.isSimilar(undoneItem) && item.type != Material.SKELETON_SKULL) {
                    sender.sendMessage("§4You have not yet modified this Item!")
                    return true
                }
                stackSize = item.amount
                if (undoneItem.amount != stackSize) {
                    undoneItem.amount = stackSize
                }


                playerInventory.setItemInMainHand(undoneItem)
                sender.sendMessage("§5The last modification you made on this item has been undone!")
                return true
            }

            else -> {
                println("default")
            }
        }

        meta.lore = lore
        item.itemMeta = meta
        sender.sendMessage("§5Lore successfully modified!")
        return true
    }


}