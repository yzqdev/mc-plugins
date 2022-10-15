package org.moreutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moreutils.MoreUtils;
import org.moreutils.utils.HpUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getLogger;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/25 19:28
 * @Modified By:
 */
public class Hp implements TabExecutor {
    private MoreUtils moreUtils;

    public Hp(MoreUtils plug) {
        this.moreUtils = plug;
    }

    private String[] subCommands = {"setall", "set"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String commandLabel, @NotNull String[] args) {
        if ("hp".equalsIgnoreCase(command.getName())) {
            if (args.length >= 1) {

                try {
                    switch (args[0]) {
                        default -> {
                        }
                        case "setall" -> {
                            MoreUtils.getInstance().getConfig().set("player-max-health", Integer.parseInt(args[1]));
                            HpUtils.setDefaultHp(args[1]);
                            sender.sendMessage(ChatColor.YELLOW + "设置所有玩家血量为" + args[1]);
                        }
                        case "set" -> {
                            Player player = Bukkit.getServer().getPlayer(args[1]);
                            assert player != null;
                            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(Double.parseDouble(args[2]));
                            sender.sendMessage(ChatColor.YELLOW + "设置玩家" + player.getName() + "当前血量为   " + args[2]);
                        }
                    }
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "血量必须是数字!");
                    e.printStackTrace();
                }


                MoreUtils.getInstance().saveConfig();

            } else {
                try {
                    sender.sendMessage(ChatColor.BLUE + "输入/hp setall [血量]");
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "血量必须是数字!");
                    e.printStackTrace();
                }

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
