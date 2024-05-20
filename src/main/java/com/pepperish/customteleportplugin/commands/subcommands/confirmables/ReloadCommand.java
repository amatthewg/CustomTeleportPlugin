package com.pepperish.customteleportplugin.commands.subcommands.confirmables;

import com.pepperish.customteleportplugin.managers.FileManager;
import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.CommandManager;
import com.pepperish.customteleportplugin.listeners.HandlePlayerDisconnect;
import com.pepperish.customteleportplugin.listeners.HandlePlayerJoin;
import com.pepperish.customteleportplugin.listeners.HandleToolUse;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.state.TeleportState;
import com.pepperish.customteleportplugin.state.TeleportStateManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class ReloadCommand extends ConfirmableSubcommand {

    private static JavaPlugin plugin;

    private static FileManager fileManager = new FileManager();
    private static LocationManager locationManager = new LocationManager();

    private static TeleportStateManager tpStateManaager = new TeleportStateManager();

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

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
    public String getConfirmationMessage() {
        return "WARNING: You should only reload the plugin if no players are currently teleported to their blocks!";
    }



    @Override
    public void perform(Player sender, String[] args) {
        if(tpStateManaager.getCurrentTeleportState() == TeleportState.TPALL_ACTIVE) {
            chatMessenger.sendChat(sender, "&cCannot reload until &e/ctp return &cis executed!");
            return;
        }
        Set<Location> allBlockLocations = locationManager.getAllBlockLocations();
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
        plugin.getServer().getPluginManager().registerEvents(new HandleToolUse(), plugin);
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
