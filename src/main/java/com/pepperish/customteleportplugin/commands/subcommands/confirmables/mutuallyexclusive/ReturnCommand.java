package com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive;

import com.pepperish.customteleportplugin.managers.TeleportFreezeManager;
import com.pepperish.customteleportplugin.util.CommandState;
import org.bukkit.entity.Player;

import java.awt.event.TextEvent;

public class ReturnCommand extends MutuallyExclusiveCommand {

    private static final TeleportFreezeManager tpFreezeManager = new TeleportFreezeManager();

    private CommandState commandState;

    private MutuallyExclusiveCommand mutualCommand = null;

    public ReturnCommand() {}

    @Override
    public String getName() { return "return"; }

    @Override
    public String getDescription() { return "Return all players to their original location"; }

    @Override
    public String getSyntax() { return "/ctp return"; }

    @Override
    public void perform(Player sender, String[] args) { tpFreezeManager.returnAllPlayers(sender); }

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

    @Override
    public MutuallyExclusiveCommand getMutual() { return new TpAllCommand(); }
}
