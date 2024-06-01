package com.aiden.customteleportplugin.listeners;

import com.aiden.customteleportplugin.managers.TeleportManager;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class HandlePlayerCommand implements Listener {

    private static final Set<String> allowedCommands = new HashSet<>();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static String commandNotAllowedMessage = null;

    public HandlePlayerCommand(JavaPlugin pl) {
        pl.getConfig().getStringList("enabled-commands").forEach(cmd -> allowedCommands.add("/" + cmd.toLowerCase()));
        commandNotAllowedMessage = pl.getConfig().getString("command-not-allowed-message");
    }

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        boolean playerIsTeleported = TeleportManager.playerIsTeleported(player);
        if(!playerIsTeleported) return;
        String command = event.getMessage().split(" ")[0].toLowerCase();
        if(allowedCommands.contains(command)) return;
        event.setCancelled(true);
        chatMessenger.sendChat(player, commandNotAllowedMessage);
    }

}
