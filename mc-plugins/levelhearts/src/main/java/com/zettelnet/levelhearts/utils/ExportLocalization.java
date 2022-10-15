package com.zettelnet.levelhearts.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ExportLocalization {

	public static void main(String[] args) throws IOException {
		ExportLocalization exportLoc = new ExportLocalization();
		exportLoc.exportFile("esMX", new File("src\\main\\resources\\lang.yml"), ExportMethod.LUA_TABLE_ADDITIONS);
	}

	public static enum ExportMethod {
		GLOBAL_STRINGS, LUA_TABLE_ADDITIONS, SIMPLE_LUA_TABLE
	}

	public void exportFile(String language, File file, ExportMethod method) throws IOException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));

		ConfigurationSection langSection = config.getConfigurationSection(language);
		if (langSection == null) {
			throw new IllegalArgumentException("File has to contain section named by language");
		}

		Map<String, Object> values = langSection.getValues(true);

		if (method == ExportMethod.SIMPLE_LUA_TABLE) {
			System.out.println("{");
		}

		for (Entry<String, Object> entry : values.entrySet()) {
			String key = entry.getKey();
			if (method == ExportMethod.LUA_TABLE_ADDITIONS) {
				key = "L[\"" + key + "\"]";
			}
			if (method == ExportMethod.SIMPLE_LUA_TABLE) {
				key = "[\"" + key + "\"]";
			}
			String value = entry.getValue().toString().replace("\"", "\\\"");
			if (entry.getValue() instanceof String)
				System.out.println(key + " = \"" + value + "\"" + (method == ExportMethod.SIMPLE_LUA_TABLE ? "," : ""));
		}

		if (method == ExportMethod.SIMPLE_LUA_TABLE) {
			System.out.println("}");
		}
	}
}
