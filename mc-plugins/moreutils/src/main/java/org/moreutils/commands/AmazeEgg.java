package org.moreutils.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moreutils.MoreUtils;
import org.moreutils.utils.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 12:06
 * @Modified By:
 */
public class AmazeEgg implements TabExecutor {
    String[] subCommands = {"add", "set", "remove"};
    private final MoreUtils moreUtils;

    public AmazeEgg(MoreUtils plug) {
        this.moreUtils = plug;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ("toegg".equalsIgnoreCase(command.getName())) {
            if ("set".equals(args[0])) {
                MoreUtils.getInstance().getConfig().set("to-egg-chance", Integer.parseInt(args[1]));
                MoreUtils.getInstance().saveConfig();
                MoreUtils.getInstance().reloadConfig();
                sender.sendMessage(ChatColor.YELLOW + "设置刷蛋成功概率为" + args[1]);
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            return Arrays.asList(subCommands);
        }
        return Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }
}
