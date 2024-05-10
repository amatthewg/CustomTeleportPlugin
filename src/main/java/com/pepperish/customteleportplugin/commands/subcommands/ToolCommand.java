package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.enums.Permission;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.util.CustomTpTool;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ToolCommand extends SubCommand {

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static ItemStack customTpTool = CustomTpTool.getItemStack();
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
    public String getPermissionString() {
        return Permission.CTP_ADMIN.getString();
    }

    @Override
    public void perform(Player sender, String[] args) {
        Map<Integer, ItemStack> map = sender.getInventory().addItem(customTpTool);
        if (map.isEmpty()) {
            chatMessenger.sendChat(sender, "&aYou have been given one Tool");
        } else {
            chatMessenger.sendChat(sender, "&cCould not add the tool to your inventory!");
        }


    }
}
