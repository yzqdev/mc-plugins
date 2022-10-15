package org.moreutils.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.moreutils.MoreUtils
import org.moreutils.utils.HpUtils
import java.util.*
import java.util.stream.Collectors

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/25 19:28
 * @Modified By:
 */
class Hp(private val moreUtils: MoreUtils) : TabExecutor {
    private val subCommands = arrayOf("setall", "set")
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        commandLabel: String,
        args: Array<String>
    ): Boolean {
        if ("hp".equals(command.name, ignoreCase = true)) {
            if (args.size >= 1) {
                try {
                    when (args[0]) {
                        "setall" -> {
                            MoreUtils.get().getConfig().set("player-max-health", args[1].toInt())
                            HpUtils.setDefaultHp(args[1])
                            sender.sendMessage(ChatColor.YELLOW.toString() + "设置所有玩家血量为" + args[1])
                        }

                        "set" -> {
                            val player = Bukkit.getServer().getPlayer(args[1])!!
                           player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue   =
                                args[2].toDouble()
                            sender.sendMessage(ChatColor.YELLOW.toString() + "设置玩家" + player.name + "当前血量为   " + args[2])
                        }

                        else -> {
                            run {}
                            run {
                                MoreUtils.get().getConfig().set("player-max-health", args[1].toInt())
                                HpUtils.setDefaultHp(args[1])
                                sender.sendMessage(ChatColor.YELLOW.toString() + "设置所有玩家血量为" + args[1])
                            }
                            run {
                                val player = Bukkit.getServer().getPlayer(args[1])!!
                                player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue  =
                                    args[2].toDouble()
                                sender.sendMessage(ChatColor.YELLOW.toString() + "设置玩家" + player.name + "当前血量为   " + args[2])
                            }
                        }
                    }
                } catch (e: Exception) {
                    sender.sendMessage(ChatColor.RED.toString() + "血量必须是数字!")
                    e.printStackTrace()
                }
                MoreUtils.get().saveConfig()
            } else {
                try {
                    sender.sendMessage(ChatColor.BLUE.toString() + "输入/hp setall [血量]")
                } catch (e: Exception) {
                    sender.sendMessage(ChatColor.RED.toString() + "血量必须是数字!")
                    e.printStackTrace()
                }
            }
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return if (args.size == 0) {
            Arrays.asList(*subCommands)
        } else Arrays.stream(subCommands).filter { s: String -> s.startsWith(args[0]) }.collect(Collectors.toList())
    }
}