package de.themoep.servertags.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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
public class ServerTags extends Plugin implements Listener {

    private YamlConfig config;

    private String name;
    private String tag;

    @Override
    public void onEnable() {
        if(loadConfig()) {
            getProxy().registerChannel(getDescription().getName().toLowerCase() + ":getinfo");
            getProxy().registerChannel(getDescription().getName().toLowerCase() + ":info");
            getProxy().getPluginManager().registerListener(this, this);
            getProxy().getPluginManager().registerCommand(this, new ServerTagsCommand());
        }
    }

    public boolean loadConfig() {
        try {
            config = new YamlConfig(this, getDataFolder() + File.separator + "config.yml");
        } catch(IOException e) {
            getLogger().log(Level.SEVERE, "Unable to load configuration! " + getDescription().getName() + " will not be enabled!");
            e.printStackTrace();
            return false;
        }
        name = config.getString("name", "");
        getLogger().log(Level.INFO, "Server name: " + name);
        tag = config.getString("tag", "");
        getLogger().log(Level.INFO, "Server tag: " + tag);
        for(ProxiedPlayer p : getProxy().getPlayers()) {
            sendPluginMessage(p.getServer(), "info", p.getName(), name, tag);
        }
        return true;
    }

    @EventHandler
    public void onPluginMessageReceive(PluginMessageEvent event) {
        if(event.getTag().startsWith(getDescription().getName() + ":")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
            String subchannel = event.getTag().split(":")[1];
            try {
                String playername = in.readUTF();
                if("getInfo".equalsIgnoreCase(subchannel)) {
                    sendPluginMessage(event.getSender(), "serverInfo", playername, name, tag);
                } else {
                    getLogger().log(Level.WARNING, "The subchannel " + subchannel + " is not supported!");
                }
            } catch(IllegalStateException e) {
                getLogger().log(Level.SEVERE, "No playername in plugin message!");
            }
        }
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        sendPluginMessage(event.getPlayer().getServer(), "serverInfo", event.getPlayer().getName(), name, tag);
    }

    private void sendPluginMessage(Connection connection, String subchannel, String... args) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for(String s : args) {
            out.writeUTF(s);
        }
        if(connection instanceof Server) {
            ((Server) connection).sendData(getDescription().getName().toLowerCase() + ":" + subchannel.toLowerCase(), out.toByteArray());
        } else if(connection instanceof ProxiedPlayer) {
            ((ProxiedPlayer) connection).sendData(getDescription().getName().toLowerCase() + ":" + subchannel.toLowerCase(), out.toByteArray());
        } else {
            getLogger().log(Level.SEVERE, "Tried to send a plugin message on a connection which doesn't support it!");
        }
    }

    private class ServerTagsCommand extends Command {
        public ServerTagsCommand() {
            super(getDescription().getName().toLowerCase(), getDescription().getName().toLowerCase() + ".command", "st");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if(args.length > 0) {
                if("version".equalsIgnoreCase(args[0])) {
                    sender.sendMessage(ChatColor.GREEN + getDescription().getName() + " " + getDescription().getVersion() + " by " + getDescription().getAuthor());
                    return;
                } else if("reload".equalsIgnoreCase(args[0])) {
                    Set<Server> servers = new HashSet<Server>();
                    for(ServerInfo si : getProxy().getServers().values()) {
                        if(si.getPlayers().size() > 0) {
                            servers.add(si.getPlayers().iterator().next().getServer());
                        }
                    }
                    for(Server server : servers) {
                        sendPluginMessage(server, "reload");
                    }
                    if(loadConfig()) {
                        sender.sendMessage(ChatColor.GREEN + "[" + ChatColor.WHITE + getDescription().getName() + ChatColor.GREEN + "] Reloaded config!");
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "[" + ChatColor.WHITE + getDescription().getName() + ChatColor.RED + "] Error while trying to reload the config!");
                    }
                    return;
                }
            }
            sender.sendMessage("Usage: /" + getName() + " [version|reload]");
        }
    }
}
