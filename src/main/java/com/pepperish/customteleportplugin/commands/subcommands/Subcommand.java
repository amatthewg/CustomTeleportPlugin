package com.pepperish.customteleportplugin.commands.subcommands;

import org.bukkit.entity.Player;

public abstract class Subcommand {



    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();

    public abstract void perform(Player sender, String[] args);
}
