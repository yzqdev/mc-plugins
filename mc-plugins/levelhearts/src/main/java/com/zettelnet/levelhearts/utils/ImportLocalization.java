package com.zettelnet.levelhearts.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.zettelnet.levelhearts.zet.chat.FormatOption;

public class ImportLocalization {

	private final Map<String, FormatOption> formatOptions;

	public static void main(String[] args) throws IOException {
		ImportLocalization importLoc = new ImportLocalization();
		importLoc.importFile("deDE", new File("E:\\deDE"));
	}

	public ImportLocalization() {

		formatOptions = new HashMap<String, FormatOption>();

		formatOptions.put("heart", new FormatOption("\u2764", "", ""));
		formatOptions.put("heartSmall", new FormatOption("\u2665", "", ""));
		formatOptions.put("heartWhite", new FormatOption("\u2661", "", ""));
		formatOptions.put("heartRotated", new FormatOption("\u2765", "", ""));
		formatOptions.put("heartExclamation", new FormatOption("\u2763", "", ""));

		// Misc
		formatOptions.put("br", new FormatOption("\n", System.lineSeparator(), System.lineSeparator()));
		formatOptions.put("...", new FormatOption("\u2026", "...", "..."));
	}

	public void importFile(String language, File raw) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(raw), Charset.forName("UTF-8")));

		byte[] encoded = Files.readAllBytes(Paths.get("src", "util", "java", "plain-lang.yml"));
		String plainLang = new String(encoded, Charset.forName("UTF-8"));
		plainLang = plainLang.replace("#_LANGUAGE#", language);

		String nextLine = br.readLine();

		while (nextLine != null) {
			if (nextLine.length() != 0 && !nextLine.startsWith("--")) {
				String rawKey = nextLine.substring(0, nextLine.indexOf('=')).toUpperCase().replace('.', '_');
				String rawValue = nextLine.substring(rawKey.length() + 1).replaceAll("\"", "\\\\\"");
				String value = "";
				for (char c : rawValue.toCharArray()) {
					value += toSafeChar(c);
				}
				plainLang = plainLang.replace("#" + rawKey + "#", "\"" + value + "\"");
			}
			nextLine = br.readLine();
		}
		br.close();

		Files.write(Paths.get(raw.getPath() + ".yml"), plainLang.getBytes(Charset.forName("UTF-8")));
	}

	private String toSafeChar(char c) {
		for (Entry<String, FormatOption> entry : formatOptions.entrySet()) {
			if (entry.getValue().toString().equals(String.valueOf(c))) {
				return "&(" + entry.getKey() + ")";
			}
		}
		return String.valueOf(c);
	}
}
