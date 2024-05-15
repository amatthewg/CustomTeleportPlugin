package com.pepperish.customteleportplugin.commands.subcommands;

import com.pepperish.customteleportplugin.managers.LocationManager;
import com.pepperish.customteleportplugin.commands.SubCommand;
import com.pepperish.customteleportplugin.managers.TeleportManager;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ReturnCommand extends SubCommand {


    private static LocationManager locationManager = new LocationManager();

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static String shouldBeTeleportedPermission = Permission.SHOULD_BE_TELEPORTED.getString();

    private static TeleportManager tpManager = new TeleportManager();

    private static JavaPlugin plugin;

    private boolean commandWasRun = false;

    public ReturnCommand(JavaPlugin pl) {
        plugin = pl;
    }

    @Override
    public String getName() {
        return "return";
    }

    @Override
    public String getDescription() {
        return "Return all players to their original location";
    }

    @Override
    public String getSyntax() {
        return "/ctp return";
    }

    @Override
    public String getPermissionString() {
        return Permission.CTP_ADMIN.getString();
    }

    @Override
    public void perform(Player sender, String[] args) {
        if(!TpAllCommand.getCommandIsActive()) {
            chatMessenger.sendChat(sender, "&cCan't return the players because they weren't already teleported!");
            return;
        }
        if(!commandWasRun) {

        }
        chatMessenger.messageAdmins(String.format("&e[CustomTeleport] &6(sent to all CTP admins)\n"+
                "&e%s &ajust executed &e/ctp return", sender.getName()));
    }
}
