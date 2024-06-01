package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.listeners.HandlePlayerMove;
import com.aiden.customteleportplugin.managers.TeleportManager;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class ReturnCommand extends ExclusiveCommand {

    private static final TeleportManager tpManager = new TeleportManager();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static boolean canBeExecuted = false;

    private boolean commandIsConfirmed = false;

    public ReturnCommand() {}

    @Override
    public String getName() { return "return"; }

    @Override
    public String getDescription() { return "Return all players to their original location"; }

    @Override
    public String getSyntax() { return "/ctp return"; }

    @Override
    public void perform(Player sender, String[] args) {
        // Unregister movement listener before returning, to avoid issues with cancelling teleport movement
        HandlerList.unregisterAll(new HandlePlayerMove());
        int returnedCount = tpManager.returnAllPlayers();
        chatMessenger.sendChat(sender, String.format("&aSuccessfully returned &e%d &aplayers", returnedCount));
        canBeExecuted = true;
        TpAllCommand.setCanBeExecuted(true);
        //ReloadCommand.setCanBeExecuted(true);
    }

    @Override
    public String getConfirmationMessage() {
        return "WARNING: You are about to return the players to their original locations!";
    }

    @Override
    public boolean isConfirmed() { return this.commandIsConfirmed; }

    @Override
    public void setIsConfirmed(boolean state) { this.commandIsConfirmed = state; }

    @Override
    public boolean canBeExecuted() { return canBeExecuted; }

    @Override
    public String getCannotBeExecutedMessage() { return "the tpall command has not been executed!"; }

    public static void setCanBeExecuted(boolean state) { canBeExecuted = state; }

}
