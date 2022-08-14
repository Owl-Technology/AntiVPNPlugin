package dev.aswitch.antivpnplugin.api.ipsession;

import dev.aswitch.antivpnplugin.api.profile.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IpSessionManager {

    private final HashMap<String, IpSession> ipSessionHashMap;

    public IpSessionManager() {
        this.ipSessionHashMap = new HashMap<>();
    }

    public void add(String ip, Profile profile) {
        IpSession ipSession = get(ip);
        if (ipSession == null) {
            ipSession = new IpSession(ip);
            ipSession.getConnected().add(profile);
            ipSessionHashMap.put(ip, ipSession);
        } else {
            ipSession.getConnected().add(profile);
        }
    }

    public void remove(String ip, Profile profile) {
        final IpSession ipSession = get(ip);
        if (ipSession == null) return;

        ipSession.getConnected().remove(profile);
    }

    public IpSession get(String ip) {
        return ipSessionHashMap.getOrDefault(ip, null);
    }

    public List<IpSession> getSessions() {
        return new ArrayList<>(ipSessionHashMap.values());
    }


}
