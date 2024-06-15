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
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;


public final class CustomTeleportPlugin extends JavaPlugin {

    private static boolean errorOnStartup = false;

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        getLogger().info("\n" +
                " ________   ___  ___   ________   _________   ________   _____ ______                      \n" +
                "|\\   ____\\ |\\  \\|\\  \\ |\\   ____\\ |\\___   ___\\|\\   __  \\ |\\   _ \\  _   \\                    \n" +
                "\\ \\  \\___| \\ \\  \\\\\\  \\\\ \\  \\___|_\\|___ \\  \\_|\\ \\  \\|\\  \\\\ \\  \\\\\\__\\ \\  \\                   \n" +
                " \\ \\  \\     \\ \\  \\\\\\  \\\\ \\_____  \\    \\ \\  \\  \\ \\  \\\\\\  \\\\ \\  \\\\|__| \\  \\                  \n" +
                "  \\ \\  \\____ \\ \\  \\\\\\  \\\\|____|\\  \\    \\ \\  \\  \\ \\  \\\\\\  \\\\ \\  \\    \\ \\  \\                 \n" +
                "   \\ \\_______\\\\ \\_______\\ ____\\_\\  \\    \\ \\__\\  \\ \\_______\\\\ \\__\\    \\ \\__\\                \n" +
                "    \\|_______| \\|_______||\\_________\\    \\|__|   \\|_______| \\|__|     \\|__|                \n" +
                "                         \\|_________|                                                      \n" +
                "                                                                                           \n" +
                "                                                                                           \n" +
                " _________   _______    ___        _______    ________   ________   ________   _________   \n" +
                "|\\___   ___\\|\\  ___ \\  |\\  \\      |\\  ___ \\  |\\   __  \\ |\\   __  \\ |\\   __  \\ |\\___   ___\\ \n" +
                "\\|___ \\  \\_|\\ \\   __/| \\ \\  \\     \\ \\   __/| \\ \\  \\|\\  \\\\ \\  \\|\\  \\\\ \\  \\|\\  \\\\|___ \\  \\_| \n" +
                "     \\ \\  \\  \\ \\  \\_|/__\\ \\  \\     \\ \\  \\_|/__\\ \\   ____\\\\ \\  \\\\\\  \\\\ \\   _  _\\    \\ \\  \\  \n" +
                "      \\ \\  \\  \\ \\  \\_|\\ \\\\ \\  \\____ \\ \\  \\_|\\ \\\\ \\  \\___| \\ \\  \\\\\\  \\\\ \\  \\\\  \\|    \\ \\  \\ \n" +
                "       \\ \\__\\  \\ \\_______\\\\ \\_______\\\\ \\_______\\\\ \\__\\     \\ \\_______\\\\ \\__\\\\ _\\     \\ \\__\\\n" +
                "        \\|__|   \\|_______| \\|_______| \\|_______| \\|__|      \\|_______| \\|__|\\|__|     \\|__|\n" +
                "                                                                                           \n" +
                "                                                                                           \n" +
                "                                                                                           ");
        plugin = this;
        saveDefaultConfig();
        FileManager fileManager = new FileManager(this);
        TeleportManager teleportManager = new TeleportManager(this);
        try {
            initializeWorldManager();
            registerStartupEvents();
            initializeCtpCommand();
            saveFileStartup();
        } catch(Exception e) {
            e.printStackTrace();
            errorOnStartup = true;
            deactivatePlugin();
        }
        getServer().getMessenger().registerOutgoingPluginChannel(this, "ptpb:ptpb");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Custom Teleport!");
        if(errorOnStartup) return;
        TeleportManager tpManager = new TeleportManager();
        tpManager.returnAllPlayers();
        BlockLocationManager blockLocationManager = new BlockLocationManager();
        FileManager fileManager = new FileManager();
        BlockLocationManager.setBlockViewingEnabled(false);
        Set<Location> allBlockLocations = BlockLocationManager.getAllBlockLocations();
        fileManager.saveSetToFile(allBlockLocations);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

    private void initializeWorldManager() {
        String worldName = getConfig().getString("world-name");
        if(worldName == null) {
            getLogger().log(Level.SEVERE,
                    "FATAL: Entry 'world-name' is missing in the config.yml");
            throw new RuntimeException();
        }
        World world = Bukkit.getWorld(worldName);
        if(world == null) {
            getLogger().log(Level.SEVERE, String.format(
                    "FATAL: Could not locate world '%s' specified in the config.yml", worldName));
            throw new RuntimeException();
        }
        WorldManager worldManager = new WorldManager(world);
    }

    private void registerStartupEvents() {
        getServer().getPluginManager().registerEvents(new HandlePlayerCommand(this), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerDisconnect(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new HandleToolUse(), this);
    }

    private void initializeCtpCommand() {
        PluginCommand command = getCommand("ctp");
        if(command == null) {
            getLogger().log(Level.SEVERE, "FATAL: Failed to initialize command 'ctp'");
            throw new NullPointerException();
        }
        command.setExecutor(new CommandManager(this));
    }

    private void saveFileStartup() throws FileNotFoundException {
        FileManager fileManager = new FileManager();
        Optional<Boolean> saveFileWasCreatedOptional = fileManager.createFileIfNotExists();
        if(saveFileWasCreatedOptional.isEmpty()) {
            getLogger().log(Level.SEVERE, "FATAL: Failed to load save file on startup");
            throw new FileNotFoundException();
        }
        boolean saveFileWasCreated = saveFileWasCreatedOptional.get();
        if(saveFileWasCreated) {
            BlockLocationManager blockLocationManager = new BlockLocationManager(new HashSet<>());
        }
        else {
            handleSetLoadedFromFile();
        }
    }

    private void handleSetLoadedFromFile() {
        FileManager fileManager = new FileManager();
        WorldManager worldManager = new WorldManager();
        Optional<Set<Location>> locationSetOptional = fileManager.loadSetFromFile();
        if(locationSetOptional.isEmpty()) {
            getLogger().log(Level.SEVERE, "FATAL: Failed to load save file on startup");
            throw new RuntimeException();
        }
        Set<Location> loadedLocations = locationSetOptional.get();
        boolean locationSetIsValid = worldManager.locationSetIsValid(loadedLocations);
        if(!locationSetIsValid) {
            getLogger().log(Level.SEVERE, "FATAL: " +
                    "Tried to load saved block locations that are not inside the world specified in the config.yml: 'world-name'");
            throw new RuntimeException();
        }
        BlockLocationManager blockLocationManager = new BlockLocationManager(loadedLocations);
    }

    public static void deactivatePlugin() {
        errorOnStartup = true;
        HandlerList.unregisterAll(plugin);
        plugin.getCommand("ctp").setExecutor(null);
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
}
