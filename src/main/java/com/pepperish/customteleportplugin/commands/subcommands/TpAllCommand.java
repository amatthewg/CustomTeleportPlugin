package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.DAL.BlockStorage;
import com.pepperish.customteleportplugin.DAL.FileManager;
import com.pepperish.customteleportplugin.DAL.ReturnLocationStorage;
import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.permissions.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// Handles the command /ctp tpall
public class TpAllCommand extends SubCommand {

    private static boolean commandIsActive = false;

    private boolean commandWasRun = false;
    private static String commandTpAllPermission = Permission.COMMAND_TPALL.getPermission();
    private static String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getPermission();
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
    public void perform(Player player, String[] args) {
        if(player.hasPermission(commandTpAllPermission)) {
            FileManager fileManager = new FileManager();
            if(!(fileManager.isCurrentlyLoading() || fileManager.isFileError())) {
                if(!commandWasRun) {
                    // Used to make the sending player confirm the cmd
                    // Note: does not account for if one admin runs the cmd first, then another admin runs the cmd
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cWARNING: You are about to teleport the players to their seats.\n" +
                                    "Re-enter &a/ctp tpall &cto confirm."));
                    // TODO this command confirm should account for different admins running the command
                    // TODO this command confirm should time out the confirm request eventually
                    commandWasRun = true;
                }
                else {
                    // Command is confirmed
                    BlockStorage blockStorage = new BlockStorage();
                    Map<Location, UUID> map = blockStorage.getBlockLocationsAsMap();

                    
                }
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "The plugin experienced an error loading save data. Please restart " +
                                "the server or try again later."));
            }
        } else{
            player.sendMessage(Permission.NO_PERMISSION.getPermission());
        }


    }
    public static boolean commandIsActive() { return commandIsActive; }
    public static void setCommandIsActive(boolean status) { commandIsActive = status; }

    private void runTeleport(Player sender) {
        BlockStorage blockStorage = new BlockStorage();
        ReturnLocationStorage returnLocationStorage = new ReturnLocationStorage();

        List<Player> playersToTeleport = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(shouldBeTeleportedPermission))
                .collect(Collectors.toList());

        List<Location> locations = new ArrayList<>(blockStorage.getBlockLocations());

        if(playersToTeleport.size() > locations.size()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format("&cWARNING: Too few block locations &f(%d) &cto teleport %d players! " +
                            "Some players won't get teleported!", locations.size(), playersToTeleport.size())));
        }


    }
}
