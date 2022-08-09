package dev.aswitch.antivpnplugin.api.profile;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
public class Profile {

    private final UUID uuid;
    private boolean alerts;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        this.alerts = true;
    }

    public boolean toggleAlerts() {
        alerts = !alerts;
        return alerts;
    }
}
