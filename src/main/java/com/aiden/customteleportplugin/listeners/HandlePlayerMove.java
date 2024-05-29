package com.aiden.customteleportplugin.listeners;

import com.aiden.customteleportplugin.managers.TeleportManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class HandlePlayerMove implements Listener {

    // Event will be registered by the Tpall command, and unregistered by the Return command
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(!TeleportManager.playerIsTeleported(e.getPlayer())) return;
        if(e.hasExplicitlyChangedPosition()) e.setCancelled(true);
    }

}
