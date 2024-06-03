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

    private static final BlockLocationManager blockLocationManager = new BlockLocationManager();

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    // originalPlayerLocations also doubles as a means to tell if a Player is/was teleported
    private static final Map<UUID, Location> originalPlayerLocations = new HashMap<>();

    private static Boolean playersShouldBeMessagedOnTeleport = null;

    private static Boolean playersShouldBeMessagedOnReturn = null;

    private static List<String> onTpMessages = null;

    private static List<String> onReturnMessages = null;

    public static boolean playerIsTeleported(Player player) {
        return originalPlayerLocations.get(player.getUniqueId()) != null;
    }

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
            boolean teleportSuccess = tryTpPlayer(p);
            if(!teleportSuccess) {
                allTeleported = false;
                break;
            }
            teleportedCount++;
        }
        if(allTeleported) return Optional.of(teleportedCount);
        else return Optional.empty();
    }

    public boolean tryTpPlayer(Player player) {
        Optional<Location> destinationOptional = blockLocationManager.getNextAvailableLocation(player);
        if(destinationOptional.isPresent()) {
            Location originalLocation = player.getLocation();
            Location rawDestination = destinationOptional.get();
            Location resultDestination = new Location(rawDestination.getWorld(), rawDestination.getX()+0.5, rawDestination.getY()+1, rawDestination.getZ()+0.5);
            player.teleportAsync(resultDestination).thenAccept(success -> {
                originalPlayerLocations.put(player.getUniqueId(), originalLocation);
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
        for(UUID uuid : originalPlayerLocations.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            p.teleportAsync(originalPlayerLocations.remove(uuid));
            if(!p.hasPermission(shouldBeTeleportedPermission)) continue;
            if(tryReturnPlayer(p)) returnedCount++;
        }
        return returnedCount;
    }

    public boolean tryReturnPlayer(Player player) {
        Location returnLocation = originalPlayerLocations.remove(player.getUniqueId());
        if(returnLocation != null) {
            player.teleportAsync(returnLocation).thenAccept(success -> {
                if(playersShouldBeMessagedOnReturn) {
                    chatMessenger.sendChat(player, onReturnMessages);
                }
                // TODO handle failed teleportation
            });
            blockLocationManager.handlePlayerReturn(player);
            return true;
        }
        // Null return location signifies that this player was never teleported
        return false;
    }
}
