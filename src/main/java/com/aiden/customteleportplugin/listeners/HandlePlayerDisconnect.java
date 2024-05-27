package com.aiden.customteleportplugin.listeners;

import com.aiden.customteleportplugin.managers.TeleportFreezeManager;
import com.aiden.customteleportplugin.util.CommandState;
import com.aiden.customteleportplugin.managers.BlockLocationManager;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive.TpAllCommand;
import com.aiden.customteleportplugin.util.Permission;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class HandlePlayerDisconnect implements Listener {

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static final BlockLocationManager locationManager = new BlockLocationManager();

    private static final TeleportFreezeManager tpFreezeManager = new TeleportFreezeManager();

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        CommandState tpAllCommandState = new TpAllCommand().getCommandState();
        if(tpAllCommandState.equals(CommandState.NOT_CURRENTLY_EXECUTED)) return;
        tpFreezeManager.tryReturnPlayer(e.getPlayer());
    }

}
