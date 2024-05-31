package com.aiden.customteleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class aManager {

    private static final Set<Location> availableBlockLocations = Collections.synchronizedSet(new HashSet<>());

    private static final Map<UUID, Location> occupiedBlockLocations = new HashMap<>();

    private static final Map<UUID, Location> originalPlayerLocations = new HashMap<>();

    private static Location centralViewingLocation = null;

    public aManager(Set<Location> blockLocations, JavaPlugin pl) {
        availableBlockLocations.addAll(blockLocations);
        String rawCenterLocationString = pl.getConfig().getString("center-viewing-block");
        if(rawCenterLocationString == null) {
            return;
        }
        String[] rawCenterLocationCoords = rawCenterLocationString.split(",");
        double x = Double.parseDouble(rawCenterLocationCoords[0]);
        double y = Double.parseDouble(rawCenterLocationCoords[1]);
        double z = Double.parseDouble(rawCenterLocationCoords[2]);
        centralViewingLocation = new Location(WorldManager.getWorld(), x+0.5, y+0.5, z+0.5);
    }

    public aManager() {}

    public Optional<Location> getNextAvailableLocation(Player player) {
        if(availableBlockLocations.isEmpty()) return Optional.empty();
        Iterator<Location> iterator = availableBlockLocations.iterator();
        Location result = iterator.next();
        iterator.remove();
        occupiedBlockLocations.put(player.getUniqueId(), result);
        originalPlayerLocations.put(player.getUniqueId(), player.getLocation());
        return Optional.of(result.add(0.5, 1, 0.5));
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

    // Not implemented yet
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
}
