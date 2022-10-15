package fyi.sugar.mobstoeggs;

import fyi.sugar.mobstoeggs.data.CreateCapsuleData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    private Main plugin = (Main) Main.getPlugin(Main.class);
    String PV = this.plugin.getDescription().getVersion();

    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
        if ("mte".equalsIgnoreCase(cmd.getName())) {

            if (args.length == 0) {
                sender.sendMessage("" + ChatColor.GRAY + "Mobs To Eggs by Sugarfyi - v" + ChatColor.GRAY);
                sender.sendMessage("" + ChatColor.AQUA + "WEBSITE: www.sugarfyi.com");
                sender.sendMessage("" + ChatColor.AQUA + "SUPPORT: discord.sugar.fyi");
                sender.sendMessage("" + ChatColor.AQUA + "PLUGIN: mte.sugar.fyi");
                sender.sendMessage("" + ChatColor.GRAY + "'/mte help' for all commands");
                return true;
            }

            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                sender.sendMessage(" ");
                sender.sendMessage("" + ChatColor.GRAY + "Mobs To Eggs by Sugarfyi - v" + ChatColor.GRAY);
                sender.sendMessage("" + ChatColor.GRAY + ChatColor.GRAY + " ------------- " + ChatColor.STRIKETHROUGH + ChatColor.AQUA + " Mobs To Eggs " + ChatColor.BOLD + ChatColor.GRAY + " ------------- ");
                sender.sendMessage(" " + ChatColor.WHITE + "Throw chicken eggs at mobs for a chance to catch them!");
                sender.sendMessage(" " + ChatColor.GRAY + "|| " + ChatColor.AQUA + "/mte" + ChatColor.GRAY + " - Show plugin info");
                sender.sendMessage(" " + ChatColor.GRAY + "|| " + ChatColor.AQUA + "/mte help" + ChatColor.GRAY + " - Show this plugin help page");
                sender.sendMessage(" " + ChatColor.GRAY + "|| " + ChatColor.AQUA + "/mte info" + ChatColor.GRAY + " - Show the catch chance and a list of all mobs that can be caught");
                if (sender.hasPermission("mobstoeggs.give")) {

                    sender.sendMessage(" " + ChatColor.GRAY + "|| " + ChatColor.AQUA + "/mte give <amount> <player>" + ChatColor.GRAY + " - Give a specified amount of capsules to the player");
                }
                if (sender.hasPermission("mobstoeggs.bypass")) {

                    sender.sendMessage(" " + ChatColor.GRAY + "|| " + ChatColor.AQUA + "/mte reload" + ChatColor.GRAY + " - Reload the config files");
                }
                sender.sendMessage(" ");
                return true;
            }
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                if (!sender.hasPermission("mobstoeggs.bypass")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + " " + ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")));
                    return true;
                }


                this.plugin.cm.reloadConfigs();
                if (this.plugin.cm.getSettings().getString("cost-type").equalsIgnoreCase("ECONOMY") &&
                        Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
                    System.out.println("MobsToEggs v" + this.plugin.mtev + " - Whoops! You cannot use economy features without installing Vault, cost-type has been set to NONE for safety!");
                    sender.sendMessage("" + ChatColor.RED + "MobsToEggs - Whoops! You cannot use economy features without installing Vault! cost-type has been set to NONE!");
                    this.plugin.cm.getSettings().set("cost-type", "NONE");
                }

                ConfigManager.saveSettings();
                this.plugin.cm.saveMobs();
                this.plugin.cm.saveMessages();
                this.plugin.cm.reloadConfigs();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + " Config files have successfully reloaded!");
                return true;
            }


            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Global chance of catching a mob: &e" + this.plugin.cm.getSettings().getString("global-catch-chance") + "&e%"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Using global catch chance: &e" + this.plugin.cm.getSettings().getBoolean("use-global-values")));
                return true;
            }


            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
                if (!sender.hasPermission("mobstoeggs.give")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + " " + ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")));
                    return true;
                }
                if (args.length == 1) {
                    sender.sendMessage("" + ChatColor.RED + "You are missing parts of the command! Use /mte give <amount> <player>");
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage("" + ChatColor.RED + "You need to enter a playername! Use /mte give <amount> <player>");
                    return true;
                }
                if (args.length == 3) {
                    if (!StringUtils.isNumeric(args[1])) {
                        sender.sendMessage("" + ChatColor.RED + "Amount needs to be before player! Use /mte give <amount> <player>");
                        return true;
                    }
                    int amount = Integer.parseInt(args[1]);
                    Player target = Bukkit.getServer().getPlayer(args[2]);
                    CreateCapsuleData.getCapsule(target, amount);
                    return true;
                }
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + ChatColor.translateAlternateColorCodes('&', this.plugin.cm.getMessages().getString("prefix")) + " That command can't be found, try /mte help");
        return true;
    }
}


