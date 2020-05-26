package de.themoep.servertags.bukkit.integrations;

import com.dthielke.herochat.ChannelChatEvent;
import de.themoep.servertags.bukkit.ServerInfo;
import de.themoep.servertags.bukkit.ServerTags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
