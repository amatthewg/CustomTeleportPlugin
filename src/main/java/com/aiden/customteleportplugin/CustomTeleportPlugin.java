package com.aiden.customteleportplugin;

import com.aiden.customteleportplugin.commands.CommandManager;
import com.aiden.customteleportplugin.listeners.HandlePlayerCommand;
import com.aiden.customteleportplugin.listeners.HandlePlayerDisconnect;
import com.aiden.customteleportplugin.listeners.HandlePlayerJoin;
import com.aiden.customteleportplugin.listeners.HandleToolUse;
import com.aiden.customteleportplugin.managers.FileManager;
import com.aiden.customteleportplugin.managers.BlockLocationManager;
import com.aiden.customteleportplugin.managers.TeleportManager;
import com.aiden.customteleportplugin.managers.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;


public final class CustomTeleportPlugin extends JavaPlugin {

    private static boolean errorOnStartup = false;

    private static BlockLocationManager locationManager = null;

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        getLogger().info("\n" +
                " ________      ___  ___      ________       _________    ________      _____ ______                         \n" +
                "|\\   ____\\    |\\  \\|\\  \\    |\\   ____\\     |\\___   ___\\ |\\   __  \\    |\\   _ \\  _   \\                       \n" +
                "\\ \\  \\___|    \\ \\  \\\\\\  \\   \\ \\  \\___|_    \\|___ \\  \\_| \\ \\  \\|\\  \\   \\ \\  \\\\\\__\\ \\  \\                      \n" +
                " \\ \\  \\        \\ \\  \\\\\\  \\   \\ \\_____  \\        \\ \\  \\   \\ \\  \\\\\\  \\   \\ \\  \\\\|__| \\  \\                     \n" +
                "  \\ \\  \\____    \\ \\  \\\\\\  \\   \\|____|\\  \\        \\ \\  \\   \\ \\  \\\\\\  \\   \\ \\  \\    \\ \\  \\                    \n" +
                "   \\ \\_______\\   \\ \\_______\\    ____\\_\\  \\        \\ \\__\\   \\ \\_______\\   \\ \\__\\    \\ \\__\\                   \n" +
                "    \\|_______|    \\|_______|   |\\_________\\        \\|__|    \\|_______|    \\|__|     \\|__|                   \n" +
                "                               \\|_________|                                                                 \n" +
                "                                                                                                            \n" +
                "                                                                                                            \n" +
                " _________    _______       ___           _______       ________    ________      ________      _________   \n" +
                "|\\___   ___\\ |\\  ___ \\     |\\  \\         |\\  ___ \\     |\\   __  \\  |\\   __  \\    |\\   __  \\    |\\___   ___\\ \n" +
                "\\|___ \\  \\_| \\ \\   __/|    \\ \\  \\        \\ \\   __/|    \\ \\  \\|\\  \\ \\ \\  \\|\\  \\   \\ \\  \\|\\  \\   \\|___ \\  \\_| \n" +
                "     \\ \\  \\   \\ \\  \\_|/__   \\ \\  \\        \\ \\  \\_|/__   \\ \\   ____\\ \\ \\  \\\\\\  \\   \\ \\   _  _\\       \\ \\  \\  \n" +
                "      \\ \\  \\   \\ \\  \\_|\\ \\   \\ \\  \\____    \\ \\  \\_|\\ \\   \\ \\  \\___|  \\ \\  \\\\\\  \\   \\ \\  \\\\  \\|       \\ \\  \\ \n" +
                "       \\ \\__\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\   \\ \\__\\      \\ \\_______\\   \\ \\__\\\\ _\\        \\ \\__\\\n" +
                "        \\|__|    \\|_______|    \\|_______|    \\|_______|    \\|__|       \\|_______|    \\|__|\\|__|        \\|__|\n" +
                "                                                                                                            \n" +
                "                                                                                                            \n" +
                "                                                                                                            ");
        plugin = this;
        saveDefaultConfig();
        FileManager fileManager = new FileManager(this);
        TeleportManager teleportManager = new TeleportManager(this);
        WorldManager worldManager;
        try {
            World configuredWorld = Bukkit.getWorld(getConfig().getString("world-name"));
            if(configuredWorld == null) throw new NullPointerException();
            worldManager = new WorldManager(configuredWorld);
        } catch(NullPointerException e) {
            getLogger().log(Level.SEVERE, "FATAL: Failed to find the specified world (config.yml: 'world-name'");
            deactivatePlugin();
            return;
        }
        getServer().getPluginManager().registerEvents(new HandlePlayerCommand(this), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerDisconnect(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new HandleToolUse(), this);
        try {
            getCommand("ctp").setExecutor(new CommandManager(this));
        } catch(NullPointerException e) {
            getLogger().log(Level.SEVERE, "FATAL: Failed to initialize command 'ctp'");
            deactivatePlugin();
            return;
        }
        Optional<Boolean> saveFileWasCreatedOptional = fileManager.createFileIfNotExists();
        if(saveFileWasCreatedOptional.isPresent()) {
            boolean saveFileWasCreated = saveFileWasCreatedOptional.get();
            if(saveFileWasCreated) {
                locationManager = new BlockLocationManager(new HashSet<>());
            }
            else {
                Optional<Set<Location>> optionalLocationSet = fileManager.loadSetFromFile();
                if(optionalLocationSet.isPresent()) {
                    Set<Location> loadedLocations = optionalLocationSet.get();
                    // Check these locations, make sure they all match the specified world in config.yml
                    if(!worldManager.locationSetIsValid(loadedLocations)) {
                        getLogger().log(Level.SEVERE, "FATAL: Tried to load saved block locations that are not inside the world specified in the config.yml: 'world-name'");
                        deactivatePlugin();
                        return;
                    }
                    locationManager = new BlockLocationManager(optionalLocationSet.get());
                }
                else {
                    getLogger().log(Level.SEVERE, "FATAL: The plugin failed to load the save file on startup!");
                    deactivatePlugin();
                }
            }
        } else {
            getLogger().log(Level.SEVERE, "FATAL: The plugin failed to load the save file on startup!");
            deactivatePlugin();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Custom Teleport!");
        if(errorOnStartup) return;
        TeleportManager tpManager = new TeleportManager();
        tpManager.returnAllPlayers();
        locationManager = new BlockLocationManager();
        locationManager.refresh();
        FileManager fileManager = new FileManager();
        Set<Location> allBlockLocations = locationManager.getAllBlockLocations();
        fileManager.saveSetToFile(allBlockLocations);
    }

    public static void deactivatePlugin() {
        errorOnStartup = true;
        HandlerList.unregisterAll(plugin);
        plugin.getCommand("ctp").setExecutor(null);
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
}
