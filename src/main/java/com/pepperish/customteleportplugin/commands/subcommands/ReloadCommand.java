package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.managers.FileManager;
import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.CommandManager;
import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.listeners.HandlePlayerDisconnect;
import com.pepperish.customteleportplugin.listeners.HandlePlayerJoin;
import com.pepperish.customteleportplugin.listeners.HandleToolUse;
import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ReloadCommand extends SubCommand {

    private static JavaPlugin plugin;
    private static FileManager fileManager = new FileManager();
    private static LocationManager locationManager = new LocationManager();

    public ReloadCommand(JavaPlugin pl) {
        plugin = pl;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Save all block locations and reload the plugin";
    }

    @Override
    public String getSyntax() {
        return "/ctp reload";
    }

    @Override
    public String getPermissionString() {
        return Permission.CTP_ADMIN.getString();
    }

    @Override
    public void perform(Player sender, String[] args) {
        locationManager.refresh();
        List<Location> allBlockLocations = locationManager.getAllBlockLocations();
        fileManager.saveSetToFile(allBlockLocations);
        plugin.reloadConfig();
        unregisterListeners();
        registerListeners();
        unregisterCommands();
        registerCommands();

    }


    private void unregisterListeners() {
        HandlerList.unregisterAll(plugin);
    }
    private void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new HandleToolUse(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HandlePlayerJoin(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HandlePlayerDisconnect(), plugin);
    }
    private void unregisterCommands() {
        plugin.getCommand("ctp").setExecutor(null);
    }
    private void registerCommands() {
        plugin.getCommand("ctp").setExecutor(new CommandManager(plugin));
    }
}
