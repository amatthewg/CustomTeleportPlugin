package com.pepperish.customteleportplugin.DAL;

import org.bukkit.Location;

import java.util.*;

// Class used to handle the storage of clicked block locations
public class BlockStorage {
    private static Set<Location> blockLocations = null;

    public BlockStorage(Set<Location> blockLocations) {
        BlockStorage.blockLocations = Collections.synchronizedSet(blockLocations);
    }
    public BlockStorage() {

    }
    public boolean addToBlockLocations(Location location) {
        return blockLocations.add(location);
    }

    public boolean removeFromTeleportLocations(Location location) {
        return blockLocations.remove(location);

    }

    public Set<Location> getBlockLocations() { return blockLocations; }

    public Map<Location, UUID> getBlockLocationsAsMap() {
        Map<Location, UUID> result = new HashMap<>();
        for(Location location : blockLocations) {
            result.put(location, null);
        }
        return result;
    }
}
