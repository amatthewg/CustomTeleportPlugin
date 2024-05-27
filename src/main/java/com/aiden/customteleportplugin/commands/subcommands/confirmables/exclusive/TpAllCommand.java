package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.managers.TeleportFreezeManager;
import com.aiden.customteleportplugin.util.CommandState;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class TpAllCommand extends ExclusiveCommand {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final TeleportFreezeManager tpFreezeManager = new TeleportFreezeManager();

    private static final String notEnoughBlocksMsg = "&cWARNING: Could not teleport all players because there\n" +
            "weren't enough set block locations!";

    private static CommandState commandState = CommandState.NOT_CURRENTLY_EXECUTED;

    private static boolean shouldWarnAdminsOnTeleportResult;

    public TpAllCommand(JavaPlugin pl) {
        shouldWarnAdminsOnTeleportResult = pl.getConfig().getBoolean("warn-admins-on-teleport");
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
        Optional<Integer> teleportedPlayerCount = tpFreezeManager.tpAllPlayers();
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
        setCommandState(CommandState.CURRENTLY_EXECUTED);
        new ReturnCommand().setCommandState(CommandState.NOT_CURRENTLY_EXECUTED);
    }

    @Override
    public String getConfirmationMessage() { return "WARNING: You are about to teleport the players to their blocks!"; }

    @Override
    public CommandState getCommandState() { return commandState; }

    @Override
    public void setCommandState(CommandState state) { commandState = state; }

    @Override
    public String getNotReadyMessage() { return "the tpall command is currently executed!"; }

}
