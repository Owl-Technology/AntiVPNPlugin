package dev.aswitch.antivpnplugin.spigot.commands;

import dev.aswitch.antivpnplugin.api.profile.Profile;
import dev.aswitch.antivpnplugin.api.utils.ChatUtils;
import dev.aswitch.antivpnplugin.spigot.AntiVPNSpigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.owltech.otter.spigot.OtterAntiVPN;
import xyz.owltech.otter.utils.ChatUtils;

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
        sender.sendMessage(ChatUtils.colour("&7&m----------------------------------"));
        sender.sendMessage(ChatUtils.colour("&cOtter"
                + " [v" + AntiVPNSpigot.getInstance().getDescription().getVersion() + "]"));
        sender.sendMessage(ChatUtils.colour("&7&m----------------------------------"));
        sender.sendMessage(ChatUtils.colour("&7/otter alerts"));
        sender.sendMessage(ChatUtils.colour("&8 -> &cToggles your alerts."));
        sender.sendMessage(ChatUtils.colour(""));
        sender.sendMessage(ChatUtils.colour("&7/otter reload"));
        sender.sendMessage(ChatUtils.colour("&8 -> &cReloads the plugin configuration."));
        sender.sendMessage(ChatUtils.colour(""));
        sender.sendMessage(ChatUtils.colour("&7/otter purge"));
        sender.sendMessage(ChatUtils.colour("&8 -> &cDeletes all IP cache's."));
        sender.sendMessage(ChatUtils.colour("&7&m----------------------------------"));
    }

}
