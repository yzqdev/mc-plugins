package me.michaelkrauty.Backpack;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created on 7/30/2014.
 *
 * @author michaelkrauty
 */
public class Config {

	private Main main;

	private File configFile = new File(Main.main.getDataFolder(), "config.yml");
	private YamlConfiguration config = new YamlConfiguration();

	public Config(Main instance) {
		main = instance;
		checkFile();
		update();
		reload();
	}

	private void checkFile() {
		if (!main.getDataFolder().exists()) {
			main.getDataFolder().mkdir();
		}
	}

	public void reload() {
		try {
			config.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			reload();
			if (config.getString("checkupdate") == null) {
				config.set("checkupdate", true);
			}
			if (config.getString("mysql.database") == null) {
				config.set("mysql.database", "minecraft");
			}
			if (config.getString("mysql.host") == null) {
				config.set("mysql.host", "localhost");
			}
			if (config.getString("mysql.user") == null) {
				config.set("mysql.user", "root");
			}
			if (config.getString("mysql.pass") == null) {
				config.set("mysql.pass", "12345");
			}
			if (config.getString("mysql.table") == null) {
				config.set("mysql.table", "Backpacks");
			}
			if (config.getString("data") == null) {
				config.set("data", "flatfile");
			}
			if (config.getString("cost") == null) {
				config.set("cost", 100);
			}
			if (config.getString("cooldown") == null) {
				config.set("cooldown", 0);
			}
			config.save(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getInt(String path) {
		return config.getInt(path);
	}

	public String getString(String path) {
		return config.getString(path);
	}

	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}
}