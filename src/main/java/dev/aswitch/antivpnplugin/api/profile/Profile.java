package dev.aswitch.antivpnplugin.api.profile;

import dev.aswitch.antivpnplugin.api.ipsession.IpSession;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
public class Profile {

    private final UUID uuid;
    private final Player player;
    private boolean alerts;

    @Setter
    private IpSession ipSession;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.alerts = true;
    }

    public boolean toggleAlerts() {
        alerts = !alerts;
        return alerts;
    }
}
