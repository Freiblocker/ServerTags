package de.themoep.servertags.bukkit.integrations;

import com.dthielke.herochat.ChannelChatEvent;
import de.themoep.servertags.bukkit.ServerInfo;
import de.themoep.servertags.bukkit.ServerTags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
public class HeroChatIntegration implements Listener {
    private final ServerTags plugin;

    public HeroChatIntegration(ServerTags plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChannelChatEvent(ChannelChatEvent event) {
        String format;
        format = event.getFormat().replace("{default}", event.getChannel().getFormatSupplier().getStandardFormat());
        ServerInfo serverInfo = plugin.getPlayerServer(event.getSender().getPlayer());
        if(serverInfo != null)        {
            format = format.replace("{servertag}", "[" + serverInfo.getTag() + "]");
            format = format.replace("{servername}", "[" + serverInfo.getName() + "]");
        } else {
            format = format.replace("{servertag}", "");
            format = format.replace("{servername}", "");
        }
        event.setFormat(format);
    }
}
