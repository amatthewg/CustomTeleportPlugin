package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// /ctp viewall
public class ViewAllCommand extends Subcommand {

    private static final LocationManager locationManager = new LocationManager();

    private static final Map<Location, Material> originalBlocksMap = new HashMap<>();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static boolean commandWasRun = false;

    @Override
    public String getName() {
        return "viewall";
    }

    @Override
    public String getDescription() {
        return "View all of the set blocks";

    }

    @Override
    public String getSyntax() {
        return "/ctp viewall";
    }



    @Override
    public void perform(Player sender, String[] args) {
        if(!commandWasRun) { // Change set blocks to an easier-to-see block type
            commandWasRun = true;
            Set<Location> blockLocations = locationManager.getAllBlockLocations();
            for(Location blockLocation : blockLocations) {
                Block block = blockLocation.getBlock();
                originalBlocksMap.put(blockLocation, block.getType());
                block.setType(Material.RED_WOOL);
            }
            chatMessenger.sendChat(sender, String.format("&aSuccessfully changed &e%d &ablocks to Red Wool",
                    blockLocations.size()));
        }
        else { // Revert blocks back to original type
            commandWasRun = false;
            for(Location key : originalBlocksMap.keySet()) {
                Block block = key.getBlock();
                Material originalMaterial = originalBlocksMap.get(key);
                block.setType(originalMaterial);
            }
            chatMessenger.sendChat(sender, String.format("&aSuccessfully reverted &e%d &ablocks to original state",
                    originalBlocksMap.size()));
            originalBlocksMap.clear();
        }

    }
}
