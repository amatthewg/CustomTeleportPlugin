package com.pepperish.customteleportplugin.listeners;


import com.pepperish.customteleportplugin.commands.subcommands.TpAllCommand;
import com.pepperish.customteleportplugin.permissions.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
Class used to handle when all players have already been teleported to the church, and then a player
joins the server and must be teleported to an empty seat

This will also account for if a player has already been teleported to the church, and then they disconnect
and rejoin.
 */
public class HandlePlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // First, check if the teleport is active
        if(!TpAllCommand.commandIsActive()) {
            return;
        }
        // Check if player should be teleported
        Player player = e.getPlayer();
        if(player.hasPermission(Permission.SHOULD_BE_TELEPORTED.getPermission())) {
            // TODO find empty seat location and teleport player there
        }
    }
}
