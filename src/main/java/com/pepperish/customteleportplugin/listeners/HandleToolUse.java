package com.pepperish.customteleportplugin.listeners;

import com.pepperish.customteleportplugin.CustomTeleportPlugin;
import com.pepperish.customteleportplugin.DAL.BlockStorage;
import com.pepperish.customteleportplugin.DAL.DAL;
import com.pepperish.customteleportplugin.permissions.Permission;
import com.pepperish.customteleportplugin.tool.CustomTpTool;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class HandleToolUse implements Listener {

    private static final ItemStack customTpTool = CustomTpTool.getItemStack();
    private static final String useToolPermission = Permission.COMMAND_TOOL.getPermission();
    private CustomTeleportPlugin plugin;
    public HandleToolUse(CustomTeleportPlugin plugin) {
        this.plugin = plugin;
    }


    // Event used to handle when the player uses our custom tool to right-click a block (add the block)
    @EventHandler
    public void onToolRightClick(PlayerInteractEvent event) {
        // First, check if player right-clicked a block
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            // Next, check if player used our tool with their main hand
            Player player = event.getPlayer();
            if(player.getInventory().getItemInMainHand().equals(customTpTool)) {
                // Next, check if player has permission to use the tool
                if(player.hasPermission(useToolPermission)) {
                    // Finally, handle the clicked block
                    handleRightClickedBlock(player, event.getClickedBlock());
                }
                else {
                    player.sendMessage("How did you get this tool?");
                    player.getInventory().setItemInMainHand(null);
                }
            }
        }
    }
    public void handleRightClickedBlock(Player interactingPlayer, Block clickedBlock) {
        BlockStorage blockStorage = new BlockStorage();
        int x = clickedBlock.getX();
        int y = clickedBlock.getY();
        int z = clickedBlock.getZ();
        // Add location to DAL
        if(blockStorage.addToBlockLocations(clickedBlock.getLocation())) {
            // Sucessfully added
            interactingPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format("&aBlock has been added! &f(%d,%d,%d)", x, y, z)));
        }
        else {
            // Block was already added
            interactingPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format("&cBlock was already added! &f(%d,%d,%d)", x, y, z)));
        }

    }

    // Event used to handle when player left-clicks a block (remove the block)
    @EventHandler
    public void onToolLeftClick(BlockBreakEvent event) {
        // First, check if player used our tool with the main hand
        Player player = event.getPlayer();
        if(player.getInventory().getItemInMainHand().equals(customTpTool)) {
            event.setCancelled(true);
            // Next check if player has permission to use the tool
            if(player.hasPermission(useToolPermission)) {
                // Finally, handle the left-clicked block
                handleLeftClickedBlock(player, event.getBlock());
            }
            else {
                player.sendMessage("How did you get this tool?");
                player.getInventory().setItemInMainHand(null);
            }

        }
    }

    public void handleLeftClickedBlock(Player interactingPlayer, Block clickedBlock) {
        BlockStorage blockStorage = new BlockStorage();
        int x = clickedBlock.getX();
        int y = clickedBlock.getY();
        int z = clickedBlock.getZ();
        if(blockStorage.removeFromTeleportLocations(clickedBlock.getLocation())) {
            // Block successfully removed
            interactingPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format("&aBlock has been removed! &f(%d,%d,%d)", x, y, z)));
        }
        else {
            // Block was never added
            interactingPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format("&cBlock was never added! &f(%d,%d,%d)", x, y, z)));
        }
    }
}
