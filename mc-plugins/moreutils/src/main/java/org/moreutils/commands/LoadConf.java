package org.moreutils.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moreutils.MoreUtils;
import org.moreutils.utils.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 1:39
 * @Modified By:
 */
public class LoadConf implements TabExecutor {
    private MoreUtils moreUtils;

    public LoadConf(MoreUtils plug) {
        this.moreUtils = plug;
    }

    private String[] subCommands = {"reload","default","set","help"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, @NotNull String[] args) {
        if ("mu".equalsIgnoreCase(command.getName())) {
            if (args.length >= 1) {

                try {
                    switch (args[0]) {
                        default -> {
                        }
                        case "reload" -> {
                            Config.loadConfig();
                            sender.sendMessage(ChatColor.YELLOW + "加载完成");
                        }
                        case "default" -> {
                            Config.createDefaultConfig();
                            sender.sendMessage(ChatColor.YELLOW + "已经恢复为初始配置文件");
                        }
                        case "set" -> Config.setConfig(args[1], args[2]);
                        case "help" -> sender.sendMessage(ChatColor.YELLOW + "已经恢初始配置文件");
                    }


                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "加载失败");
                    e.printStackTrace();
                }


                MoreUtils.getInstance().saveConfig();

            } else {
                try {
                    sender.sendMessage(ChatColor.BLUE + "输入/mu  [指令]");
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "未知错误");
                    e.printStackTrace();
                }

            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length > 1) {
            return new ArrayList<>();
        }
        if (args.length == 0) {
            return Arrays.asList(subCommands);
        }
        return Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }
}
