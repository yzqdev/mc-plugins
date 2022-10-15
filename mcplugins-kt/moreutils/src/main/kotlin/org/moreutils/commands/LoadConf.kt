package org.moreutils.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.moreutils.MoreUtils
import org.moreutils.utils.Config
import java.util.*
import java.util.stream.Collectors

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 1:39
 * @Modified By:
 */
class LoadConf(private val moreUtils: MoreUtils) : TabExecutor {
    private val subCommands = arrayOf("reload", "default", "set", "help")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if ("mu".equals(command.name, ignoreCase = true)) {
            if (args.size >= 1) {
                try {
                    when (args[0]) {
                        "reload" -> {
                            Config.loadConfig()
                            sender.sendMessage(ChatColor.YELLOW.toString() + "加载完成")
                        }

                        "default" -> {
                            Config.createDefaultConfig()
                            sender.sendMessage(ChatColor.YELLOW.toString() + "已经恢复为初始配置文件")
                        }

                        "set" -> Config.setConfig(args[1], args[2])
                        "help" -> sender.sendMessage(ChatColor.YELLOW.toString() + "已经恢初始配置文件")
                        else -> {
                            run {}
                            run {
                                Config.loadConfig()
                                sender.sendMessage(ChatColor.YELLOW.toString() + "加载完成")
                            }
                            run {
                                Config.createDefaultConfig()
                                sender.sendMessage(ChatColor.YELLOW.toString() + "已经恢复为初始配置文件")
                            }
                            Config.setConfig(args[1], args[2])
                            sender.sendMessage(ChatColor.YELLOW.toString() + "已经恢初始配置文件")
                        }
                    }
                } catch (e: Exception) {
                    sender.sendMessage(ChatColor.RED.toString() + "加载失败")
                    e.printStackTrace()
                }
                MoreUtils.get().saveConfig()
            } else {
                try {
                    sender.sendMessage(ChatColor.BLUE.toString() + "输入/mu  [指令]")
                } catch (e: Exception) {
                    sender.sendMessage(ChatColor.RED.toString() + "未知错误")
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
        if (args.size > 1) {
            return ArrayList()
        }
        return if (args.size == 0) {
            Arrays.asList(*subCommands)
        } else Arrays.stream(subCommands).filter { s: String -> s.startsWith(args[0]) }.collect(Collectors.toList())
    }
}