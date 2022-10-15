package com.zettelnet.levelhearts.zet.permissions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author yanni
 */
public abstract class PermissionManager {

    private final JavaPlugin plugin;

    private final Map<String, Permission> permissions;

    public PermissionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        permissions = new HashMap<String, Permission>();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    protected void add(String key, Permission perm) {
        permissions.put(key.toLowerCase(), perm);
    }

    protected void add(Permission perm) {
        add(perm.getName(), perm);
    }

    public boolean isEmptyPermission(String perm) {
        return perm == null || perm.isEmpty() || perm.equalsIgnoreCase("*");
    }

    public boolean isEmptyPermission(Permission perm) {
        return perm == null || isEmptyPermission(perm.getName());
    }

    public boolean hasPermission(Permissible permissible, String permKey) {
        if (!permissions.containsKey(permKey.toLowerCase())) {
            return false;
        }
        return permissible.hasPermission(permissions.get(permKey.toLowerCase()));
    }
}
