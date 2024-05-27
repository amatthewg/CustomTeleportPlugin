package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.aiden.customteleportplugin.util.CommandState;

public abstract class ExclusiveCommand extends ConfirmableSubcommand {

    public abstract CommandState getCommandState();

    public abstract void setCommandState(CommandState state);

    public abstract String getNotReadyMessage();

}
