package com.pepperish.customteleportplugin.messengers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerTitleMessenger {

    public PlayerTitleMessenger() {}

    public void sendTitleMsg(Player target, String title, String subtitle) {
        String resultTitle = ChatColor.translateAlternateColorCodes('&', title);
        String resultSubtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        target.sendTitle(resultTitle, resultSubtitle);
    }
}
