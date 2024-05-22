package com.pepperish.customteleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class LocationManager {

    private static final Set<Location> availableBlockLocations = new HashSet<>();

    private static final Map<UUID, Location> occupiedBlockLocations = new HashMap<>();

    private static final Map<UUID, Location> originalPlayerLocations = new HashMap<>();


    public LocationManager(Set<Location> blockLocations) { availableBlockLocations.addAll(blockLocations); }

    public LocationManager() {}

    public Optional<Location> getNextAvailableLocation(Player player) {
        if(availableBlockLocations.isEmpty()) return Optional.empty();
        Iterator<Location> iterator = availableBlockLocations.iterator();
        Location result = iterator.next();
        iterator.remove();
        occupiedBlockLocations.put(player.getUniqueId(), result);
        return Optional.of(result);
    }

    public Optional<Location> getReturnLocation(Player player) {
        Location result = occupiedBlockLocations.remove(player.getUniqueId());
        return result == null ? Optional.empty() : Optional.of(result);
    }

    public boolean addAvailableBlockLocation(Location location) { return availableBlockLocations.add(location); }

    public boolean removeAvailableBlockLocation(Location location) { return availableBlockLocations.remove(location); }

    public Set<Location> getAllBlockLocations() {
        Set<Location> result = new HashSet<>(availableBlockLocations);
        result.addAll(occupiedBlockLocations.values());
        return result;
    }

    public void refresh() {
        availableBlockLocations.addAll(occupiedBlockLocations.values());
        originalPlayerLocations.clear();
        occupiedBlockLocations.clear();
    }
}
