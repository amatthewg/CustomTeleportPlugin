package com.pepperish.customteleportplugin.listeners;


import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.enums.Permission;
import com.pepperish.customteleportplugin.util.CustomTpTool;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;


public class HandleToolUse implements Listener {

    private static final ItemStack customTpTool = CustomTpTool.getItemStack();

    private static final String ctpAdmin = Permission.CTP_ADMIN.getString();

    private static LocationManager locationManager = new LocationManager();

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    public HandleToolUse() {}


    // Event used to handle when the player uses our custom tool to right-click a block (add the block)
    @EventHandler
    public void onToolRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().equals(customTpTool)) return;
        // PlayerInteractEvent will fire twice for each hand, so must check for the hand used
        if (!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND))) return;
        if (player.hasPermission(ctpAdmin)) {
            handleRightClickedBlock(player, event.getClickedBlock());
        } else {
            chatMessenger.sendChat(player, "&cYou shouldn't have this tool!");
            player.getInventory().setItemInMainHand(null);
        }
    }

    public void handleRightClickedBlock(Player interactingPlayer, Block clickedBlock) {
        Material blockMaterial = clickedBlock.getType();
        int x = clickedBlock.getX();
        int y = clickedBlock.getY();
        int z = clickedBlock.getZ();
        if(locationManager.addAvailableBlockLocation(clickedBlock.getLocation())) {
            chatMessenger.sendChat(interactingPlayer, String.format("&aAdded block &e%s &f(%d,%d,%d)", blockMaterial, x, y, z));
        }
        else {
            chatMessenger.sendChat(interactingPlayer, String.format(
                    "&cBlock &e%s was already added! &f(%d,%d,%d)", blockMaterial, x, y, z
            ));
        }
    }

    // Event used to handle when player left-clicks a block (remove the block)
    @EventHandler
    public void onToolLeftClick(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(!player.getInventory().getItemInMainHand().equals(customTpTool)) return;
        event.setCancelled(true);
        if(!player.hasPermission(ctpAdmin)) {
            chatMessenger.sendChat(player, "&cYou shouldn't have this tool!");
            player.getInventory().setItemInMainHand(null);
            return;
        }
        handleLeftClickedBlock(player, event.getBlock());
    }

    public void handleLeftClickedBlock(Player interactingPlayer, Block clickedBlock) {
        Material blockMaterial = clickedBlock.getType();
        Location blockLocation = clickedBlock.getLocation();
        int x = clickedBlock.getX();
        int y = clickedBlock.getY();
        int z = clickedBlock.getZ();
        if(locationManager.removeAvailableBlockLocation(blockLocation)) {
            chatMessenger.sendChat(interactingPlayer, String.format(
                    "&aBlock &e%s &awas removed! &f(%d,%d,%d)", blockMaterial, x, y, z
            ));
        }
        else {
            chatMessenger.sendChat(interactingPlayer, String.format(
                    "&cBlock &e%s &cwas never added! &f(%d,%d,%d)", blockMaterial, x, y, z
            ));
        }


    }
}
