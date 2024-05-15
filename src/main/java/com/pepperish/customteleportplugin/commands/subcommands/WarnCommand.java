package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.util.Permission;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicInteger;

public class WarnCommand extends SubCommand {

    private static boolean commandWasRun = false;

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static JavaPlugin plugin;

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "Warn all teleportable players of imminent teleportation";
    }

    @Override
    public String getSyntax() {
        return "/ctp warn";
    }

    @Override
    public String getPermissionString() {
        return Permission.CTP_ADMIN.getString();
    }

    @Override
    public void perform(Player sender, String[] args) {
        if(!commandWasRun) {
            chatMessenger.sendChat(sender, "&cWARNING: You are about to send a warning message to" +
                    " all players. Reenter &a/ctp warn &cto confirm.");
            commandWasRun = true;
            return;
        }
        commandWasRun = false;
        String messageTitle = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("warning-title"));
        String messageSubtitle = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("warning-subtitle"));

        if(messageTitle.isEmpty() && messageSubtitle.isEmpty()) {
            chatMessenger.sendChat(sender, "&cThis command is disabled in the config.yml!");
            return;
        }

        AtomicInteger warnedPlayerCount = new AtomicInteger(0);
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(shouldBeTeleportedPermission))
                .forEach(p -> {
                    p.sendTitle(messageTitle, messageSubtitle);
                    warnedPlayerCount.incrementAndGet();
                });
        chatMessenger.sendChat(sender, String.format("&aSuccessfully warned &e%s &aplayers of imminent teleportation",
                warnedPlayerCount.get()));
        chatMessenger.messageAdmins(String.format("&e[CustomTeleport] &6(sent to all CTP admins)\n"+
                "&e%s &ajust executed &e/ctp warn", sender.getName()));
    }
}
