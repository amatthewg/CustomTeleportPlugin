package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.managers.TeleportFreezeManager;
import com.aiden.customteleportplugin.util.CommandState;
import org.bukkit.entity.Player;

public class ReturnCommand extends ExclusiveCommand {

    private static final TeleportFreezeManager tpFreezeManager = new TeleportFreezeManager();

    private static CommandState commandState = CommandState.CURRENTLY_EXECUTED;

    public ReturnCommand() {}

    @Override
    public String getName() { return "return"; }

    @Override
    public String getDescription() { return "Return all players to their original location"; }

    @Override
    public String getSyntax() { return "/ctp return"; }

    @Override
    public void perform(Player sender, String[] args) {
        tpFreezeManager.returnAllPlayers(sender);
        setCommandState(CommandState.CURRENTLY_EXECUTED);
        new TpAllCommand().setCommandState(CommandState.NOT_CURRENTLY_EXECUTED);
    }

    @Override
    public String getConfirmationMessage() {
        return "WARNING: You are about to return the players to their original locations!";
    }


    @Override
    public CommandState getCommandState() { return commandState; }

    @Override
    public void setCommandState(CommandState state) { commandState = state; }

    @Override
    public String getNotReadyMessage() { return "the tpall command has not been executed!"; }

}
