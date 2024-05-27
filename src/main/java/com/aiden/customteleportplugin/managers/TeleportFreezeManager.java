package com.aiden.customteleportplugin.managers;

import com.aiden.customteleportplugin.util.Permission;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;

public class TeleportFreezeManager {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final BlockLocationManager locationManager = new BlockLocationManager();

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static Boolean playersShouldBeMessagedOnTeleport = null;

    private static Boolean playersShouldBeMessagedOnReturn = null;

    private static List<String> onTpMessages = null;

    private static List<String> onReturnMessages = null;

    public TeleportFreezeManager(JavaPlugin pl) {
        playersShouldBeMessagedOnTeleport = pl.getConfig().getBoolean("message-players-on-tp");
        playersShouldBeMessagedOnReturn = pl.getConfig().getBoolean("message-players-on-return");
        onTpMessages = pl.getConfig().getStringList("on-tp-messages");
        onReturnMessages = pl.getConfig().getStringList("on-return-messages");
    }

    public TeleportFreezeManager() {}

    public Optional<Integer> tpAllPlayers() {
        int teleportedCount = 0;
        boolean allTeleported = true;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!p.hasPermission(shouldBeTeleportedPermission)) continue;
            if(!tryTpPlayer(p)) {
                allTeleported = false;
                break;
            }
            teleportedCount++;
        }
        if(allTeleported) return Optional.of(teleportedCount);
        else return Optional.empty();
    }

    public boolean tryTpPlayer(Player player) {
        Optional<Location> destinationOptional = locationManager.getNextAvailableLocation(player);
        if(destinationOptional.isPresent()) {
            player.teleportAsync(destinationOptional.get().add(0, 1, 0)).thenAccept(success -> {
                player.setWalkSpeed(0);
                player.setFlySpeed(0);
                player.setJumping(false);
                if(playersShouldBeMessagedOnTeleport) {
                    chatMessenger.sendChat(player, onTpMessages);
                }
                // TODO handle failed teleportation
            });
            return true;
        }
        return false;
    }

    public int returnAllPlayers(Player commandSender) {
        int returnedCount = 0;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!p.hasPermission(shouldBeTeleportedPermission)) continue;
            if(tryReturnPlayer(p)) returnedCount++;
        }
        locationManager.refresh();
        return returnedCount;
    }

    public boolean tryReturnPlayer(Player player) {
        Optional<Location> returnLocationOptional = locationManager.getReturnLocation(player);
        if(returnLocationOptional.isPresent()) {
            player.teleportAsync(returnLocationOptional.get()).thenAccept(success -> {
                player.setWalkSpeed(1);
                player.setFlySpeed(1);
                player.setJumping(true);
                if(playersShouldBeMessagedOnReturn) {
                    chatMessenger.sendChat(player, onReturnMessages);
                }
                // TODO handle failed teleportation
            });
            return true;
        }
        return false;
    }
}
