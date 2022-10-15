package com.zettelnet.levelhearts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class LevelHeartsTabCompleter implements TabCompleter {

    private final LevelHeartsPermissions perm;

    public LevelHeartsTabCompleter() {
        this.perm = LevelHearts.getPermissionManager();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        String toComplete = args.length == 0 ? "" : args[args.length - 1];
        switch (cmd.getName().toLowerCase()) {
            case "health":
                if (!perm.hasCommandHealthEnabled(sender)) {
                    return null;
                }
                switch (args.length) {
                    case 0:
                    case 1:
                        List<String> options = new ArrayList<>(6);
                        options.add("help");
                        options.add("get");
                        if (perm.hasCommandHealthSetOwn(sender)) {
                            options.add("set");
                            options.add("give");
                            options.add("take");
                            options.add("restore");
                        }
                        return options;
                    case 2:
                        if (!perm.hasCommandHealthGetOthers(sender)) {
                            return null;
                        }
                        switch (args[0]) {
                            case "help":
                            case "get":
                                break;
                            case "set":
                            case "change":
                            case "give":
                            case "add":
                            case "take":
                            case "remove":
                            case "rem":
                            case "substract":
                            case "restore":
                            case "reset":
                            case "replenish":
                            case "fill":
                            case "full":
                            case "complete":
                                if (!perm.hasCommandHealthSetOthers(sender)) {
                                    return null;
                                }
                                break;
                            default:
                                return null;
                        }
                        options = new ArrayList<>(Bukkit.getOnlinePlayers().size());
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().startsWith(toComplete)) {
                                options.add(player.getName());
                            }
                        }
                        return options;
                }
            case "maxhealth":
                if (!perm.hasCommandMaxHealthEnabled(sender)) {
                    return null;
                }
                switch (args.length) {
                    case 0:
                    case 1:
                        List<String> options = new ArrayList<>(6);
                        options.add("help");
                        options.add("get");
                        if (perm.hasCommandMaxHealthSetOwn(sender)) {
                            options.add("set");
                            options.add("give");
                            options.add("take");
                            options.add("reset");
                        }
                        return options;
                    case 2:
                        if (!perm.hasCommandMaxHealthGetOthers(sender)) {
                            return null;
                        }
                        switch (args[0]) {
                            case "help":
                            case "get":
                                break;
                            case "set":
                            case "change":
                            case "give":
                            case "add":
                            case "take":
                            case "remove":
                            case "rem":
                            case "substract":
                            case "reset":
                            case "restore":
                            case "replenish":
                            case "recalculate":
                            case "recalc":
                                if (!perm.hasCommandMaxHealthSetOthers(sender)) {
                                    return null;
                                }
                                break;
                            default:
                                return null;
                        }
                        options = new ArrayList<>(Bukkit.getOnlinePlayers().size());
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().startsWith(toComplete)) {
                                options.add(player.getName());
                            }
                        }
                        return options;
                }
            case "levelhearts":
                if (!perm.hasCommandInfoEnabled(sender)) {
                    return null;
                }
                List<String> options = new ArrayList<>(2);
                options.add("help");
                if (perm.hasCommandInfoReload(sender)) {
                    options.add("reload");
                }
                return options;
            default:
                return null;
        }
    }
}
