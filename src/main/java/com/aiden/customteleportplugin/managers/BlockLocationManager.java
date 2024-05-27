package com.aiden.customteleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class BlockLocationManager {

    private static final Set<Location> availableBlockLocations = new HashSet<>();

    private static final Map<UUID, Location> occupiedBlockLocations = new HashMap<>();

    private static final Map<UUID, Location> originalPlayerLocations = new HashMap<>();

    public BlockLocationManager(Set<Location> blockLocations) { availableBlockLocations.addAll(blockLocations); }

    public BlockLocationManager() {}

    public Optional<Location> getNextAvailableLocation(Player player) {
        if(availableBlockLocations.isEmpty()) return Optional.empty();
        Iterator<Location> iterator = availableBlockLocations.iterator();
        Location result = iterator.next();
        iterator.remove();
        occupiedBlockLocations.put(player.getUniqueId(), result);
        originalPlayerLocations.put(player.getUniqueId(), player.getLocation());
        return Optional.of(result);
    }

    public Optional<Location> getReturnLocation(Player player) {
        availableBlockLocations.add(occupiedBlockLocations.remove(player.getUniqueId()));
        Location result = originalPlayerLocations.remove(player.getUniqueId());
        return result == null ? Optional.empty() : Optional.of(result);
        /* Empty optional signifies that this specific player must have joined the server while the tpall
        command was active, but there weren't enough set block locations. Therefore, the player was not teleported
        in the first place, and does not have a return location
         */
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
