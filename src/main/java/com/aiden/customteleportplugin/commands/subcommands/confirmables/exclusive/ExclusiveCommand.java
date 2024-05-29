package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.aiden.customteleportplugin.util.CommandState;

public abstract class ExclusiveCommand extends ConfirmableSubcommand {

    public abstract boolean isCurrentlyExecuted();

    public abstract void setIsCurrentlyExecuted(boolean state);

    public abstract Class<ExclusiveCommand> getMutual();

    public abstract String getNotReadyMessage();

}
