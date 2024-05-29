package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.listeners.HandlePlayerMove;
import com.aiden.customteleportplugin.managers.TeleportManager;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import com.aiden.customteleportplugin.util.CommandState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class ReturnCommand extends ExclusiveCommand {

    private static final TeleportManager tpFreezeManager = new TeleportManager();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static CommandState commandState = CommandState.CURRENTLY_EXECUTED;

    private static JavaPlugin plugin;

    private boolean commandIsConfirmed = false;

    public ReturnCommand(JavaPlugin pl) { plugin = pl; }

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
        int returnedCount = tpFreezeManager.returnAllPlayers();
        chatMessenger.sendChat(sender, String.format("&aSuccessfully returned &e%d &aplayers", returnedCount));
        setCommandState(CommandState.CURRENTLY_EXECUTED);
        new TpAllCommand().setCommandState(CommandState.NOT_CURRENTLY_EXECUTED);
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
    public CommandState getCommandState() { return commandState; }

    @Override
    public void setCommandState(CommandState state) { commandState = state; }

    @Override
    public String getNotReadyMessage() { return "the tpall command has not been executed!"; }

}
