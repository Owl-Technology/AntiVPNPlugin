package dev.aswitch.antivpnplugin.bungee;

import dev.aswitch.antivpnplugin.api.OtterApi;
import dev.aswitch.antivpnplugin.api.ipsession.IpSessionManager;
import dev.aswitch.antivpnplugin.api.profile.ProfileManager;
import dev.aswitch.antivpnplugin.api.utils.ConfigUtil;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.bungee.events.JoinEvent;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public final class AntiVPNBungee extends Plugin {

    @Getter
    private static AntiVPNBungee instance;

    private ExecutorService executorService;
    private OtterApi otterApi;
    private ProfileManager profileManager;
    private PluginLogger pluginLogger;

    private IpSessionManager ipSessionManager;

    private ConfigUtil configUtil;


    @Override
    public void onEnable() {
        instance = this;

        configUtil = new ConfigUtil("config.yml", getDataFolder());

        pluginLogger = getPluginLogger();
        otterApi = new OtterApi();
        ipSessionManager = new IpSessionManager();
        executorService = Executors.newSingleThreadExecutor();
        profileManager = new ProfileManager();

        getProxy().getPluginManager().registerListener(this, new JoinEvent());

        try {
            loadConfig();
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public void onDisable() {

    }

    private void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }


        File configFile = new File(getDataFolder().getPath(), "config.yml");
        if (configFile.exists()) {
            configUtil.loadConfig();
        } else {
            configUtil.loadConfig();

            String uuid = UUID.randomUUID().toString();
            configUtil.getConfiguration().set("license", uuid);

            // antivpn
            configUtil.getConfiguration().set("antiVPN.enabled", true);
            configUtil.getConfiguration().set("antiVPN.alerts.enabled", true);
            configUtil.getConfiguration().set("antiVPN.alerts.console", true);
            configUtil.getConfiguration().set("antiVPN.alerts.message", "&7[&3Owl&7] &f%player% &7has joint on a &fVPN/proxy");
            configUtil.getConfiguration().set("antiVPN.punishment.enabled", true);
            configUtil.getConfiguration().set("antiVPN.punishment.opBypass", true);
            configUtil.getConfiguration().set("antiVPN.punishment.message", "&cVPNs/Proxies are not enabled on this server!");

            // ip limiter

            configUtil.getConfiguration().set("ipLimiter.enabled", true);
            configUtil.getConfiguration().set("ipLimiter.limit", 2);
            configUtil.getConfiguration().set("ipLimiter.message", "&cYou have reached your IP connection limit!");

            configUtil.save();
            configUtil.loadConfig();
        }

        Settings.LICENSE = configUtil.getConfiguration().getString("license");

        Settings.ALERTS_ENABLED = configUtil.getConfiguration().getBoolean("antiVPN.alerts.enabled");
        Settings.CONSOLE_ALERTS = configUtil.getConfiguration().getBoolean("antiVPN.alerts.console");
        Settings.ALERT_MESSAGE = configUtil.getConfiguration().getString("antiVPN.punishment.message");

        Settings.KICK_PLAYERS = configUtil.getConfiguration().getBoolean("antiVPN.punishment.enabled");
        Settings.OP_BYPASS = configUtil.getConfiguration().getBoolean("antiVPN.punishment.enabled");
        Settings.KICK_MESSAGE = configUtil.getConfiguration().getString("antiVPN.punishment.message");

        Settings.IP_LIMIT_ENABLED = configUtil.getConfiguration().getBoolean("ipLimiter.enabled");
        Settings.MAX_IP_CONNECTIONS = configUtil.getConfiguration().getInt("ipLimiter.limit");
        Settings.IP_LIMIT_KICK_MESSAGE = configUtil.getConfiguration().getString("ipLimiter.message");

        System.out.println("License: " + Settings.LICENSE);

    }

}
