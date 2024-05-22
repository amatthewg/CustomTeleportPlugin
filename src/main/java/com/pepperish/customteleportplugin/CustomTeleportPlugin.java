package com.pepperish.customteleportplugin;

import com.pepperish.customteleportplugin.managers.FileManager;
import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.CommandManager;
import com.pepperish.customteleportplugin.listeners.HandlePlayerDisconnect;
import com.pepperish.customteleportplugin.listeners.HandlePlayerJoin;
import com.pepperish.customteleportplugin.listeners.HandleToolUse;
import com.pepperish.customteleportplugin.managers.TeleportFreezeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;


public final class CustomTeleportPlugin extends JavaPlugin {


    private static LocationManager locationManager = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FileManager fileManager = new FileManager(this);
        TeleportFreezeManager teleportManager = new TeleportFreezeManager(this);

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
        TeleportFreezeManager tpManager = new TeleportFreezeManager();
        tpManager.returnAllPlayers(null);
        locationManager = new LocationManager();
        locationManager.refresh();
        FileManager fileManager = new FileManager();
        Set<Location> allBlockLocations = locationManager.getAllBlockLocations();
        fileManager.saveSetToFile(allBlockLocations);
    }
}
