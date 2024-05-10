package com.pepperish.customteleportplugin.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomTpTool {


    // Method to create/retrieve the custom tool
    public static ItemStack getItemStack() {
        // The tool will be a stick
        ItemStack customTool = new ItemStack(Material.STICK, 1);
        // Create/set item meta
        ItemMeta itemMeta = customTool.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eCustom TP Tool"));
        List<String> loreStrings = new ArrayList<>();
        loreStrings.add(ChatColor.translateAlternateColorCodes('&', "&a&lCustomTeleport"));
        loreStrings.add("Right-click on a block with this tool to ");
        loreStrings.add("set the blocks for players to teleport to.");
        loreStrings.add("Left-click on a block to remove it from the");
        loreStrings.add("teleport list.");
        itemMeta.setLore(loreStrings);
        customTool.setItemMeta(itemMeta);
        return customTool;
    }

}
