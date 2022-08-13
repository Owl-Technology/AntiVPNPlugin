package dev.aswitch.antivpnplugin.bungee;

import dev.aswitch.antivpnplugin.api.profile.ProfileManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.ExecutorService;

@Getter
public final class AntiVPNBungee extends Plugin {

    private static AntiVPNBungee instance;

    private ExecutorService executorService;

    private ProfileManager profileManager;


    @Override
    public void onEnable() {
        System.out.println("[Otter] we currently do not support bungeecord");
    }

    @Override
    public void onDisable() {

    }

}
