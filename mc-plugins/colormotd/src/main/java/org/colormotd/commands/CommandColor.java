package org.colormotd.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.colormotd.ColorMotd;
import org.colormotd.hook.HookPlaceholder;
import org.colormotd.ReflectFactory;
import org.colormotd.utils.Config;
import org.colormotd.utils.ConfigManager;
import org.colormotd.utils.FaviconList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2020/2/3 16:23
 * @Modified By:
 */
@Getter
public class CommandColor implements TabExecutor {
    private ColorMotd plugin;
    ConfigManager configManager;
    final String prefix = ChatColor.AQUA + "[" + ChatColor.GOLD + "ColorMOTD" + ChatColor.AQUA + "] " + ChatColor.GREEN;
    private String[] subCommands = {"reload", "smode", "emode", "help","test"};

    public CommandColor(ColorMotd colorMotd) {
        this.plugin = colorMotd;
    }
    public ConfigManager configManager() {
        return configManager;
    }

    public Config config() {
        return configManager().getConfig();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //下面是维护模式
        //if ("smode".equals(command.getName())) {
        //    Bukkit.dispatchCommand(sender, "colormotd smode");
        //    return true;
        //}
        //if ("emode".equals(command.getName())) {
        //    Bukkit.dispatchCommand(sender, "colormotd emode");
        //    return true;
        //}
        if ("colormotd".equalsIgnoreCase(command.getName())) {
            if (!sender.hasPermission("colormotd.admin")) {
                sender.sendMessage(prefix + "权限不足");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(prefix + "支持的子命令: smode emode reload");
                return true;
            }

            if ("reload".equals(args[0])) {
                getConfigManager().loadConfig();
                sender.sendMessage(prefix + "重载完成");
                return true;
            }
            if ("smode".equals(args[0])) {
                if (Config.getPluginConfig().isMaintenanceMode()) {
                    Config.getPluginConfig().setMaintenanceMode(false);
                    ConfigManager.getConfigManager().saveConfig();
                    sender.sendMessage(prefix + "维护模式已关闭");
                } else {
                    Config.getPluginConfig().setMaintenanceMode(true);
                    ConfigManager.getConfigManager().saveConfig();
                    for (Player onlinePlayer : ReflectFactory.getPlayers()) {
                        if (!onlinePlayer.isOp() && !onlinePlayer.hasPermission("colormotd.maintenance.join")) {
                            onlinePlayer.kickPlayer(HookPlaceholder.getPlaceHolder().applyPlaceHolder(Config.getPluginConfig().getMaintenanceModeKickMsg(), onlinePlayer.getAddress().getHostString()));
                        }
                    }
                    sender.sendMessage(prefix + "维护模式已开启，所有非OP和无colormotd.maintenance.join权限玩家已被移出服务器并在关闭前无法加入服务器");
                }
                return true;
            }
            if ("emode".equals(args[0])) {
               sender.sendMessage("维护模式"+ Config.getPluginConfig().isEmergencyMode());
                if (Config.getPluginConfig().isEmergencyMode()) {
                    Config.getPluginConfig().setEmergencyMode(false);
                    ConfigManager.getConfigManager().saveConfig();
                    sender.sendMessage(prefix + "应急模式已关闭");
                } else {
                    Config.getPluginConfig().setEmergencyMode(true);
                    ConfigManager.getConfigManager().saveConfig();
                    sender.sendMessage(prefix + "应急模式已开启，现在将忽略所有MOTD请求");
                }
                return true;
            }
            if("test".equalsIgnoreCase(args[0])){
                sender.sendMessage("维护模式"+ config().isEmergencyMode());
            }
        }

        sender.sendMessage(prefix + "支持的子命令: smode emode reload");
        return true;
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
