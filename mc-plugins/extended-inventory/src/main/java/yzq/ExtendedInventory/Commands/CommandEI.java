package yzq.ExtendedInventory.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yzq.ExtendedInventory.ExtendedInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import yzq.ExtendedInventory.utils.Config;
import yzq.ExtendedInventory.utils.StringGet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yanni
 */
public class CommandEI implements TabExecutor {
    private ExtendedInventory plugin;

    public CommandEI(ExtendedInventory plug) {
        this.plugin = plug;
    }

    private String[] subCommands = {"reload", "default", "set", "help"};


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        boolean updateNeeded = false;
        if ("ei".equalsIgnoreCase(cmd.getName())) {


            if (args.length >= 1) {
                try {
                    switch (args[0]) {
                        default:
                            break;
                        case "reload":
                            Config.reload();
                            sender.sendMessage(ChatColor.YELLOW + "加载完成");
                            break;
                        case "help":
                            sender.sendMessage(StringGet.getPrefix() + " §9§m-----------§9| §6ExtendedInventory §9|§m-----------");
                            sender.sendMessage(StringGet.getPrefix() + " §3Author: §f" + this.plugin.getDescription().getName());
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage(StringGet.getPrefix() + " §9§m-----------§9| §6ExtendedInventory §9|§m-----------");
                //sender.sendMessage(StringGet.getPrefix() + " §3Author: §f" + this.plugin.getDescription().getAuthors().get(0));
                if (updateNeeded) {
                    sender.sendMessage(
                            StringGet.getPrefix() + " §3Version: §f" + this.plugin.getDescription().getVersion() + " §4(Outdated)");
                } else {
                    sender.sendMessage(
                            StringGet.getPrefix() + " §3Version: §f" + this.plugin.getDescription().getVersion() + " §2(Newest)");
                }
                sender.sendMessage(String.valueOf(StringGet.getPrefix()) + " §9§m-----------§9| §6ExtendedInventory §9|§m-----------");
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


