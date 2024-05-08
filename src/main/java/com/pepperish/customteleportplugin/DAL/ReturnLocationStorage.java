package com.pepperish.customteleportplugin.DAL;

import org.bukkit.Location;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

// Class used to store each player's location prior to being teleported, so that they may be returned to their original
// location
public class ReturnLocationStorage {
    private static Map<UUID, Location> returnLocationMap = null;

    public ReturnLocationStorage(Map<UUID, Location>  set) {
        returnLocationMap = set;
    }
    public ReturnLocationStorage() {}

    public void addToStorage(UUID uuid, Location location) {
        returnLocationMap.put(uuid, location);
    }

    public void clearStorage() {
        returnLocationMap.clear();
    }


}
