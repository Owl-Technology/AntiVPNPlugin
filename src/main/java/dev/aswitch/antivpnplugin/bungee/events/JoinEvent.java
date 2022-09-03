package dev.aswitch.antivpnplugin.bungee.events;

import dev.aswitch.antivpnplugin.api.ipsession.IpSession;
import dev.aswitch.antivpnplugin.api.profile.Profile;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.bungee.AntiVPNBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final String ip = player.getAddress().getAddress().getHostAddress();

        AntiVPNBungee.getInstance().getProfileManager().add(player.getUniqueId());

        final Profile profile = AntiVPNBungee.getInstance().getProfileManager().get(player.getUniqueId());
        IpSession ipSession = AntiVPNBungee.getInstance().getIpSessionManager().get(ip);

        if (ipSession == null) {
            AntiVPNBungee.getInstance().getIpSessionManager().add(ip, profile);
            ipSession = AntiVPNBungee.getInstance().getIpSessionManager().get(ip);
        }

        if (Settings.IP_LIMIT_ENABLED) {
            // Checks if too many people are connected to the same IP
            if (ipSession.getConnected().size() > Settings.MAX_IP_CONNECTIONS) {
                event.getPlayer().disconnect(ChatColor.translateAlternateColorCodes('&', Settings.IP_LIMIT_KICK_MESSAGE));
                return;
            }
        }

        profile.setIpSession(ipSession);
        ipSession.getConnected().add(profile);

        // Checks if the player is using a vpn.
        final boolean vpn = AntiVPNBungee.getInstance().getOtterApi().check(ip);
        if (vpn) {
            event.getPlayer().disconnect(ChatColor.translateAlternateColorCodes('&', Settings.KICK_MESSAGE));
        }

    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final Profile profile = AntiVPNBungee.getInstance().getProfileManager().get(player.getUniqueId());

        if (profile == null) return;
        profile.getIpSession().getConnected().remove(profile);
        AntiVPNBungee.getInstance().getProfileManager().remove(event.getPlayer().getUniqueId());
    }

}
