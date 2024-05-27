package com.aiden.customteleportplugin.commands.subcommands.confirmables;

import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import com.aiden.customteleportplugin.messengers.PlayerTitleMessenger;
import com.aiden.customteleportplugin.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WarnCommand extends ConfirmableSubcommand {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final PlayerTitleMessenger titleMessenger = new PlayerTitleMessenger();

    private static final String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static JavaPlugin plugin;

    public WarnCommand(JavaPlugin pl) { plugin = pl; }

    @Override
    public String getName() { return "warn"; }

    @Override
    public String getDescription() { return "Warn all teleportable players of imminent teleportation"; }

    @Override
    public String getSyntax() { return "/ctp warn"; }

    @Override
    public void perform(Player sender, String[] args) {
        String messageTitle = plugin.getConfig().getString("warning-title");
        String messageSubtitle = plugin.getConfig().getString("warning-subtitle");
        int warnedCount = 0;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission(shouldBeTeleportedPermission)) {
                titleMessenger.sendTitleMsg(p, messageTitle, messageSubtitle);
                warnedCount++;
            }
        }
        chatMessenger.sendChat(sender, String.format("&aSuccessfully warned &e%d &aplayers of imminent teleportation",
                warnedCount));
    }

    @Override
    public String getConfirmationMessage() {
        return "WARNING: You are about to send a title warning message to all players!";
    }

}
