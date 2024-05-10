package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.managers.TeleportManager;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.enums.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

// Handles the command /ctp tpall
public class TpAllCommand extends SubCommand {

    private static boolean commandIsActive = false;

    private static LocationManager locationManager = new LocationManager();

    private static String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static TeleportManager tpManager = new TeleportManager();

    private static JavaPlugin plugin;

    private static boolean commandWasRun = false;

    public static boolean getCommandIsActive() {
        return commandIsActive;
    }

    public static boolean getCommandWasRun() { return commandWasRun; }

    public TpAllCommand(JavaPlugin pl) {
        plugin = pl;
    }


    @Override
    public String getName() {
        return "tpall";
    }

    @Override
    public String getDescription() {
        return "Teleport all players to the set locations.";
    }

    @Override
    public String getSyntax() {
        return "/ctp tpall";
    }

    @Override
    public String getPermissionString() {
        return Permission.CTP_ADMIN.getString();
    }

    @Override
    public void perform(Player sender, String[] args) {
        if (!commandWasRun) {
            // TODO does not account for if one admin runs the cmd first, then another admin runs the cmd
            chatMessenger.sendChat(sender, "&cWARNING: You are about to teleport the players to their seats.\n" +
                    "Re-enter &a/ctp tpall &cto confirm.");
            // TODO this command confirm should time out the confirm request eventually
            // TODO does not
            commandWasRun = true;
        } else {
            chatMessenger.messageAdmins(String.format("&e[CustomTeleport] &6(sent to all CTP admins)\n"+
                    "&e%s &ajust executed &e/ctp tpall", sender.getName()));
            tpManager.tpAllPlayers();
            commandWasRun = false;
        }
    }



}
