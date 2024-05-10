package com.pepperish.customteleportplugin.enums;

public enum Permission {
    CTP_ADMIN("ctp.admin"),
    SHOULD_BE_TELEPORTED("ctp.shouldbeteleported");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getString() {
        return this.permission;
    }


}
