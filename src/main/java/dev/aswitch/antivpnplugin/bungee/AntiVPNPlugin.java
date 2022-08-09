package dev.aswitch.antivpnplugin.bungee;

import dev.aswitch.antivpnplugin.api.profile.ProfileManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.ExecutorService;

@Getter
public final class AntiVPNPlugin extends Plugin {

    private static AntiVPNPlugin instance;

    private ExecutorService executorService;

    private ProfileManager profileManager;


    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
