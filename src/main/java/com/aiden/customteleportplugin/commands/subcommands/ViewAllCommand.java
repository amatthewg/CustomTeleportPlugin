package com.aiden.customteleportplugin.commands.subcommands;

import com.aiden.customteleportplugin.managers.BlockLocationManager;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewAllCommand extends Subcommand {

    private static final BlockLocationManager blockLocationManager = new BlockLocationManager();

    private static final Map<Location, Material> originalBlocksMap = new HashMap<>();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static boolean shouldRevertBlocks = false;

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
        if (!shouldRevertBlocks) {
            shouldRevertBlocks = true;
            Set<Location> blockLocations = blockLocationManager.getAllBlockLocations();
            blockLocations.forEach(loc -> {


                Block block = loc.getBlock();
                originalBlocksMap.put(loc, block.getType());
                block.setType(Material.RED_WOOL);


            });
            chatMessenger.sendChat(sender, String.format("&aSuccessfully changed &e%d &ablocks to Red Wool",
                    blockLocations.size()));
        } else {
            shouldRevertBlocks = false;
            originalBlocksMap.forEach((key, val) -> key.getBlock().setType(val));
            chatMessenger.sendChat(sender, String.format("&aSuccessfully reverted &e%d &ablocks to original state",
                    originalBlocksMap.size()));
            originalBlocksMap.clear();
        }
    }

}
