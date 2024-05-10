package com.pepperish.customteleportplugin.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();

    public abstract String getPermissionString();
    public abstract void perform(Player sender, String[] args);
}
