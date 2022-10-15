package org.health.utils;

import org.bukkit.Bukkit;

public class Debug {
    private static final boolean DEBUG_MODE = false;

    public static void color(String s) {
        if (DEBUG_MODE) {
            Bukkit.getConsoleSender().sendMessage(s.replace("&", "?"));
        }
    }

    public static void log(String s) {
        if (DEBUG_MODE) {
            System.out.println(s);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
