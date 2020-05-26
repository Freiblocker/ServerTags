package de.themoep.servertags.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

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
public class PlayerServerInfoChangeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private ServerInfo server;
    private ServerInfo oldServer;

    /**
     * Called when the info about the server of a player changes
     * @param player The player
     * @param server The new server info
     * @param oldServer The old server info; <tt>null</tt> if the player didn't have one
     */
    public PlayerServerInfoChangeEvent(Player player, ServerInfo server, ServerInfo oldServer) {
        super(player);
        this.server = server;
        this.oldServer = oldServer;
    }

    /**
     * Get the changed server info
     * @return The ServerInfo which changed
     */
    public ServerInfo getServer() {
        return server;
    }

    /**
     * Get the old server info
     * @return The old ServerInfo
     */
    public ServerInfo getOldServer() {
        return oldServer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
