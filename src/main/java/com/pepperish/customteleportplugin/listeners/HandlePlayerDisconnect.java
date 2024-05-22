package com.pepperish.customteleportplugin.listeners;

import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive.TpAllCommand;
import com.pepperish.customteleportplugin.util.Permission;
import com.pepperish.customteleportplugin.managers.TeleportFreezeManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


/*
Class used to handle when all players have been teleported to the church,
and then a teleported player disconnects, and the block they were on must now become available
 */
public class HandlePlayerDisconnect implements Listener {

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static final LocationManager locationManager = new LocationManager();

    private static final TeleportFreezeManager tpManager = new TeleportFreezeManager();

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        if(!TpAllCommand.getCommandIsActive()) return;
        Player quittingPlayer = e.getPlayer();
        if(!quittingPlayer.hasPermission(shouldBeTeleportedPermission)) return;

        Location returnLocation = locationManager.getReturnLocation(quittingPlayer);
        tpManager.returnPlayer(quittingPlayer, returnLocation);
    }
}
