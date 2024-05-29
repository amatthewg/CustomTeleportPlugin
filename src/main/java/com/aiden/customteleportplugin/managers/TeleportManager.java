package com.aiden.customteleportplugin.managers;

import com.aiden.customteleportplugin.util.Permission;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class TeleportManager {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final BlockLocationManager locationManager = new BlockLocationManager();

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static final Set<UUID> teleportedPlayers = new HashSet<>();

    private static Boolean playersShouldBeMessagedOnTeleport = null;

    private static Boolean playersShouldBeMessagedOnReturn = null;

    private static List<String> onTpMessages = null;

    private static List<String> onReturnMessages = null;

    private static boolean playersAreTeleported = false;

    public static void addTeleportedPlayer(Player player) { teleportedPlayers.add(player.getUniqueId()); }

    public static void removeTeleportedPlayer(Player player) { teleportedPlayers.remove(player.getUniqueId()); }

    public static boolean playerIsTeleported(Player player) { return teleportedPlayers.contains(player.getUniqueId()); }

    public static boolean playersAreTeleported() { return playersAreTeleported; }

    private static void setPlayersAreTeleported(boolean state) { playersAreTeleported = state; }

    public TeleportManager(JavaPlugin pl) {
        playersShouldBeMessagedOnTeleport = pl.getConfig().getBoolean("message-players-on-tp");
        playersShouldBeMessagedOnReturn = pl.getConfig().getBoolean("message-players-on-return");
        onTpMessages = pl.getConfig().getStringList("on-tp-messages");
        onReturnMessages = pl.getConfig().getStringList("on-return-messages");
    }

    public TeleportManager() {}

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
            addTeleportedPlayer(player);
            player.teleportAsync(destinationOptional.get().add(0, 1, 0)).thenAccept(success -> {
                if(playersShouldBeMessagedOnTeleport) {
                    chatMessenger.sendChat(player, onTpMessages);
                }
                // TODO handle failed teleportation
            });
            return true;
        }
        return false;
    }

    public int returnAllPlayers() {
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
            removeTeleportedPlayer(player);
            player.teleportAsync(returnLocationOptional.get()).thenAccept(success -> {
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
