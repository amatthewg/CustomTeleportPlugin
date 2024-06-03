package com.aiden.customteleportplugin.listeners;

import com.aiden.customteleportplugin.managers.BlockLocationManager;
import com.aiden.customteleportplugin.managers.WorldManager;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import com.aiden.customteleportplugin.util.Permission;
import com.aiden.customteleportplugin.util.CustomTpTool;
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

    private static final BlockLocationManager blockLocationManager = new BlockLocationManager();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final WorldManager worldManager = new WorldManager();

    private static boolean viewingCommandEnabled = false;

    public static void setViewingCommandEnabled(boolean state) { viewingCommandEnabled = state; }


    public HandleToolUse() {}

    @EventHandler
    public void onToolRightClick(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().equals(customTpTool)) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (player.hasPermission(ctpAdmin)) {
            if(badLocation(player, event.getClickedBlock().getLocation())) return;
            handleRightClickedBlock(player, event.getClickedBlock());
        } else {
            chatMessenger.sendChat(player, "&cYou shouldn't have this tool!");
            player.getInventory().setItemInMainHand(null);
        }
    }

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
        if(badLocation(player, event.getBlock().getLocation())) return;
        handleLeftClickedBlock(player, event.getBlock());
    }

    public void handleRightClickedBlock(Player interactingPlayer, Block clickedBlock) {
        Material blockMaterial = clickedBlock.getType();
        int x = clickedBlock.getX();
        int y = clickedBlock.getY();
        int z = clickedBlock.getZ();
        if(blockLocationManager.addAvailableBlockLocation(clickedBlock.getLocation())) {
            chatMessenger.sendChat(interactingPlayer, String.format("&aAdded block &e%s &f(%d,%d,%d)", blockMaterial, x, y, z));
        }
        else {
            chatMessenger.sendChat(interactingPlayer, String.format(
                    "&cBlock &e%s was already added! &f(%d,%d,%d)", blockMaterial, x, y, z
            ));
        }
    }

    public void handleLeftClickedBlock(Player interactingPlayer, Block clickedBlock) {
        Material blockMaterial = clickedBlock.getType();
        Location blockLocation = clickedBlock.getLocation();
        int x = clickedBlock.getX();
        int y = clickedBlock.getY();
        int z = clickedBlock.getZ();
        if(blockLocationManager.removeAvailableBlockLocation(blockLocation)) {
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

    private boolean badLocation(Player interactingPlayer, Location location) {
        if(worldManager.isCorrectWorld(location.getWorld())) return false;
        String worldName = worldManager.getWorldName();
        chatMessenger.sendChat(interactingPlayer, String.format(
                "&cBlock locations can only be set/removed in world '%s'", worldName));
        return true;
    }

}
