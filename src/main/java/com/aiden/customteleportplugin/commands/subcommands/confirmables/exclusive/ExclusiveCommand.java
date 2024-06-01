package com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive;

import com.aiden.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;

public abstract class ExclusiveCommand extends ConfirmableSubcommand {

    public abstract boolean canBeExecuted();

    public abstract String getCannotBeExecutedMessage();

}
