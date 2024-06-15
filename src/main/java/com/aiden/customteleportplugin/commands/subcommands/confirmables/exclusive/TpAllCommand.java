package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.listeners.HandlePlayerMove;
import com.aiden.customteleportplugin.managers.TeleportManager;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class TpAllCommand extends ExclusiveCommand {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final TeleportManager tpManager = new TeleportManager();

    private static final String notEnoughBlocksMsg = "&cWARNING: Could not teleport all players because there\n" +
            "weren't enough set block locations!";

    private static String bungeeCordTpChannel = null;

    private static boolean shouldWarnAdminsOnTeleportResult;

    private static JavaPlugin plugin;

    private static boolean canBeExecuted = true;

    private boolean commandIsConfirmed = false;

    public TpAllCommand(JavaPlugin pl) {
        plugin = pl;
        shouldWarnAdminsOnTeleportResult = plugin.getConfig().getBoolean("warn-admins-on-teleport");
        bungeeCordTpChannel = plugin.getConfig().getString("bungeecord-channel");
    }

    public TpAllCommand() {}

    @Override
    public String getName() { return "tpall"; }

    @Override
    public String getDescription() { return "Teleport all players to the set locations."; }

    @Override
    public String getSyntax() { return "/ctp tpall"; }

    @Override
    public void perform(Player sender, String[] args) {
        Optional<Integer> teleportedPlayerCount = tpManager.tpAllPlayers();
        if(teleportedPlayerCount.isPresent()) {
            if(shouldWarnAdminsOnTeleportResult) {
                chatMessenger.messageAdmins(String.format(
                        "&aSuccessfully teleported &e%d &aplayers", teleportedPlayerCount.get()));
                return;
            }
            chatMessenger.sendChat(sender, String.format(
                    "&aSuccessfully teleported &e%d &aplayers", teleportedPlayerCount.get()
            ));
        }
        else {
            if(shouldWarnAdminsOnTeleportResult) {
                chatMessenger.messageAdmins(notEnoughBlocksMsg);
                return;
            }
            chatMessenger.sendChat(sender, notEnoughBlocksMsg);
        }
        canBeExecuted = false;
        ReturnCommand.setCanBeExecuted(true);
        //ReloadCommand.setCanBeExecuted(false);
        // Register movement listener after teleport is complete to avoid issues with cancelling teleport movement
        plugin.getServer().getPluginManager().registerEvents(new HandlePlayerMove(), plugin);

        // TPall command is fully active -- send bungeecord message to connect all players from various servers
        if(bungeeCordTpChannel == null) {
            chatMessenger.sendChat(sender, "&cWarning: cannot get players from other servers because the bungeecord channel is not specified in the config.yml");
            return;
        }
        sendBungeeCordMessage(sender);
    }

    @Override
    public String getConfirmationMessage() { return "WARNING: You are about to teleport the players to their blocks!"; }

    @Override
    public boolean isConfirmed() { return this.commandIsConfirmed; }

    @Override
    public void setIsConfirmed(boolean state) { this.commandIsConfirmed = state; }

    @Override
    public boolean canBeExecuted() { return canBeExecuted; }

    @Override
    public String getCannotBeExecutedMessage() { return "you must first execute command &e/ctp return"; }

    public static void setCanBeExecuted(boolean state) { canBeExecuted = state; }

    private void sendBungeeCordMessage(Player player) {
        com.google.common.io.ByteArrayDataOutput out = com.google.common.io.ByteStreams.newDataOutput();
        out.writeBoolean(true);
        player.sendPluginMessage(plugin, bungeeCordTpChannel, out.toByteArray());
    }

}
