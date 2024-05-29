package com.aiden.customteleportplugin.commands.subcommands.confirmables;

import com.aiden.customteleportplugin.commands.subcommands.Subcommand;

public abstract class ConfirmableSubcommand extends Subcommand {

    public abstract String getConfirmationMessage();

    public abstract boolean isConfirmed();

    public abstract void setIsConfirmed(boolean state);

}
