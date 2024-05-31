package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;

public abstract class ExclusiveCommand extends ConfirmableSubcommand {

    public abstract boolean isCurrentlyExecuted();

    public abstract String getNotReadyMessage();

}
