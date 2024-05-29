package com.aiden.customteleportplugin.listeners;

import com.aiden.customteleportplugin.managers.TeleportManager;
import com.aiden.customteleportplugin.util.CommandState;
import com.aiden.customteleportplugin.managers.BlockLocationManager;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive.TpAllCommand;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import com.aiden.customteleportplugin.util.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HandlePlayerJoin implements Listener {

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static final BlockLocationManager locationManager = new BlockLocationManager();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final TeleportManager tpFreezeManager = new TeleportManager();

    public HandlePlayerJoin() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CommandState tpAllCommandState = new TpAllCommand().getCommandState();
        if(tpAllCommandState.equals(CommandState.NOT_CURRENTLY_EXECUTED)) return;
        Player joiningPlayer = e.getPlayer();
        if (!joiningPlayer.hasPermission(shouldBeTeleportedPermission)) return;
        if(!tpFreezeManager.tryTpPlayer(joiningPlayer)) {
            chatMessenger.messageAdmins(String.format("&cWARNING: Player &f%s &cjoined but couldn't\n" +
                    "&cbe teleported because there weren't enough set block locations!", joiningPlayer.getName()));
        }
    }

}
