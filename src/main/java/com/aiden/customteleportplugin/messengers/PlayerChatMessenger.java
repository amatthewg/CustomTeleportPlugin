package com.aiden.customteleportplugin.messengers;

import com.aiden.customteleportplugin.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerChatMessenger {

    private static final String ctpAdmin = Permission.CTP_ADMIN.getString();

    public PlayerChatMessenger() {}

    public void messageAdmins(String message) {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(ctpAdmin))
                .forEach(p -> {
                    sendChat(p, "&b(Sent to all CTP admins)");
                    sendChat(p, message);
                });
    }

    public void sendChat(Player target, String message) {
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendChat(Player target, List<String> messages) {
        messages.forEach(msg -> sendChat(target, msg));
    }
}
