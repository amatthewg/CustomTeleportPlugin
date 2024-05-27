package com.aiden.customteleportplugin.commands.subcommands;

import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import com.aiden.customteleportplugin.util.CustomTpTool;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ToolCommand extends Subcommand {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final ItemStack customTpTool = CustomTpTool.getItemStack();

    @Override
    public String getName() { return "tool"; }

    @Override
    public String getDescription() { return "Retrieve the block selection tool"; }

    @Override
    public String getSyntax() { return "/ctp tool"; }

    @Override
    public void perform(Player sender, String[] args) {
        Map<Integer, ItemStack> map = sender.getInventory().addItem(customTpTool);
        if (map.isEmpty()) chatMessenger.sendChat(sender, "&aYou have been given one Tool");
        else chatMessenger.sendChat(sender, "&cCould not add the tool to your inventory!");
    }

}
