package dev.aswitch.antivpnplugin.api.ipsession;

import dev.aswitch.antivpnplugin.api.profile.Profile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IpSession {

    private final String ip;
    private final List<Profile> connected;

    public IpSession(String ip) {
        this.ip = ip;
        this.connected = new ArrayList<>();
    }

}
