package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.enums.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// /ctp viewall
public class ViewAllCommand extends SubCommand {

    private static LocationManager locationManager = new LocationManager();

    private static Map<Location, Material> originalBlocksMap = new HashMap<>();

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
    public String getPermissionString() {
        return Permission.CTP_ADMIN.getString();
    }

    @Override
    public void perform(Player sender, String[] args) {
        if(!commandWasRun) { // Change set blocks to an easier-to-see block type
            commandWasRun = true;
            List<Location> blockLocations = locationManager.getAllBlockLocations();
            // I don't understand packets yet :(
            for(Location blockLocation : blockLocations) {
                Block block = blockLocation.getBlock();
                originalBlocksMap.put(blockLocation, block.getType());
                block.setType(Material.RED_WOOL);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "&aSuccessfully changed &e%s &ablocks to Red Wool", blockLocations.size()
            )));
        }
        else { // Revert blocks back to original type
            commandWasRun = false;
            for(Location key : originalBlocksMap.keySet()) {
                Block block = key.getBlock();
                Material originalMaterial = originalBlocksMap.get(key);
                block.setType(originalMaterial);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "&aSuccessfully reverted &e%s &ablocks to original state", originalBlocksMap.size()
            )));
            originalBlocksMap.clear();
        }

    }
}
