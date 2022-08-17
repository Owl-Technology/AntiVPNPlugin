package dev.aswitch.antivpnplugin.spigot.events;

import dev.aswitch.antivpnplugin.api.ipsession.IpSession;
import dev.aswitch.antivpnplugin.api.profile.Profile;
import dev.aswitch.antivpnplugin.api.utils.ChatUtils;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.spigot.AntiVPNSpigot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinEventListener implements Listener {

//    @EventHandler
//    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
//
//
//
//    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String ip = event.getAddress().getHostAddress();

        AntiVPNSpigot.getInstance().getProfileManager().add(player.getUniqueId());

        final Profile profile = AntiVPNSpigot.getInstance().getProfileManager().get(player.getUniqueId());
        IpSession ipSession = AntiVPNSpigot.getInstance().getIpSessionManager().get(ip);

        if (ipSession == null) {
            AntiVPNSpigot.getInstance().getIpSessionManager().add(ip, profile);
            ipSession = AntiVPNSpigot.getInstance().getIpSessionManager().get(ip);
        }

        if (Settings.IP_LIMIT_ENABLED) {
            // Checks if too many people are connected to the same IP
            if (ipSession.getConnected().size() > Settings.MAX_IP_CONNECTIONS) {
                kick(player, Settings.IP_LIMIT_KICK_MESSAGE);
                return;
            }
        }

        profile.setIpSession(ipSession);
        ipSession.getConnected().add(profile);

        // Checks if the player is using a vpn.
        final boolean vpn = AntiVPNSpigot.getInstance().getOtterApi().check(ip);
        if (vpn) {

            kick(player, Settings.KICK_MESSAGE);

            final String message = ChatUtils.colour(Settings.ALERT_MESSAGE.replaceAll("%player%", player.getName()));
            if (Settings.ALERTS_ENABLED) {
                if (Settings.CONSOLE_ALERTS) {
                    Bukkit.getConsoleSender().sendMessage(message);
                }

                // Looks stupid but its faster.
                for (int i = 0; i < AntiVPNSpigot.getInstance().getProfileManager().toList().size(); i++) {
                    Profile staff = AntiVPNSpigot.getInstance().getProfileManager().toList().get(i);
                    if (staff == null) continue;

                    staff.getPlayer().sendMessage(message);

                }

            }
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        final Profile profile = AntiVPNSpigot.getInstance().getProfileManager().get(event.getPlayer().getUniqueId());
        profile.getIpSession().getConnected().remove(profile);

        AntiVPNSpigot.getInstance().getProfileManager().remove(event.getPlayer().getUniqueId());
    }


    public void kick(Player player, String message) {
        if (Settings.KICK_PLAYERS
                && !(Settings.OP_BYPASS && player.isOp())
                && !player.hasPermission("otter.bypass")) {
            Bukkit.getScheduler().runTask(AntiVPNSpigot.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.kickPlayer(ChatUtils.colour(message));
                }
            });
        }


    }

}
