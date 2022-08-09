package dev.aswitch.antivpnplugin.api.profile;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class ProfileManager {

    private final HashMap<UUID, Profile> profileHashMap;

    public ProfileManager() {
        this.profileHashMap = new HashMap<>();
    }

    public void add(UUID uuid) {
        profileHashMap.put(uuid, new Profile(uuid));
    }

    public void remove(UUID uuid) {
        profileHashMap.remove(uuid);
    }

    public Profile get(UUID uuid) {
        return profileHashMap.getOrDefault(uuid, null);
    }

    public List<Profile> toList() {
        return new ArrayList<>(profileHashMap.values());
    }

}
