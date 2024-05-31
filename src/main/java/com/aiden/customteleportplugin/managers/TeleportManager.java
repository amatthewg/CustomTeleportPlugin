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

    private static final aManager BLOCK_LOCATION_MANAGER = new aManager();

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

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
        Optional<Location> destinationOptional = BLOCK_LOCATION_MANAGER.getNextAvailableLocation(player);
        if(destinationOptional.isPresent()) {
            originalPlayerLocations.put(player.getUniqueId(), player.getLocation());
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
        for(UUID uuid : originalPlayerLocations.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            p.teleportAsync(originalPlayerLocations.remove(uuid));
            if(!p.hasPermission(shouldBeTeleportedPermission)) continue;
            if(tryReturnPlayer(p)) returnedCount++;
        }
        BLOCK_LOCATION_MANAGER.refresh();
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
            return true;
        }
        // Null return location signifies that this player was never teleported
        return false;
    }
}
