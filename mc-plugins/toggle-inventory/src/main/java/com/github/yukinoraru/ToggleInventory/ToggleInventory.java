 package com.github.yukinoraru.ToggleInventory;

 import java.io.File;
 import java.io.IOException;
 import java.util.logging.Logger;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;
 import org.bukkit.event.Listener;
 import org.bukkit.plugin.Plugin;
 import org.bukkit.plugin.java.JavaPlugin;

 public class ToggleInventory
   extends JavaPlugin implements Listener {
   protected Logger log;
   protected UpdateChecker updateChecker;
   protected InventoryManager inventoryManager;

   @Override
   public void onEnable() {
     this.log = getLogger();

     saveDefaultConfig();


     getConfig().options().copyDefaults(true);
     saveConfig();


     if (getConfig().getBoolean("update-check", true)) {
       this.updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/server-mods/toggleinventory/files.rss");
       if (this.updateChecker.updateNeeded()) {
         this.log.info("A new version is available: v." + this.updateChecker.getVersion());
         this.log.info("Get it from: " + this.updateChecker.getLink());
       }
     } else {

       this.log.info("Update check was skipped.");
     }

     this.inventoryManager = new InventoryManager(this);


     File spInvFile = this.inventoryManager.getDefaultSpecialInventoryFile();
     if (!spInvFile.exists()) {
       saveResource(spInvFile.getName(), false);
     }



   }





   @Override
   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
     Player player = null;
     String playerName = null;
     if (sender instanceof Player) {
       player = (Player)sender;
       playerName = player.getName();
     } else {
       sender.sendMessage("You must be a player!");
       return false;
     }


     boolean isToggleInvCommand = cmd.getName().equalsIgnoreCase("togglei");
     boolean isReverse = cmd.getName().equalsIgnoreCase("toggleir");

     if (isToggleInvCommand || isReverse) {
       if (!player.hasPermission("toggle_inventory.toggle")) {
         outputError("You don't have permission to toggle inventory.", (CommandSender)player);
         return true;
       }


       if (args.length >= 1 && args[0].length() > 0 &&
         args[0].startsWith("h")) {
         player.sendMessage("USAGE1: /ti - toggle inventory like a ring");
         player.sendMessage("USAGE2: /it - toggle inventory like a ring (reverse)");
         player.sendMessage("Advanced: /ti [enable|disable] gamemode - (you can toggle with gamemode)");
         return true;
       }



       if (args.length >= 2 && args[0].length() > 0 && args[1].length() > 0) {
         try {
           if (args[0].startsWith("e")) {
             if (args[1].startsWith("g")) {
               this.inventoryManager.setGameModeSaving(playerName, true);
               player.sendMessage("[ToggleInventory] Game mode toggle is enabled.");
             }
             return true;
           }  if (args[0].startsWith("d")) {
             if (args[1].startsWith("g")) {
               this.inventoryManager.setGameModeSaving(playerName, false);
               player.sendMessage("[ToggleInventory] Game mode toggle is disabled.");
             }
             return true;
           }
         } catch (IOException e) {
           outputError("Something went wrong! (gamemode enable option)", (CommandSender)player);
         }
       }


       try {
         if (this.inventoryManager.getSpecialInventoryUsingStatus(playerName)) {
           this.inventoryManager.restoreInventory((CommandSender)player);
           this.inventoryManager.setSpecialInventoryUsingStatus(playerName, false);
         } else if (args.length >= 1 && args[0].length() > 0) {
           int index = Integer.parseInt(args[0]);
           this.inventoryManager.toggleInventory((CommandSender)player, index);
         } else {

           this.inventoryManager.toggleInventory((CommandSender)player, !isReverse);
         }
         player.sendMessage(this.inventoryManager.makeInventoryMessage((CommandSender)player) + " inventory toggled.");
       } catch (NumberFormatException e) {
         outputError("Index must be a number.", (CommandSender)player);
       } catch (Exception e) {
         outputError(e.getMessage(), (CommandSender)player);
       }
       return true;
     }


     boolean isTogglelInvSpecialCommand = cmd.getName().equalsIgnoreCase("toggleis");
     boolean isTISReverse = cmd.getName().equalsIgnoreCase("toggleisr");
     if (isTogglelInvSpecialCommand || isTISReverse) {
       if (!player.hasPermission("toggle_inventory.toggle_special")) {
         outputError("You don't have permission to toggle special inventories.", (CommandSender)player);
         return true;
       }

       try {
         if (args.length >= 1 && (args[0].equals("add") || args[0].equals("delete"))) {
           String name = (args.length == 2) ? args[1] : null;
           if (name == null) {
             player.sendMessage("USAGE: /tis [add|delete] [name]");
             return true;
           }
           if (args[0].equals("add")) {
             this.inventoryManager.saveSpecialInventory(player, name);
             player.sendMessage(ChatColor.DARK_GREEN + String.format("Add %s to special inventories.", new Object[] { ChatColor.GREEN + name + ChatColor.DARK_GREEN }));
           }
           else if (args[0].equals("delete")) {
             this.inventoryManager.deleteSpecialInventory(this.inventoryManager.getInventoryFile(playerName), name);
             player.sendMessage(ChatColor.DARK_GREEN + String.format("Delete %s from special inventories.", new Object[] { ChatColor.GREEN + name + ChatColor.DARK_GREEN }));
           }
           return true;
         }

         if (args.length >= 1 && (args[0].equals("add-default") || args[0].equals("delete-default"))) {
           String name = (args.length == 2) ? args[1] : null;
           if (name == null) {
             player.sendMessage("USAGE: /tis [add-deafult|delete-default] [name]");
             return true;
           }
           if (args[0].equals("add-default")) {
             this.inventoryManager.saveSpecialInventory(player, name);
             player.sendMessage(ChatColor.DARK_GREEN + String.format("Add %s to default special inventories.", new Object[] { ChatColor.GREEN + name + ChatColor.DARK_GREEN }));
           }
           else if (args[0].equals("delete-default")) {
             this.inventoryManager.deleteSpecialInventory(this.inventoryManager.getDefaultSpecialInventoryFile(), name);
             player.sendMessage(ChatColor.DARK_GREEN + String.format("Delete %s from default special inventories.", new Object[] { ChatColor.GREEN + name + ChatColor.DARK_GREEN }));
           }
           return true;
         }

         if (args.length >= 1 && args[0].equals("copy")) {
           int destinationIndex = (args.length == 3) ? Integer.parseInt(args[2]) : -1;
           String spInvName = (args.length == 3) ? args[1] : null;
           if (destinationIndex > 0) {
             this.inventoryManager.copySpInvToNormalInventory((CommandSender)player, spInvName, destinationIndex);
             if (destinationIndex == this.inventoryManager.getCurrentInventoryIndex(playerName)) {
               this.inventoryManager.restoreInventory((CommandSender)player);
             }
             player.sendMessage(String.format("'%s' was copied to '%s' successfully!", new Object[] { ChatColor.GREEN + spInvName + ChatColor.RESET, ChatColor.BOLD + String.valueOf(destinationIndex) + ChatColor.RESET }));





             return true;
           }

           player.sendMessage("USAGE: /tis copy [special inventory name] [invenotry index]");

           return true;
         }

         if (args.length >= 1 && args[0].equals("reset")) {
           boolean isForce = (args.length == 2) ? args[1].equals("-f") : false;
           if (isForce) {
             this.inventoryManager.initializeSPInvFromDefault(playerName);
             player.sendMessage(ChatColor.GOLD + "All special inventory were reset!");
             return true;
           }

           player.sendMessage(ChatColor.GOLD + "WARNING: All special inventory will be reset by default.");
           player.sendMessage(ChatColor.GOLD + "If you want to continue operation, retype " + ChatColor.DARK_RED + "'/tis reset -f'");

           return true;
         }

         if (args.length >= 1 && args[0].equals("reset-default")) {
           boolean isForce = (args.length == 2) ? args[1].equals("-f") : false;
           if (isForce) {
             saveResource(this.inventoryManager.getDefaultSpecialInventoryFile().getName(), true);
             player.sendMessage(ChatColor.GOLD + "Default special inventory were reset!");
             return true;
           }

           player.sendMessage(ChatColor.GOLD + "WARNING: Default special inventory will be reset by default.");
           player.sendMessage(ChatColor.GOLD + "If you want to continue operation, retype " + ChatColor.DARK_RED + "'/tis reset-default -f'");

           return true;
         }




         if (this.inventoryManager.isFirstUseForToggleInventorySpecial(playerName)) {
           this.inventoryManager.initializeSPInvFromDefault(playerName);
           this.inventoryManager.setSpecialInventoryUsingStatusForFirstUse(playerName, false);
         }
         if (args.length == 1 && args[0].length() > 0) {
           this.inventoryManager.toggleSpecialInventory((CommandSender)player, args[0]);
         } else {
           this.inventoryManager.toggleSpecialInventory((CommandSender)player, !isTISReverse);
         }

         player.sendMessage(this.inventoryManager.makeSpecialInventoryMessage((CommandSender)player) + " inventory toggled.");
       } catch (Exception e) {
         outputError(e.getMessage(), (CommandSender)player);
         e.printStackTrace();
       }
     }
     return true;
   }


   @Override
   public void onDisable() {}


   private void outputError(String msg) { getLogger().warning(msg); }


   private void outputError(String msg, CommandSender sender) {
     sender.sendMessage(ChatColor.RED + "[ERROR] " + msg);
     outputError(msg);
   }
 }


/* Location:              D:\Desktop\ToggleInventory 1.6.4.jar!\com\github\yukinoraru\ToggleInventory\ToggleInventory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */