package yzq.ExtendedInventory.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import yzq.ExtendedInventory.ExtendedInventory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created on 8/11/2014.
 *
 * @author michaelkrauty
 */
public class Locale {

    private ExtendedInventory main;


    private static File localeFile = new File(ExtendedInventory.getInstance().getDataFolder() + "/lang", getLang());
    private static YamlConfiguration locale = new YamlConfiguration();
    public static String getLang() {
        String langString = ExtendedInventory.getInstance().getConfig().getString("Lang");
        switch (langString) {
            default:
                return "en_US.yml";
            case "zh_CN":
                return "zh_CN.yml";
            case "en_US":
                return "en_US.yml";
        }

    }

    public static YamlConfiguration getLangFiles() {
        return YamlConfiguration.loadConfiguration(localeFile);
    }

    public Locale(ExtendedInventory instance) {
        main = instance;
        //checkFile();
        reload();
        update();
        saveDefaultLang();
        //save();
    }

    private void saveDefaultLang() {
        main.saveResource("lang/zh_CN.yml", false);
        main.saveResource("lang/en_US.yml", false);
    }

    private void checkFile() {
        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdir();
        }
        if (!localeFile.exists()) {
            try {
                localeFile.createNewFile();
                InputStream input = main.getResource("locale.yml");
                OutputStream output = new FileOutputStream(localeFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) > 0) {
                    output.write(buffer, 0, bytesRead);
                }
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        try {
            locale.load(localeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            locale.save(localeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getMessage(String path) {
        ArrayList<String> ret;
        if (locale.getStringList(path).size() != 0) {
            ret = new ArrayList<String>(locale.getStringList(path));
        } else {
            ret = new ArrayList<String>();
            ret.add(locale.getString(path));
        }
        return ret;


    }

    public int getInt(String path) {
        return locale.getInt(path);
    }

    public String getString(String path) {
        return locale.getString(path);
    }

    public boolean getBoolean(String path) {
        return locale.getBoolean(path);
    }

    public void update() {
        if (locale.getString("permission_denied") == null) {
            locale.set("permission_denied", "&cYou don't have permission to do that!");
        }
        if (locale.getString("backpack_command") == null) {
            locale.set("backpack_command", new String[]{
                    "&a--[Backpacks]--",
                    "&c/backpack get [name]&7: Get a backpack ([name] is optional)",
                    "&c/backpack name <name>&7: Name the backpack you are holding.",
                    "&2Backpacks are physical items. You can carry as many as you want. If you drop a backpack, anyone can pick it up & get the items inside.",
                    "&4WARNING: &cDo NOT rename a backpack with an anvil! You will never be able to access the items inside of it again!"
            });
        }
        if (locale.getString("insufficient_funds") == null) {
            locale.set("insufficient_funds", "&cYou need at least $<backpack_cost> to get a backpack!");
        }
        if (locale.getString("got_backpack_without_price") == null) {
            locale.set("got_backpack_without_price", "&7You gave yourself one backpack.");
        }
        if (locale.getString("bought_backpack") == null) {
            locale.set("bought_backpack", "&7You bought a backpack for $<backpack_cost>");
        }
        if (locale.getString("inventory_full") == null) {
            locale.set("inventory_full", "&cYour inventory is too full to give you a backpack!");
        }
        if (locale.getString("renamed_backpack") == null) {
            locale.set("renamed_backpack", "&7Renamed this backpack to <new_name>");
        }
        if (locale.getString("backpack_not_in_hand") == null) {
            locale.set("backpack_not_in_hand", "&cMake sure you're holding a backpack in your hand.");
        }
        if (locale.getString("cooldown") == null) {
            locale.set("cooldown", "&cYou can't spawn a backpack for another <cooldown> seconds.");
        }
    }
}