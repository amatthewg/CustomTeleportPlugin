package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.permissions.Permission;
import com.pepperish.customteleportplugin.tool.CustomTpTool;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ToolCommand extends SubCommand {
    @Override
    public String getName() {
        return "tool";
    }

    @Override
    public String getDescription() {
        return "Retrieve the block selection tool";
    }

    @Override
    public String getSyntax() {
        return "/ctp tool";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(player.hasPermission(Permission.COMMAND_TOOL.getPermission())) {
            Map<Integer, ItemStack> map = player.getInventory().addItem(CustomTpTool.getItemStack());
            if(map.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aYou have been given one Tool"));
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cCould not add the tool to your inventory!"));
            }

        }
    }
}
