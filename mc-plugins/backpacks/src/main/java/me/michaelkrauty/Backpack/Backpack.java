package me.michaelkrauty.Backpack;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;

/**
 * Created on 7/24/2014.
 *
 * @author michaelkrauty
 */
public class Backpack {

	private static Main main;

	private String uuid;
	private Inventory inventory;
	private File file;

	public Backpack(Main main, String uuid) {
		this.main = main;
		this.uuid = uuid;
		file = new File(main.getDataFolder() + "/backpacks/" + uuid);
		load();
	}

	public String getUUID() {
		return uuid;
	}

	public File getFile() {
		return file;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
		main.getServer().getScheduler().scheduleAsyncDelayedTask(main, () -> save());
	}

	private boolean checkExistance() {
		boolean exists = true;
		if (main.sql != null) {
			if (!main.sql.backpackExists(uuid)) {
				exists = false;
				main.sql.addBackpack(uuid);
			}
		} else if (!file.exists()) {
			exists = false;
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return exists;
	}

	public void load() {
		if (main.sql != null) {
			try {
				if (file.exists()) {
					YamlConfiguration yaml = new YamlConfiguration();
					yaml.load(file);
					inventory = Main.fromBase64(yaml.getString("data"));
					main.sql.updateBackpack(uuid, Main.toBase64(inventory));
					file.delete();
				} else if (main.sql.backpackExists(uuid))
					inventory = Main.fromBase64(main.sql.getBackpackInventory(uuid));
				else
					inventory = main.getServer().createInventory(null, 54, "Backpack");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (file.exists()) {
					YamlConfiguration yaml = new YamlConfiguration();
					yaml.load(file);
					inventory = Main.fromBase64(yaml.getString("data"));
				} else
					inventory = main.getServer().createInventory(null, 54, "Backpack");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void save() {
		checkExistance();
		if (main.sql != null) {
			try {
				main.sql.updateBackpack(uuid, Main.toBase64(inventory));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			YamlConfiguration yaml = new YamlConfiguration();
			try {
				yaml.set("data", Main.toBase64(inventory));
				yaml.save(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
