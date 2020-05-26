package de.themoep.servertags.bukkit.integrations;

import de.themoep.servertags.bukkit.ServerInfo;
import de.themoep.servertags.bukkit.ServerTags;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

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
public class PlaceholderApiIntegration extends PlaceholderExpansion {
    private final ServerTags plugin;

    public PlaceholderApiIntegration(ServerTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return plugin.getName().toLowerCase();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        ServerInfo serverInfo = plugin.getPlayerServer(player);
        String r = "";
        if (serverInfo != null) {
            if (identifier.startsWith("servertag")) {
                r = serverInfo.getTag();
            } else if (identifier.startsWith("servername")) {
                r = serverInfo.getName();
            } else {
                return null;
            }
        }

        if (identifier.endsWith("_brackets")) {
            r = "[" + r + "]";
        }

        return r;
    }

    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }
}
