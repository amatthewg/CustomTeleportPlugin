package com.pepperish.customteleportplugin.commands.subcommands.confirmables;

import com.pepperish.customteleportplugin.messengers.PlayerTitleMessenger;
import com.pepperish.customteleportplugin.util.Permission;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicInteger;

public class WarnCommand extends ConfirmableSubcommand {

    private static boolean commandWasRun = false;

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static PlayerTitleMessenger titleMessenger = new PlayerTitleMessenger();

    private static String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static JavaPlugin plugin;

    public WarnCommand(JavaPlugin pl) { plugin = pl; }
    @Override
    public String getName() { return "warn"; }

    @Override
    public String getDescription() { return "Warn all teleportable players of imminent teleportation"; }

    @Override
    public String getSyntax() { return "/ctp warn"; }

    @Override
    public String getConfirmationMessage() {
        return "WARNING: You are about to send a title warning message to all players!";
    }

    @Override
    public void perform(Player sender, String[] args) {
        String messageTitle = plugin.getConfig().getString("warning-title");
        String messageSubtitle = plugin.getConfig().getString("warning-subtitle");
        AtomicInteger warnedPlayerCount = new AtomicInteger(0);
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(shouldBeTeleportedPermission))
                .forEach(p -> {
                    titleMessenger.sendTitleMsg(p, messageTitle, messageSubtitle);
                    warnedPlayerCount.incrementAndGet();
                });
        chatMessenger.sendChat(sender, String.format("&aSuccessfully warned &e%s &aplayers of imminent teleportation",
                warnedPlayerCount.get()));
    }
}
