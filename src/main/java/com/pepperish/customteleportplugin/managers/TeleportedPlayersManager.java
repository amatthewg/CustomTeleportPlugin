package com.pepperish.customteleportplugin.managers;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TeleportedPlayersManager {
    private static final Set<Player> teleportedPlayers = new HashSet<>();

    public TeleportedPlayersManager() {}

    public boolean addTeleportedPlayer(Player player) { return teleportedPlayers.add(player); }

    public boolean removeTeleportedPlayer(Player player) { return teleportedPlayers.remove(player); }

    public boolean playerIsTeleported(Player player ) { return teleportedPlayers.contains(player); }
}
