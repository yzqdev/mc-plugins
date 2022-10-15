package com.github.yukinoraru.ToggleInventory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;


public class InventoryManager {
    private static final int CONFIG_INVENTORY_MAX_INDEX_DEFAULT = 4;
    private static final int CONFIG_INVENTORY_MAXIMUM = 30;
    public static final String CONFIG_FILENAME_SPECIAL_INV = "special_inventories.yml";
    private ToggleInventory plugin;

    public InventoryManager(ToggleInventory plugin) {
        this.plugin = plugin;
    }


    public File getInventoryFile(String playerName) {
        String parentPath = this.plugin.getDataFolder() + File.separator + "players";
        String childPath = playerName + ".yml";
        File f = new File(parentPath);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
        return new File(parentPath, childPath);
    }


    public File getDefaultSpecialInventoryFile() {
        return new File(this.plugin.getDataFolder(), "special_inventories.yml");
    }


    private void prepareFile(File file) throws Exception {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (Exception e) {
            throw e;
        }
    }

    private int getMaxInventoryIndex(CommandSender player) {
        int max = -1;
        for (int i = 2; i <= 30; i++) {
            String permissionPath = String.format("toggle_inventory.%d", new Object[]{Integer.valueOf(i)});

            if (player.hasPermission(permissionPath)) {
                max = i;
            }
        }
        return (max <= 1) ? 4 : max;
    }


    public int calcNextInventoryIndex(int maxIndex, int currentIndex, boolean rotateDirection) {
        int nextIndex;
        if (rotateDirection) {
            nextIndex = (currentIndex + 1 > maxIndex) ? 1 : (currentIndex + 1);
        } else {

            nextIndex = (currentIndex - 1 <= 0) ? maxIndex : (currentIndex - 1);
        }
        return nextIndex;
    }

    private FileConfiguration getPlayersFileConfiguration(String playerName) {
        File file = getInventoryFile(playerName);
        return (FileConfiguration) YamlConfiguration.loadConfiguration(file);
    }


    private void setPlayerConfig(String playerName, String section, Object obj) throws IOException {
        File file = getInventoryFile(playerName);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set(section, obj);
        yamlConfiguration.save(file);
    }


    public String makeInventoryMessage(CommandSender player) {
        String msg = ChatColor.GRAY + "[";
        String playerName = player.getName();
        int invCurrentIndex = getCurrentInventoryIndex(playerName);
        int maxIndex = getMaxInventoryIndex(player);

        for (int i = 1; i <= maxIndex; i++) {
            msg = msg + ((i == invCurrentIndex) ? ChatColor.WHITE :  ChatColor.GRAY) + Integer.toString(i);
            msg = msg + ChatColor.RESET + " ";
        }
        return msg + ChatColor.GRAY + "] ";
    }


    public String makeSpecialInventoryMessage(CommandSender player) throws Exception {
        String msg = ChatColor.GRAY + "[";

        String playerName = player.getName();
        String invCurrentName = getCurrentSpecialInventoryIndex(playerName);
        String[] list = getListSpecialInventory(getInventoryFile(playerName));

        for (int i = 0; i < list.length; i++) {
            msg = msg + (list[i].equals(invCurrentName) ? ChatColor.GREEN : ChatColor.DARK_GREEN) + list[i];
            msg = msg + ChatColor.RESET + " ";
        }
        return msg + ChatColor.GRAY + "] ";
    }

    private String[] getListSpecialInventory(File specialInventoryFile) throws Exception {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(specialInventoryFile);
        try {
            Set<String> nameList = yamlConfiguration.getConfigurationSection("special_inventories").getKeys(false);
            String[] tmp = nameList.toArray(new String[0]);
            if (tmp.length == 0) {
                throw new Exception();
            }
            return tmp;
        } catch (Exception e) {
            return null;
        }
    }

    public String getNextSpecialInventory(String[] list, String name, boolean rotateDirection) {
        String nextInvName = null;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(name)) {
                if (rotateDirection) {
                    if (i + 1 < list.length) {
                        nextInvName = list[i + 1];
                        break;
                    }
                    nextInvName = list[0];
                    break;
                }
                if (i - 1 >= 0) {
                    nextInvName = list[i - 1];
                    break;
                }
                nextInvName = list[list.length - 1];

                break;
            }
        }

        return (nextInvName == null) ? list[0] : nextInvName;
    }

    private boolean isExistSpecialInv(String playerName, String specialInventoryName) throws Exception {
        String[] list = getListSpecialInventory(getInventoryFile(playerName));
        boolean isMatched = false;
        for (String name : list) {
            if (name.equals(specialInventoryName)) {
                isMatched = true;
                break;
            }
        }
        return isMatched;
    }


    public void saveInventory(Player player, String index, boolean isSpecialInventory) throws Exception {
        String sectionPathPotion, sectionPathGameMode, sectionPathArmor, sectionPathContents;
        PlayerInventory inventory = player.getInventory();
        Inventory inventoryArmor = InventoryUtils.getArmorInventory(inventory);


        String serializedInventoryContents = InventoryUtils.inventoryToString((Inventory) inventory);
        String serializedInventoryArmor = InventoryUtils.inventoryToString(inventoryArmor);
        String serializedPotion = PotionUtils.serializePotion(player.getActivePotionEffects());


        if (isSpecialInventory) {
            sectionPathContents = getSectionPathForSPInvContents(index);
            sectionPathArmor = getSectionPathForSPInvArmor(index);
            sectionPathGameMode = getSectionPathForSPInvGameMode(index);
            sectionPathPotion = getSectionPathForSPInvPotion(index);
        } else {

            int tmp = Integer.parseInt(index);
            sectionPathContents = getSectionPathForUserContents(tmp);
            sectionPathArmor = getSectionPathForUserArmor(tmp);
            sectionPathGameMode = getSectionPathForUserInvGameMode(tmp);
            sectionPathPotion = getSectionPathForUserInvPotion(tmp);
        }


        File inventoryFile = getInventoryFile(player.getName());
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(inventoryFile);

        yamlConfiguration.set(sectionPathContents, serializedInventoryContents);
        yamlConfiguration.set(sectionPathArmor, serializedInventoryArmor);
        yamlConfiguration.set(sectionPathGameMode, player.getGameMode().name());
        yamlConfiguration.set(sectionPathPotion, serializedPotion);

        prepareFile(inventoryFile);

        yamlConfiguration.save(inventoryFile);
    }


    private void loadInventory(Player player, String index, boolean isSpecialInventory) {
        String sectionPathPotion, sectionPathGameMode, sectionPathArmor, sectionPathContents;
        File inventoryFile = getInventoryFile(player.getName());
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(inventoryFile);


        if (isSpecialInventory) {
            sectionPathContents = getSectionPathForSPInvContents(index);
            sectionPathArmor = getSectionPathForSPInvArmor(index);
            sectionPathGameMode = getSectionPathForSPInvGameMode(index);
            sectionPathPotion = getSectionPathForSPInvPotion(index);
        } else {

            int tmp = Integer.parseInt(index);
            sectionPathContents = getSectionPathForUserContents(tmp);
            sectionPathArmor = getSectionPathForUserArmor(tmp);
            sectionPathGameMode = getSectionPathForUserInvGameMode(tmp);
            sectionPathPotion = getSectionPathForUserInvPotion(tmp);
        }


        String serializedInventoryContents = yamlConfiguration.getString(sectionPathContents);
        String serializedInventoryArmor = yamlConfiguration.getString(sectionPathArmor);

        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);

        if (serializedInventoryContents != null) {
            Inventory deserializedInventoryNormal = InventoryUtils.stringToInventory(serializedInventoryContents);

            int i = 0;
            for (ItemStack item : deserializedInventoryNormal.getContents()) {
                i++;
                if (item != null) {

                    inventory.setItem(i - 1, item);
                }
            }
        }
        if (serializedInventoryArmor != null) {
            Inventory deserialized_inv_armor = InventoryUtils.stringToInventory(serializedInventoryArmor);
            ItemStack[] tmp = deserialized_inv_armor.getContents();
            if (tmp != null) {
                inventory.setArmorContents(tmp);
            }
        }


        if (isEnableGameModeSaving(player.getName())) {
            String gamemode = yamlConfiguration.getString(sectionPathGameMode);
            if (gamemode != null && gamemode.length() > 0) {
                player.setGameMode(GameMode.valueOf(gamemode));
            }
        }


        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        String effectsInString = yamlConfiguration.getString(sectionPathPotion);
        try {
            player.addPotionEffects(PotionUtils.deserializePotion(effectsInString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void toggleInventory(CommandSender player, boolean rotateDirection) throws Exception {
        String playerName = player.getName();

        int maxIndex = getMaxInventoryIndex(player);
        int currentIndex = getCurrentInventoryIndex(playerName);
        int nextIndex = calcNextInventoryIndex(maxIndex, currentIndex, rotateDirection);

        toggleInventory(player, nextIndex);
    }


    public void toggleInventory(CommandSender player, int index) throws Exception {
        String playerName = player.getName();

        int maxIndex = getMaxInventoryIndex(player);
        int currentIndex = getCurrentInventoryIndex(playerName);
        int nextIndex = index;


        if (nextIndex > maxIndex) {
            throw new IndexOutOfBoundsException(String.format("Max inventory index is %d", new Object[]{Integer.valueOf(maxIndex)}));
        }
        if (nextIndex <= 0) {
            throw new Exception("Inventory index is wrong.");
        }
        if (currentIndex == nextIndex) {
            throw new Exception("It's current inventory.");
        }


        saveInventory((Player) player, String.valueOf(currentIndex), false);
        loadInventory((Player) player, String.valueOf(nextIndex), false);


        setCurrentInventoryIndex(playerName, nextIndex);
    }


    public void toggleSpecialInventory(CommandSender player, String inventoryName) throws Exception {
        String playerName = player.getName();
        if (!getSpecialInventoryUsingStatus(playerName)) {
            int currentIndex = getCurrentInventoryIndex(playerName);
            saveInventory((Player) player, String.valueOf(currentIndex), false);
        }


        String[] list = getListSpecialInventory(getInventoryFile(playerName));
        if (list == null) {
            throw new Exception("Your special inventory is empty.\n Try '/tis add' or '/tis reset -f', '/tis reset-default -f'");
        }
        int index = LevenshteinDistance.find(list, inventoryName);
        String specialInvName = list[index];


        loadSpecialInventory((Player) player, specialInvName);


        setCurrentSpecialInventoryIndex(playerName, specialInvName);


        setSpecialInventoryUsingStatus(playerName, true);
    }

    public void toggleSpecialInventory(CommandSender player, boolean rotateDirection) throws Exception {
        String playerName = player.getName();
        String currentSpIndex = getCurrentSpecialInventoryIndex(playerName);
        String[] list = getListSpecialInventory(getInventoryFile(playerName));
        try {
            String nextSpIndex = getSpecialInventoryUsingStatus(playerName) ? getNextSpecialInventory(list, currentSpIndex, rotateDirection) : currentSpIndex;

            toggleSpecialInventory(player, nextSpIndex);
        } catch (NullPointerException e) {

            toggleSpecialInventory(player, null);
        }
    }


    public void initializeSPInvFromDefault(String playerName) throws Exception {
        deleteAllSPInventoryFromUser(playerName);


        File defaultFile = getDefaultSpecialInventoryFile();
        File playerFile = getInventoryFile(playerName);
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(defaultFile);
        YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration(playerFile);

        String[] defaultSPInvList = getListSpecialInventory(defaultFile);

        if (defaultSPInvList == null) {
            throw new Exception("There are no default special inventories in special_inventories.yml. Please check it.");
        }


        for (String name : defaultSPInvList) {
            yamlConfiguration2.set(getSectionPathForSPInvContents(name), yamlConfiguration1.get(getSectionPathForSPInvContents(name)));
            yamlConfiguration2.set(getSectionPathForSPInvArmor(name), yamlConfiguration1.get(getSectionPathForSPInvArmor(name)));
            yamlConfiguration2.set(getSectionPathForSPInvPotion(name), yamlConfiguration1.get(getSectionPathForSPInvPotion(name)));
            yamlConfiguration2.set(getSectionPathForSPInvGameMode(name), yamlConfiguration1.get(getSectionPathForSPInvGameMode(name)));
        }

        yamlConfiguration2.save(playerFile);
    }


    public void copySpInvToNormalInventory(CommandSender player, String specialInventoryName, int destinationIndex) throws Exception {
        String playerName = player.getName();


        int maxIndex = getMaxInventoryIndex(player);
        if (destinationIndex <= 0 || destinationIndex > maxIndex) {
            throw new Exception("Wrong destination index.");
        }


        if (!isExistSpecialInv(playerName, specialInventoryName)) {
            throw new Exception(String.format("No such special inventory found: '%s'", new Object[]{specialInventoryName}));
        }

        File playerInventoryFile = getInventoryFile(playerName);
        File specialInventoryFile = getInventoryFile(playerName);
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(playerInventoryFile);
        YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration(specialInventoryFile);


        String playerSectionPathContents = getSectionPathForUserContents(destinationIndex);
        String playerSectionPathArmor = getSectionPathForUserArmor(destinationIndex);
        String playerSectionPathPotion = getSectionPathForUserInvPotion(destinationIndex);
        String playerSectionPathGameMode = getSectionPathForUserInvGameMode(destinationIndex);


        String spinvSectionPathContents = getSectionPathForSPInvContents(specialInventoryName);
        String spinvSectionPathArmor = getSectionPathForSPInvArmor(specialInventoryName);
        String spinvSectionPathPotion = getSectionPathForSPInvPotion(specialInventoryName);
        String spinvSectionPathGameMode = getSectionPathForSPInvGameMode(specialInventoryName);


        yamlConfiguration1.set(playerSectionPathContents, yamlConfiguration2.get(spinvSectionPathContents));
        yamlConfiguration1.set(playerSectionPathArmor, yamlConfiguration2.get(spinvSectionPathArmor));
        yamlConfiguration1.set(playerSectionPathPotion, yamlConfiguration2.get(spinvSectionPathPotion));
        yamlConfiguration1.set(playerSectionPathGameMode, yamlConfiguration2.get(spinvSectionPathGameMode));

        yamlConfiguration1.save(playerInventoryFile);
    }


    public void restoreInventory(CommandSender player) {
        int index = getCurrentInventoryIndex(((Player) player).getName());
        loadInventory((Player) player, index);
    }

    public void deleteSpecialInventory(File file, String name) throws IOException {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        this.plugin.getLogger().warning(getSectionPathForSPInvRoot(name));
        yamlConfiguration.set(getSectionPathForSPInvRoot(name), null);
        yamlConfiguration.save(file);
    }

    private void deleteAllSPInventoryFromUser(String playerName) throws Exception {
        File playerFile = getInventoryFile(playerName);
        String[] playerSPInvList = getListSpecialInventory(playerFile);
        if (playerSPInvList != null) {
            for (String name : playerSPInvList) {
                deleteSpecialInventory(playerFile, name);
            }
        }
    }


    public void saveSpecialInventory(Player player, String name) throws Exception {
        saveInventory(player, name, true);
    }


    private void loadInventory(Player player, int index) {
        loadInventory(player, String.valueOf(index), false);
    }


    private void loadSpecialInventory(Player player, String name) {
        loadInventory(player, name, true);
    }


    public boolean getSpecialInventoryUsingStatus(String playerName) {
        return getPlayersFileConfiguration(playerName).getBoolean("sp_using");
    }


    public void setSpecialInventoryUsingStatus(String playerName, boolean isUsing) throws IOException {
        setPlayerConfig(playerName, "sp_using", Boolean.valueOf(isUsing));
    }


    private String getCurrentSpecialInventoryIndex(String playerName) {
        return getPlayersFileConfiguration(playerName).getString("sp_current", "");
    }


    private void setCurrentSpecialInventoryIndex(String playerName, String name) throws IOException {
        setPlayerConfig(playerName, "sp_current", name);
    }


    public int getCurrentInventoryIndex(String playerName) {
        return getPlayersFileConfiguration(playerName).getInt("current", 1);
    }


    private void setCurrentInventoryIndex(String playerName, int index) throws IOException {
        setPlayerConfig(playerName, "current", Integer.valueOf(index));
    }


    public boolean isFirstUseForToggleInventorySpecial(String playerName) {
        return getPlayersFileConfiguration(playerName).getBoolean("sp_firstuse", true);
    }


    public void setSpecialInventoryUsingStatusForFirstUse(String playerName, boolean isFirstUse) throws IOException {
        setPlayerConfig(playerName, "sp_firstuse", Boolean.valueOf(isFirstUse));
    }


    public boolean isEnableGameModeSaving(String playerName) {
        return getPlayersFileConfiguration(playerName).getBoolean("enable_gamemode_toggle", false);
    }


    public void setGameModeSaving(String playerName, boolean enable) throws IOException {
        setPlayerConfig(playerName, "enable_gamemode_toggle", Boolean.valueOf(enable));
    }


    private String getSectionPathForSPInvPotion(String name) {
        return String.format("%s.potion", new Object[]{getSectionPathForSPInvRoot(name)});
    }


    private String getSectionPathForUserInvPotion(int index) {
        return String.format("inv%d.potion", new Object[]{Integer.valueOf(index)});
    }


    private String getSectionPathForSPInvGameMode(String name) {
        return String.format("%s.gamemode", new Object[]{getSectionPathForSPInvRoot(name)});
    }


    private String getSectionPathForUserInvGameMode(int index) {
        return String.format("inv%d.gamemode", new Object[]{Integer.valueOf(index)});
    }


    private String getSectionPathForUserContents(int index) {
        return String.format("inv%d.contents", new Object[]{Integer.valueOf(index)});
    }


    private String getSectionPathForUserArmor(int index) {
        return String.format("inv%d.armor", new Object[]{Integer.valueOf(index)});
    }


    private String getSectionPathForSPInvRoot(String name) {
        return String.format("special_inventories.%s", new Object[]{name});
    }


    private String getSectionPathForSPInvContents(String name) {
        return String.format("%s.contents", new Object[]{getSectionPathForSPInvRoot(name)});
    }


    private String getSectionPathForSPInvArmor(String name) {
        return String.format("%s.armor", new Object[]{getSectionPathForSPInvRoot(name)});
    }
}


/* Location:              D:\Desktop\ToggleInventory 1.6.4.jar!\com\github\yukinoraru\ToggleInventory\InventoryManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */