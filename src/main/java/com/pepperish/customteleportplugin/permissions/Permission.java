package com.pepperish.customteleportplugin.permissions;

import org.bukkit.ChatColor;

public enum Permission {

    NO_PERMISSION(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!")),
    COMMAND_BASE("ctp"),
    COMMAND_TOOL("ctp.tool"),
    COMMAND_TPALL("ctp.tpall"),

    SHOULD_BE_TELEPORTED("ctp.shouldbeteleported");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }


}
