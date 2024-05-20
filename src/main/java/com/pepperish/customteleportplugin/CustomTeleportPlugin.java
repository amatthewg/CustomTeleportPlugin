package com.pepperish.customteleportplugin;

import com.pepperish.customteleportplugin.managers.FileManager;
import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.CommandManager;
import com.pepperish.customteleportplugin.listeners.HandlePlayerDisconnect;
import com.pepperish.customteleportplugin.listeners.HandlePlayerJoin;
import com.pepperish.customteleportplugin.listeners.HandleToolUse;
import com.pepperish.customteleportplugin.managers.TeleportManager;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.Set;
import java.util.Timer;


public final class CustomTeleportPlugin extends JavaPlugin {


    private static LocationManager locationManager = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FileManager fileManager = new FileManager(this);
        TeleportManager teleportManager = new TeleportManager(this);

        getServer().getPluginManager().registerEvents(new HandleToolUse(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerDisconnect(), this);

        getCommand("ctp").setExecutor(new CommandManager(this));

        if (!fileManager.createFileIfNotExists()) {
            Set<Location> loadedLocations = fileManager.loadSetFromFile();
            if(loadedLocations != null) { // If null signifies file exists but was empty
                locationManager = new LocationManager(loadedLocations);
            }
        }


    }
    @Override
    public void onDisable() {
        TeleportManager tpManager = new TeleportManager();
        tpManager.returnAllPlayers(null);
        locationManager = new LocationManager();
        locationManager.refresh();
        FileManager fileManager = new FileManager();
        Set<Location> allBlockLocations = locationManager.getAllBlockLocations();
        fileManager.saveSetToFile(allBlockLocations);
    }
}
