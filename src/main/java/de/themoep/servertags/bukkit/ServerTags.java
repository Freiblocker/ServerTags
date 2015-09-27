package de.themoep.servertags.bukkit;

import com.dthielke.herochat.ChannelChatEvent;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * ServerTags
 * Copyright (C) 2015 Max Lee (https://github.com/Phoenix616/)
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
public class ServerTags extends JavaPlugin implements PluginMessageListener, Listener {

    private Map<UUID, ServerInfo> serverMap = new HashMap<UUID, ServerInfo>();

    @Override
    public void onEnable() {
        getServer().getMessenger().registerIncomingPluginChannel(this, getDescription().getName(), this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChannelChatEvent(ChannelChatEvent event) {
        String format;
        format = event.getFormat().replace("{default}", event.getChannel().getFormatSupplier().getStandardFormat());
        if(serverMap.containsKey(event.getSender().getPlayer().getUniqueId()))        {
            format = format.replace("{servertag}", "[" + serverMap.get(event.getSender().getPlayer().getUniqueId()).getTag() + "]");
            format = format.replace("{servername}", "[" + serverMap.get(event.getSender().getPlayer().getUniqueId()).getName() + "]");
        } else {
            format = format.replace("{servertag}", "");
            format = format.replace("{servername}", "");
        }
        event.setFormat(format);
    }

    /**
     * Get the server a player is on
     * @param player
     * @return A server info object; <tt>null</tt> </7tt>if the player doesn't have one
     */
    public ServerInfo getPlayerServer(Player player) {
        return serverMap.get(player.getUniqueId());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player receiver, byte[] bytes) {
        if(channel.equalsIgnoreCase(getDescription().getName())) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            try {
                String subchannel = in.readUTF();
                if("serverInfo".equalsIgnoreCase(subchannel)) {
                    try {
                        String playername = in.readUTF();
                        Player player = getServer().getPlayer(playername);
                        if(player != null) {
                            ServerInfo server = new ServerInfo("", "");
                            try {
                                String name = in.readUTF();
                                String tag = in.readUTF();
                                getLogger().info(name + " - " + tag);
                                server = new ServerInfo(name, tag);
                            } catch(IllegalStateException e) {
                                getLogger().log(Level.WARNING, "No server name or tag send in plugin message. Assuming empty one!");
                            }
                            ServerInfo oldServer = serverMap.put(player.getUniqueId(), server);
                            if(!server.equals(oldServer)) {
                                getServer().getPluginManager().callEvent(new PlayerServerInfoChangeEvent(player, server, oldServer));
                            }
                        } else {
                            getLogger().log(Level.WARNING, "The player with the name " + playername + " is not online (anymore?)");
                        }
                    } catch(IllegalStateException e) {
                        getLogger().log(Level.SEVERE, "No playername in plugin message!");
                    }
                } else {
                    getLogger().log(Level.WARNING, "The subchannel " + subchannel + " is not supported!");
                }
            } catch(IllegalStateException e) {
                getLogger().log(Level.SEVERE, "No subchannel send in plugin message!");
            }
        }
    }
}
