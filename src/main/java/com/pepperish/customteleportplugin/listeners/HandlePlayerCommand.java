package com.pepperish.customteleportplugin.listeners;

import com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive.TpAllCommand;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.util.CommandState;
import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HandlePlayerCommand implements Listener {

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();
    private static final Set<String> allowedCommands = new HashSet<>();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static String commandNotAllowedMessage = null;

    private static JavaPlugin plugin;
    public HandlePlayerCommand(JavaPlugin pl) {
        plugin = pl;
        plugin.getConfig().getStringList("allowed-commands").forEach(cmd -> allowedCommands.add("/" + cmd));
        commandNotAllowedMessage = plugin.getConfig().getString("command-not-allowed-message");
    }

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if(new TpAllCommand().getCommandState().equals(CommandState.NOT_CURRENTLY_EXECUTED)) return;
        Player player = event.getPlayer();
        if(!player.hasPermission(shouldBeTeleportedPermission)) return;
        String command = event.getMessage().split(" ")[0];
        if(allowedCommands.contains(command)) return;
        event.setCancelled(true);
        chatMessenger.sendChat(player, commandNotAllowedMessage);
    }
}
