package com.aiden.customteleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;

public class BlockLocationManager {

    private static final Set<Location> availableBlockLocations = new HashSet<>();

    private static final Map<UUID, Location> occupiedBlockLocations = new HashMap<>();

    public BlockLocationManager(Set<Location> blockLocations) { availableBlockLocations.addAll(blockLocations); }

    public BlockLocationManager() {}

    public Optional<Location> getNextAvailableLocation(Player player) {
        if(availableBlockLocations.isEmpty()) return Optional.empty();
        Iterator<Location> iterator = availableBlockLocations.iterator();
        Location result = iterator.next();
        iterator.remove();
        occupiedBlockLocations.put(player.getUniqueId(), result);
        return Optional.of(result);
    }

    public void handlePlayerReturn(Player player) {
        System.out.printf("**Called handlePlayerReturn** Result: ");
        availableBlockLocations.add(occupiedBlockLocations.remove(player.getUniqueId()));
    }

    public boolean addAvailableBlockLocation(Location location) { return availableBlockLocations.add(location); }

    public boolean removeAvailableBlockLocation(Location location) { return availableBlockLocations.remove(location); }

    public Set<Location> getAllBlockLocations() {
        Set<Location> result = new HashSet<>(availableBlockLocations);
        result.addAll(occupiedBlockLocations.values());
        return result;
    }

    /* Unnecessarily complex for version 1.0, will implement in future version
    private Location calculateAlteredDestinationForCenteringViewpoint(Location unalteredDestination) {
        Vector direction = centralViewingLocation.toVector().subtract(unalteredDestination.toVector()).normalize();
        double dx = direction.getX();
        double dz = direction.getZ();
        double dy = direction.getY();
        double yaw = Math.atan2(-dx, dz);
        yaw = Math.toDegrees(yaw);
        double horizontalDistance = Math.sqrt(dx*dx + dz*dz);
        double pitch = Math.atan2(dy, horizontalDistance);
        pitch = Math.toDegrees(pitch);
        Location result = unalteredDestination.clone();
        result.setYaw((float) yaw);
        result.setPitch((float) pitch);
        return result;
    }

     */
}
