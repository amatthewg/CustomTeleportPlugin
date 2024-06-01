package com.aiden.customteleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Set;

public class WorldManager {

    private static World world = null;

    public WorldManager(World w) { world = w; }

    public WorldManager() {}

    public boolean isCorrectWorld(World w) { return world == w; }

    public boolean locationSetIsValid(Set<Location> set) { return set.stream().allMatch(loc -> loc.getWorld() == world); }

    public String getWorldName() { return world.getName(); }

}
