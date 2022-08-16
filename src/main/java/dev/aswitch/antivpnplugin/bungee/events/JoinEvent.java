package dev.aswitch.antivpnplugin.bungee.events;

import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.bungee.AntiVPNBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final String ip = player.getAddress().getAddress().getHostAddress();

        final boolean vpn = AntiVPNBungee.getInstance().getOtterApi().check(ip);
        if (vpn) {
            event.getPlayer().disconnect(ChatColor.translateAlternateColorCodes('&', Settings.KICK_MESSAGE));
        }
    }

}
