package dev.aswitch.antivpnplugin.spigot;

import dev.aswitch.antivpnplugin.api.OtterApi;
import dev.aswitch.antivpnplugin.api.ipsession.IpSession;
import dev.aswitch.antivpnplugin.api.ipsession.IpSessionManager;
import dev.aswitch.antivpnplugin.api.profile.Profile;
import dev.aswitch.antivpnplugin.api.profile.ProfileManager;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.spigot.commands.OtterCommand;
import dev.aswitch.antivpnplugin.spigot.events.PlayerJoinEventListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class AntiVPNSpigot extends JavaPlugin {

    @Getter
    private static AntiVPNSpigot instance;

    private ExecutorService executorService;
    private OtterApi otterApi;
    private ProfileManager profileManager;
    private IpSessionManager ipSessionManager;

    private PluginLogger pluginLogger;

    @Override
    public void onEnable() {
        instance = this;
        pluginLogger = getPluginLogger();
        otterApi = new OtterApi();
        ipSessionManager = new IpSessionManager();
        executorService = Executors.newSingleThreadExecutor();
        profileManager = new ProfileManager();

        saveResource("config.yml", false);
        loadConfigSettings();

        for (Player player : Bukkit.getOnlinePlayers()) {
            profileManager.add(player.getUniqueId());

            Profile profile = profileManager.get(player.getUniqueId());
            String ip = player.getAddress().getAddress().getHostAddress();

            IpSession ipSession = AntiVPNSpigot.getInstance().getIpSessionManager().get(ip);
            if (ipSession == null) {
                AntiVPNSpigot.getInstance().getIpSessionManager().add(ip, profile);
                ipSession = AntiVPNSpigot.getInstance().getIpSessionManager().get(ip);
            } else {
                ipSession.getConnected().add(profile);
            }
            profile.setIpSession(ipSession);
        }

        getCommand("otter").setExecutor(new OtterCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventListener(), this);

    }

    @Override
    public void onDisable() {

    }

    public void loadConfigSettings() {
        reloadConfig();

        if (getConfig().getString("license") == null || getConfig().getString("license").equals("none")) {
            String uuid = UUID.randomUUID().toString();
            getConfig().set("license", uuid);
            saveConfig();
        }

        Settings.LICENSE = getConfig().getString("license");

        Settings.ALERTS_ENABLED = getConfig().getBoolean("antiVPN.alerts.enabled");
        Settings.CONSOLE_ALERTS = getConfig().getBoolean("antiVPN.alerts.console");
        Settings.ALERT_MESSAGE = getConfig().getString("antiVPN.punishment.message");

        Settings.KICK_PLAYERS = getConfig().getBoolean("antiVPN.punishment.enabled");
        Settings.OP_BYPASS = getConfig().getBoolean("antiVPN.punishment.enabled");
        Settings.KICK_MESSAGE = getConfig().getString("antiVPN.punishment.message");

        Settings.IP_LIMIT_ENABLED = getConfig().getBoolean("ipLimiter.enabled");
        Settings.MAX_IP_CONNECTIONS = getConfig().getInt("ipLimiter.limit");
        Settings.IP_LIMIT_KICK_MESSAGE = getConfig().getString("ipLimiter.message");

    }

}
