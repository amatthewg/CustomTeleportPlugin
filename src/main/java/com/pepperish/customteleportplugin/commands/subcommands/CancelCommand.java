package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.entity.Player;

public class CancelCommand extends SubCommand {
    @Override
    public String getName() {
        return "cancel";
    }

    @Override
    public String getDescription() {
        return "Cancel the execution of &9/ctp tpall &6or &9/ctp return";
    }

    @Override
    public String getSyntax() {
        return "/ctp cancel";
    }

    @Override
    public String getPermissionString() {
        return Permission.CTP_ADMIN.getString();
    }

    @Override
    public void perform(Player sender, String[] args) {

    }
}
