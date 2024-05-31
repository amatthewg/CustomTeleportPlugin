package com.aiden.customteleportplugin.commands;

import com.aiden.customteleportplugin.commands.subcommands.CancelCommand;
import com.aiden.customteleportplugin.commands.subcommands.Subcommand;
import com.aiden.customteleportplugin.commands.subcommands.ToolCommand;
import com.aiden.customteleportplugin.commands.subcommands.ViewAllCommand;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive.ExclusiveCommand;
import com.aiden.customteleportplugin.util.CommandState;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.ReloadCommand;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.WarnCommand;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive.ReturnCommand;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.exclusive.TpAllCommand;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import com.aiden.customteleportplugin.util.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.View;
import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private static final List<Subcommand> subcommands = new ArrayList<>();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final String ctpAdminPermission = Permission.CTP_ADMIN.getString();

    private static String noPermissionMsg = null;

    private static Boolean adminsShouldBeWarnedOnCommandExecute = null;

    public static List<ConfirmableSubcommand> getConfirmableSubcommands() {
        List<ConfirmableSubcommand> result = new ArrayList<>();
        subcommands.forEach(cmd -> {if(cmd instanceof ConfirmableSubcommand) result.add((ConfirmableSubcommand) cmd);});
        return result;
    }

    public CommandManager(JavaPlugin pl) {
        noPermissionMsg = pl.getConfig().getString("no-permission-message");
        adminsShouldBeWarnedOnCommandExecute = pl.getConfig().getBoolean("warn-admins-on-execute");
        subcommands.add(new ViewAllCommand());
        subcommands.add(new ToolCommand());
        subcommands.add(new CancelCommand());
        subcommands.add(new WarnCommand(pl));
        subcommands.add(new ReloadCommand(pl));
        subcommands.add(new TpAllCommand(pl));
        subcommands.add(new ReturnCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Command can only be used in-game");
            return true;
        }
        Player sender = (Player) commandSender;
        if (!sender.hasPermission(ctpAdminPermission)) {
            chatMessenger.sendChat(sender, noPermissionMsg);
            return true;
        }
        if (strings.length != 1) {
            sendCommandHelp(sender);
            return true;
        }
        for (Subcommand subcommand : subcommands) {
            if (strings[0].equalsIgnoreCase(subcommand.getName())) {
                if (subcommand instanceof ExclusiveCommand) {
                    ExclusiveCommand exclusiveCommand = (ExclusiveCommand) subcommand;
                    if(exclusiveCommand.isCurrentlyExecuted()) {
                        chatMessenger.sendChat(sender, String.format(
                                "&cCannot execute command &a%s &cbecause %s", exclusiveCommand.getSyntax(),
                                exclusiveCommand.getNotReadyMessage()
                        ));
                        return true;
                    }
                }
                if (subcommand instanceof ConfirmableSubcommand) {
                    ConfirmableSubcommand confirmableSubcommand = (ConfirmableSubcommand) subcommand;
                    if(confirmableSubcommand.isConfirmed()) {
                        confirmableSubcommand.setIsConfirmed(false);
                        confirmableSubcommand.perform(sender, strings);
                        if(adminsShouldBeWarnedOnCommandExecute) {
                            chatMessenger.messageAdmins(String.format("&cWARNING: &a%s &cjust executed command &a%s",
                                    sender.getName(), confirmableSubcommand.getSyntax()));
                        }
                    }
                    else {
                        confirmableSubcommand.setIsConfirmed(true);
                        chatMessenger.sendChat(sender, "&c" + confirmableSubcommand.getConfirmationMessage());
                        chatMessenger.sendChat(sender, String.format(
                                "&cRe-enter &a%s &cto confirm, or use &a/ctp cancel &cto cancel.",
                                confirmableSubcommand.getSyntax()
                        ));
                    }
                    return true;
                }
                subcommand.perform(sender, strings);
                return true;
            }
        }
        sendCommandHelp(sender);
        return true;
    }

    private void sendCommandHelp(Player target) {
        chatMessenger.sendChat(target, "&b-------[&3CustomTeleport&b]-------");
        subcommands.forEach(cmd -> chatMessenger.sendChat(target, String.format(
                "&9%s &6%s", cmd.getSyntax(), cmd.getDescription()
        )));
        chatMessenger.sendChat(target, "&b----------------------------");
    }

}
