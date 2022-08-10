package dev.aswitch.antivpnplugin.spigot;

import dev.aswitch.antivpnplugin.api.OtterApi;
import dev.aswitch.antivpnplugin.api.profile.ProfileManager;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import dev.aswitch.antivpnplugin.spigot.commands.OtterCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class AntiVPNSpigot extends JavaPlugin {

    @Getter
    private static AntiVPNSpigot instance;

    private ExecutorService executorService;
    private OtterApi otterApi;
    private ProfileManager profileManager;

    @Override
    public void onEnable() {
        instance = this;
        otterApi = new OtterApi();
        executorService = Executors.newSingleThreadExecutor();
        profileManager = new ProfileManager();

        saveResource("config.yml", false);
        loadConfigSettings();

        for (Player player : Bukkit.getOnlinePlayers()) {
            profileManager.add(player.getUniqueId());
        }

        getCommand("otter").setExecutor(new OtterCommand());

    }

    @Override
    public void onDisable() {

    }

    public void loadConfigSettings() {
        reloadConfig();
        Settings.LICENSE = getConfig().getString("license");

        Settings.KICK_MESSAGE = getConfig().getString("kicks.message");
        Settings.KICK_PLAYERS = getConfig().getBoolean("kicks.enabled");
        Settings.OP_BYPASS = getConfig().getBoolean("kicks.op-bypass");

        Settings.ALERTS_ENABLED = getConfig().getBoolean("alerts.enabled");
        Settings.ALERT_MESSAGE = getConfig().getString("alerts.message");
    }

}
