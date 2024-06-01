package com.aiden.customteleportplugin.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomTpTool {

    public static ItemStack getItemStack() {
        ItemStack customTool = new ItemStack(Material.STICK, 1);
        ItemMeta itemMeta = customTool.getItemMeta();
        itemMeta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("&eCustom TP Tool"));
        List<Component> loreStrings = new ArrayList<>();
        loreStrings.add(LegacyComponentSerializer.legacyAmpersand().deserialize("&a&lCustomTeleport"));
        loreStrings.add(Component.text("Right-click on a block with this tool to "));
        loreStrings.add(Component.text("set the blocks for players to teleport to."));
        loreStrings.add(Component.text("Left-click on a block to remove it from the"));
        loreStrings.add(Component.text("teleport list."));
        itemMeta.lore(loreStrings);
        customTool.setItemMeta(itemMeta);
        return customTool;
    }
}
