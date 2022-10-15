package com.zettelnet.levelhearts.utils;

import java.beans.Introspector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

public class GenerateLanguageLoader {

	private static Map<String, String> languageValues;

	public static void main(String[] args) throws FileNotFoundException {
		fromYamlFile("src/lang.yml", "enUS");
	}

	@SuppressWarnings("unchecked")
	public static void fromYamlFile(String path, String defaultLanguage) throws FileNotFoundException {
		InputStream input = new FileInputStream(new File(path));
		Yaml yaml = new Yaml();
		Object data = yaml.load(input);
		languageValues = new HashMap<String, String>();
		addMembers("", ((Map<String, Object>) data).get(defaultLanguage));
		for (Entry<String, String> entry : languageValues.entrySet()) {
			String key = entry.getKey();
			String value = escapeString(entry.getValue());
			String varName = pathToVariableName(key);
			System.out.println(varName + " = load(config, lang + \"" + key + "\", \"" + value + "\");");
			// commandsErrorSyntax = load(config, lang +
			// ".commands.error.syntax", "(%(syntax))");
		}
	}

	public static String pathToVariableName(String path) {
		char[] chars = path.toCharArray();
		String variableName = "";

		boolean uppercase = false;
		char seperator = '.';

		for (char c : chars) {
			if (uppercase) {
				variableName += Character.toUpperCase(c);
				uppercase = false;
			} else {
				if (c == seperator) {
					uppercase = true;
				} else {
					variableName += c;
				}
			}
		}

		return Introspector.decapitalize(variableName);
	}

	public static String escapeString(String str) {
		char[] chars = str.toCharArray();
		String newStr = "";

		for (char c : chars) {
			if (c == '\\' || c == '\"' || c == '\'') {
				newStr += '\\';
			}
			newStr += c;
		}
		return newStr;
	}

	@SuppressWarnings("unchecked")
	public static void addMembers(String path, Object data) {
		if (data == null) {
			return;
		}
		if (data instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) data;
			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				addMembers(path + "." + key, value);
			}
			return;
		}
		languageValues.put(path, data.toString());
	}
}
