package de.themoep.servertags.bukkit;

import org.bukkit.ChatColor;

/*
 * ServerTags
 * Copyright (C) 2020 Max Lee aka Phoenix616 (mail@moep.tv)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class ServerInfo {
    private String name;
    private String tag;

    public ServerInfo(String name, String tag) {
        this.name = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name));
        this.tag = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', tag));
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o != null && o instanceof ServerInfo) {
            ServerInfo other = (ServerInfo) o;
            return name.equals(other.name) && tag.equals(other.tag);
        }
        return false;
    }
}
