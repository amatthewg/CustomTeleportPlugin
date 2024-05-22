package com.pepperish.customteleportplugin.listeners;


import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive.TpAllCommand;
import com.pepperish.customteleportplugin.managers.TeleportFreezeManager;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.util.CommandState;
import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;

/*
Class used to handle when all players have already been teleported to the church, and then a player
joins the server and must be teleported to an empty seat
*/
public class HandlePlayerJoin implements Listener {

    private static String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static LocationManager locationManager = new LocationManager();

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static TeleportFreezeManager tpManager = new TeleportFreezeManager();

    private static JavaPlugin plugin;

    public HandlePlayerJoin(JavaPlugin pl) {
        plugin = pl;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player joiningPlayer = e.getPlayer();
        CommandState tpAllCommandState = TpAllCommand.
        if (!TpAllCommand.getCommandIsActive()) return;
        if (!joiningPlayer.hasPermission(shouldBeTeleportedPermission)) return;

        Optional<Location> nextAvailableLocation = locationManager.getNextAvailableLocation(joiningPlayer);
        if(nextAvailableLocation.isPresent()) {
            tpManager.tpPlayer(joiningPlayer, nextAvailableLocation.get());
            boolean playersShouldBeMessagedOnTeleport = plugin.getConfig().getBoolean("message-players-on-tp");
            List<String> onTpMessages = plugin.getConfig().getStringList("on-tp-messages");
            if(playersShouldBeMessagedOnTeleport) {
                chatMessenger.sendChat(joiningPlayer, onTpMessages);
            }
        } else {
            chatMessenger.messageAdmins(String.format("&cWARNING: Player &f%s &cjoined but couldn't\n" +
                    "&cbe teleported because there weren't enough set block locations!", joiningPlayer.getName()));

        }
    }
}
