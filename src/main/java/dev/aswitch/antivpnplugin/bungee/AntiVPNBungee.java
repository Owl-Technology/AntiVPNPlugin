package dev.aswitch.antivpnplugin.bungee;

import dev.aswitch.antivpnplugin.api.OtterApi;
import dev.aswitch.antivpnplugin.api.ipsession.IpSessionManager;
import dev.aswitch.antivpnplugin.api.profile.ProfileManager;
import dev.aswitch.antivpnplugin.bungee.events.JoinEvent;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;

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


    @Override
    public void onEnable() {
        instance = this;

        pluginLogger = getPluginLogger();
        otterApi = new OtterApi();
        executorService = Executors.newSingleThreadExecutor();
        profileManager = new ProfileManager();

        getProxy().getPluginManager().registerListener(this, new JoinEvent());

    }

    @Override
    public void onDisable() {

    }

}
