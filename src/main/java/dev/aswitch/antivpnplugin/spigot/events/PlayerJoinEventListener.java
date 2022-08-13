package dev.aswitch.antivpnplugin.spigot.events;

import dev.aswitch.antivpnplugin.api.profile.Profile;
import dev.aswitch.antivpnplugin.api.utils.ChatUtils;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.spigot.AntiVPNSpigot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTask(AntiVPNSpigot.getInstance(), new Runnable() {
            @Override
            public void run() {
                boolean vpn = AntiVPNSpigot.getInstance().getOtterApi().check(event.getAddress().getHostAddress());
                if (vpn) {

                    final String message = ChatUtils.colour(Settings.ALERT_MESSAGE.replaceAll("%player%", event.getPlayer().getName()));
                    if (Settings.ALERTS_ENABLED) {
                        Bukkit.getConsoleSender().sendMessage(message);

                        AntiVPNSpigot.getInstance().getProfileManager().toList().stream().filter(Profile::isAlerts).forEach(profile -> {
                            Player player = Bukkit.getPlayer(profile.getUuid());
                            if (player != null) {
                                player.sendMessage(message);
                            }
                        });

                    }

                    if (Settings.KICK_PLAYERS
                            && !(Settings.OP_BYPASS && event.getPlayer().isOp())
                            && !event.getPlayer().hasPermission("otter.bypass")) {

                        Bukkit.getScheduler().runTask(AntiVPNSpigot.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                event.getPlayer().kickPlayer(ChatUtils.colour(Settings.KICK_MESSAGE));
                            }
                        });

                    }
                }
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        AntiVPNSpigot.getInstance().getProfileManager().add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        AntiVPNSpigot.getInstance().getProfileManager().remove(event.getPlayer().getUniqueId());
    }

}
