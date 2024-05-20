package com.pepperish.customteleportplugin.state;

import com.pepperish.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;

public class TeleportStateManager {

    private static TeleportState currentTeleportState = TeleportState.TPALL_INACTIVE;

    private ConfirmableSubcommand subcommand;
    public TeleportStateManager(ConfirmableSubcommand subcommand) {
        this.subcommand = subcommand;
    }

    public TeleportState getCurrentTeleportState() { return currentTeleportState; }

    public void setCurrentPluginState(TeleportState currentState) { currentTeleportState = currentState; }



}
