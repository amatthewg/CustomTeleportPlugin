package com.pepperish.customteleportplugin.messengers;

import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerChatMessenger {

    private static String ctpAdmin = Permission.CTP_ADMIN.getString();

    public PlayerChatMessenger() {

    }

    public void messageAdmins(String message) {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(ctpAdmin))
                .forEach(p -> sendChat(p, message));
    }

    public void messageAdmins(List<String> messages) {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(ctpAdmin))
                .forEach(p -> sendChat(p, messages));
    }

    public void sendChat(Player target, String message) {
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendChat(Player target, List<String> messages) {
        messages.forEach(msg -> target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)));
    }
}
