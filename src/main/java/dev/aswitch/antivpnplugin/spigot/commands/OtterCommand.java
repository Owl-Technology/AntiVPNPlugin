package dev.aswitch.antivpnplugin.spigot.commands;

import dev.aswitch.antivpnplugin.api.profile.Profile;
import dev.aswitch.antivpnplugin.api.utils.ChatUtils;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.spigot.AntiVPNSpigot;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OtterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("otter.command")) {
            if (args.length == 0) {
                help(sender);
            } else {
                String subCommand = args[0];

                switch (subCommand.toLowerCase()) {
                    case "reload": {
                        if (sender.hasPermission("otter.reload")) {
                            sender.sendMessage(ChatUtils.colour("&7Reloading config..."));
                            AntiVPNSpigot.getInstance().loadConfigSettings();
                            sender.sendMessage(ChatUtils.colour("&aReloaded config!"));
                        } else {
                            sender.sendMessage(ChatUtils.colour("&cYou do not have permission to do this command"));
                        }
                        break;
                    }

                    case "alerts": {
                        if (sender.hasPermission("otter.alerts")) {
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(ChatUtils.colour("&cThis command is for players only"));
                            } else {
                                Player player = (Player) sender;
                                Profile profile = AntiVPNSpigot.getInstance().getProfileManager().get(player.getUniqueId());

                                if (profile.toggleAlerts()) {
                                    player.sendMessage(ChatUtils.colour("&aEnabled your antivpn alerts!"));
                                } else {
                                    player.sendMessage(ChatUtils.colour("&cDisabled your antivpn alerts!"));
                                }
                            }
                        } else {
                            sender.sendMessage(ChatUtils.colour("&cYou do not have permission to do this command"));
                        }
                        break;
                    }

                    case "purge": {
                        if (sender.hasPermission("otter.purge")) {
                            AntiVPNSpigot.getInstance().getOtterApi().cachedResults.clear();
                            sender.sendMessage(ChatUtils.colour("&aPurged all cached IPs."));
                        } else {
                            sender.sendMessage(ChatUtils.colour("&cYou do not have permission to do this command"));
                        }
                        break;
                    }

                    case "serverid": {
                        if (sender.hasPermission("otter.serverid")) {
                            sender.sendMessage(ChatUtils.colour("&7Your serverID is &e" + Settings.LICENSE));
                        } else {
                            sender.sendMessage(ChatUtils.colour("&cYou do not have permission to do this command"));
                        }
                        break;
                    }

                    case "check": {
                        if (sender.hasPermission("otter.check")) {

                            if (args.length <= 1) {
                                sender.sendMessage(ChatUtils.colour("&c/otter check <user>"));
                            } else {

                                String input = args[1];

                                Player player = Bukkit.getPlayer(input);
                                if (player == null) {
                                    sender.sendMessage(ChatUtils.colour("&cPlayer not found"));
                                } else {
                                    Profile profile = AntiVPNSpigot.getInstance().getProfileManager().get(player.getUniqueId());

                                    if (profile == null) {
                                        sender.sendMessage(ChatUtils.colour("&cPlayer not found"));
                                    } else {

                                        String ip = profile.getIp();
                                        boolean vpn = AntiVPNSpigot.getInstance().getOtterApi().check(ip);

                                        StringBuilder players = new StringBuilder();

                                        for (Profile profile1 : profile.getIpSession().getConnected()) {
                                            if (profile1.getUuid() != profile.getUuid()) {
                                                Player player1 = Bukkit.getPlayer(profile1.getUuid());
                                                if (player1 == null) {
                                                    continue;
                                                }
                                                players.append(profile1.getPlayer().getName()).append(" ");
                                            }
                                        }

                                        if (players.toString().equals("")) {
                                            players = new StringBuilder("none");
                                        }

                                        sender.sendMessage(ChatUtils.colour("&7&m                                       "));
                                        sender.sendMessage(ChatUtils.colour("&e&lChecking " + player.getName()));
                                        sender.sendMessage(ChatUtils.colour(""));
                                        sender.sendMessage(ChatUtils.colour("&7VPN: &f" + vpn));
                                        sender.sendMessage(ChatUtils.colour("&7Players with the same IP: &f" + players));
                                        sender.sendMessage(ChatUtils.colour("&7&m                                       "));

                                    }

                                }
                            }

                        } else {
                            sender.sendMessage(ChatUtils.colour("&cYou do not have permission to do this command"));
                        }
                        break;
                    }

                    default: {
                        help(sender);
                        break;
                    }
                }

            }
        } else {
            sender.sendMessage(ChatUtils.colour("&cYou do not have permission to do this command"));
        }
        return true;
    }

    private void help(CommandSender sender) {
        sender.sendMessage(ChatUtils.colour("&7&m                                       "));
        sender.sendMessage(ChatUtils.colour("&eOtter"
                + " &7[&ev" + AntiVPNSpigot.getInstance().getDescription().getVersion() + "&7]"));
        sender.sendMessage(ChatUtils.colour("&7&m                                       "));
        sender.sendMessage(ChatUtils.colour("&7/otter alerts"));
        sender.sendMessage(ChatUtils.colour("&8 -> &eToggles your alerts."));
        sender.sendMessage(ChatUtils.colour(""));
        sender.sendMessage(ChatUtils.colour("&7/otter reload"));
        sender.sendMessage(ChatUtils.colour("&8 -> &eReloads the plugin configuration."));
        sender.sendMessage(ChatUtils.colour(""));
        sender.sendMessage(ChatUtils.colour("&7/otter purge"));
        sender.sendMessage(ChatUtils.colour("&8 -> &eDeletes all IP cache's."));
        sender.sendMessage(ChatUtils.colour(""));
        sender.sendMessage(ChatUtils.colour("&7/otter serverid"));
        sender.sendMessage(ChatUtils.colour("&8 -> &eShows your server ID."));
        sender.sendMessage(ChatUtils.colour(""));
        sender.sendMessage(ChatUtils.colour("&7/otter check <username>"));
        sender.sendMessage(ChatUtils.colour("&8 -> &eDisplays a player's online alts."));
        sender.sendMessage(ChatUtils.colour("&7&m                                       "));
    }

}
