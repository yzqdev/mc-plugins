package org.health;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.health.Updater;

public class Commands implements CommandExecutor {
    private static final String PREFIX = "§2[§aHealthBar§2] ";
    public Main instance;

    public Commands(Main main) {
        instance = main;
    }

    private void noPermissionMessage(CommandSender sender) {
        sender.sendMessage("§cYou don't have permission.");
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendInfo(sender);

            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sendCommandList(sender);

            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfigs(sender);

            return true;
        }

        if (args[0].equalsIgnoreCase("update")) {
            if (!sender.hasPermission("healthbar.update")) {
                noPermissionMessage(sender);

                return true;
            }

            Thread updaterThread = new Thread(() -> Updater.UpdaterHandler.manuallyCheckUpdates(sender));

            updaterThread.start();

            return true;
        }

        sender.sendMessage(PREFIX + "§eUnknown command. Type §a" + label + " §efor help.");

        return true;
    }

    private void reloadConfigs(CommandSender sender) {
        if (!sender.hasPermission("healthbar.reload")) {
            noPermissionMessage(sender);

            return;
        }

        try {
            instance.reloadConfigFromDisk();
            sender.sendMessage("§e>>§6 HealthBar reloaded");
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage("§cFailed to reload configs, take a look at the console!");
        }
    }

    private void sendCommandList(CommandSender sender) {
        if (!sender.hasPermission("healthbar.help")) {
            noPermissionMessage(sender);

            return;
        }

        sender.sendMessage("§e>>§6 HealthBar commands: ");
        sender.sendMessage("§2/hbr §7- §aDisplays general plugin info");
        sender.sendMessage("§2/hbr reload §7- §aReloads the configs");
        sender.sendMessage("§2/hbr update §7- §aChecks for updates");
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage(PREFIX);
        sender.sendMessage("§aVersion: §7" + instance.getDescription().getVersion());
        sender.sendMessage("§aDeveloper: §7filoghost");
        sender.sendMessage("§aCommands: §7/hbr help");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
