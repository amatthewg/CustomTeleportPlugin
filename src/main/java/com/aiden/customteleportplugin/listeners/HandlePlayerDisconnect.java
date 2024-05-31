package com.aiden.customteleportplugin.listeners;

import com.aiden.customteleportplugin.managers.TeleportManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandlePlayerDisconnect implements Listener {

    private static final TeleportManager tpManager = new TeleportManager();

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        tpManager.tryReturnPlayer(e.getPlayer());
        /* Not sure whether the teleportation of an offline player will be cancelled by HandlePlayerMove.
        Either way, if the player rejoins after the event is over, they may just spawn in their past seating location
        which shouldn't break anything
         */
    }

}
