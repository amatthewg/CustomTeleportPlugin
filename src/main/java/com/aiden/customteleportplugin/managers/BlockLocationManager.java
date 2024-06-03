package com.aiden.customteleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockLocationManager {

    private static final Set<Location> availableBlockLocations = new HashSet<>();

    private static final Map<UUID, Location> occupiedBlockLocations = new HashMap<>();

    private static final Map<Location, Material> originalBlocksMap = new HashMap<>();

    private static boolean blockViewingEnabled = false;

    public static int setBlockViewingEnabled(boolean state) {
        blockViewingEnabled = state;
        AtomicInteger blockCount = new AtomicInteger(0);
        if(blockViewingEnabled) {
            getAllBlockLocations().forEach(location -> {
                Block block = location.getBlock();
                originalBlocksMap.put(location, block.getType());
                block.setType(Material.RED_WOOL);
                blockCount.getAndIncrement();
            });
        }
        else {
            originalBlocksMap.forEach((key, val) -> {
                key.getBlock().setType(val);
                blockCount.incrementAndGet();
            });
            originalBlocksMap.clear();
        }
        return blockCount.get();
    }

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
        availableBlockLocations.add(occupiedBlockLocations.remove(player.getUniqueId()));
    }

    public boolean addAvailableBlockLocation(Location location) {
        boolean success = availableBlockLocations.add(location);
        if(success && blockViewingEnabled) {
            Block block = location.getBlock();
            originalBlocksMap.put(location, block.getType());
            block.setType(Material.RED_WOOL);
        }
        return success;
    }

    public boolean removeAvailableBlockLocation(Location location) {
        boolean success = availableBlockLocations.remove(location);
        if(success && blockViewingEnabled) {
            Block block = location.getBlock();
            block.setType(originalBlocksMap.remove(location));
        }
        return success;
    }

    public static Set<Location> getAllBlockLocations() {
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
