package com.zettelnet.levelhearts.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GenerateFormatOptions {

	public static void main(String[] args) throws IOException {
		GenerateFormatOptions genFo = new GenerateFormatOptions();
		genFo.fromFile(new File("E:\\cyrilic.txt"));
	}
	
	public String fromFile(File file) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(file.getPath()), Charset.forName("UTF-8"));
		
		for(String line : lines) {
			String[] explo = line.split(" ", 3);
			String unicode = explo[0];
			String fullName = explo[2];
			String[] fullNameExplo = fullName.split(" ");
			String cDefault = fullNameExplo[1].equals("CAPITAL") ? fullNameExplo[fullNameExplo.length - 1] : fullNameExplo[fullNameExplo.length - 1].toLowerCase();
			String name = cDefault + "cyrl";
			if (fullNameExplo.length == 5) {
				name += fullNameExplo[3].toLowerCase().charAt(0);
			}

			//System.out.println("\\u"+unicode+" = "+c+" ("+name+")");
			System.out.println("formatOptions.put(\""+name+"\", new FormatOption(\"\\u"+unicode+"\", \""+cDefault+"\", \""+cDefault+"\"));");
			//System.out.println("|&("+name+")|Cyrillic "+(fullNameExplo[1].equals("CAPITAL") ? "Capital" : "Small")+" Letter **"+c+"**|");
		}
		
		
		return null;
	}
}
