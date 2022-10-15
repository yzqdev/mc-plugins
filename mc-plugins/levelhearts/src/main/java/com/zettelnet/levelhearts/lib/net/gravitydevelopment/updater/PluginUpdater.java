package com.zettelnet.levelhearts.lib.net.gravitydevelopment.updater;

import java.io.File;
import java.util.Arrays;

import org.bukkit.plugin.Plugin;

public class PluginUpdater extends Updater {

    public PluginUpdater(Plugin plugin, int id, File file, UpdateType type, boolean announce) {
        super(plugin, id, file, type, announce);
    }

    @Override
    public boolean shouldUpdate(String localVersion, String remoteVersion) {
        try {
            int digitLength = localVersion.length() > remoteVersion.length() ? localVersion.length() : remoteVersion.length();
            String[] localVersionArray = Arrays.copyOf(localVersion.split("\\."), digitLength);
            String[] remoteVersionArray = Arrays.copyOf(remoteVersion.split("\\."), digitLength);
            for (int i = 0; i < digitLength; i++) {
                if (parseInt(localVersionArray[i]) < parseInt(remoteVersionArray[i])) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return !localVersion.equalsIgnoreCase(remoteVersion);
        }
    }

    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }
}
