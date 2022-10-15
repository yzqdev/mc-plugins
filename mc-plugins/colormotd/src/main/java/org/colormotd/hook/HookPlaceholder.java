/*
 * Copyright (C) 2019 Bukkit Commons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.colormotd.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import org.colormotd.ColorMotd;
import org.colormotd.ReflectFactory;
import org.colormotd.utils.Config;
import org.colormotd.utils.InternalPlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**使用placeholder的api
 * @author yzqde
 */
public class HookPlaceholder {
    Config config;
    InternalPlaceHolder internalPlaceHolder;
    public static HookPlaceholder instance;

    public static HookPlaceholder getPlaceHolder() {
        return instance;
    }

    public HookPlaceholder(Config config) {
        this.config = config;
        this.internalPlaceHolder = new InternalPlaceHolder(config);
    }

    public String applyPlaceHolder(String text, String ip) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        text = internalPlaceHolder.applyPlaceHolder(text, ip);
        text = text.replaceAll("%online%", String.valueOf(ReflectFactory.getPlayers().length));
        text = text.replaceAll("%maxplayer%", String.valueOf(Bukkit.getMaxPlayers()));
        text = text.replaceAll("%playedbefore%", String.valueOf(Bukkit.getOfflinePlayers().length));
        if (ColorMotd.usePlaceHolderAPI) {
            text = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("ColorMOTD"), text);
        }
        return text;

    }
}
