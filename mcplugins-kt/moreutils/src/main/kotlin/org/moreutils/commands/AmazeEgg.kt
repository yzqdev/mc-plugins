package org.moreutils.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.moreutils.MoreUtils
import java.util.*
import java.util.stream.Collectors

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 12:06
 * @Modified By:
 */
class AmazeEgg(private val moreUtils: MoreUtils) : TabExecutor {
    var subCommands = arrayOf("add", "set", "remove")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if ("toegg".equals(command.name, ignoreCase = true)) {
            if ("set" == args[0]) {
                MoreUtils.get().getConfig().set("to-egg-chance", args[1].toInt())
                MoreUtils.get().saveConfig()
                MoreUtils.get().reloadConfig()
                sender.sendMessage(ChatColor.YELLOW.toString() + "设置刷蛋成功概率为" + args[1])
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