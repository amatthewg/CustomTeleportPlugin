package com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive;

import com.pepperish.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.pepperish.customteleportplugin.util.CommandState;

public abstract class MutuallyExclusiveCommand extends ConfirmableSubcommand {

    public abstract CommandState getCommandState();

    public abstract void setCommandState(CommandState state);

    public abstract String getNotReadyMessage();

    public abstract MutuallyExclusiveCommand getMutual();
}
