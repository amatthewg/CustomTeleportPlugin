package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.permissions.Permission;
import org.bukkit.entity.Player;

/*
Handles the commands
/ctp
/ctp help
 */
public class HelpCommand extends SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get help using CustomTeleport";
    }

    @Override
    public String getSyntax() {
        return "/ctp; /ctp help";
    }

    @Override
    public void perform(Player player, String[] args) {
        // Check for permission
        if(player.hasPermission(Permission.COMMAND_BASE.getPermission())) {
            // TODO send all available cmds
        }
        else {
            player.sendMessage(Permission.NO_PERMISSION.getPermission());
        }
    }
}
