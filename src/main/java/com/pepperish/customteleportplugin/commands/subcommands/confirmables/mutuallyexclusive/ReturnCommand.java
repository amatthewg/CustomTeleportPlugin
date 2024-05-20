package com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive;

import com.pepperish.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.managers.TeleportManager;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.util.CommandState;
import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ReturnCommand extends MutuallyExclusiveCommand {

    private CommandState commandState;

    private MutuallyExclusiveCommand mutualCommand = null;

    public ReturnCommand() {

    }

    public ReturnCommand() {}

    @Override
    public String getName() {
        return "return";
    }

    @Override
    public String getDescription() {
        return "Return all players to their original location";
    }

    @Override
    public String getSyntax() {
        return "/ctp return";
    }

    @Override
    public void perform(Player sender, String[] args) {

    }

    @Override
    public String getConfirmationMessage() {
        return "WARNING: You are about to return the players to their original locations!";
    }


    @Override
    public CommandState getCommandState() {
        return null;
    }

    @Override
    public void setCommandState(CommandState state) {

    }

    @Override
    public String getNotReadyMessage() {
        return "the players have not been teleported yet!";
    }

    @Override
    public MutuallyExclusiveCommand getMutual() {
        return new TpAllCommand();
    }
}
