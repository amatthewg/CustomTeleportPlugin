package com.pepperish.customteleportplugin.commands;

import com.pepperish.customteleportplugin.commands.subcommands.*;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.ReloadCommand;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.WarnCommand;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive.ReturnCommand;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive.MutuallyExclusiveCommand;
import com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive.TpAllCommand;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.util.CommandState;
import com.pepperish.customteleportplugin.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {
    private static final ArrayList<Subcommand> subcommands = new ArrayList<>();

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final Map<ConfirmableSubcommand, Boolean> commandConfirmationStates = new HashMap<>();

    private static JavaPlugin plugin;

    private static String noPermissionMsg = null;

    private static boolean adminsShouldBeWarnedOnCommandExecute;

    public static Map<ConfirmableSubcommand, Boolean> getCommandConfirmationStates() {
        return commandConfirmationStates;
    }

    public static void addCommandConfirmationState(ConfirmableSubcommand subcommand, boolean state) {
        commandConfirmationStates.put(subcommand, state);
    }

    public CommandManager(JavaPlugin pl) {
        plugin = pl;
        noPermissionMsg = plugin.getConfig().getString("no-permission-message");
        adminsShouldBeWarnedOnCommandExecute = plugin.getConfig().getBoolean("warn-admins-on-execute");
        subcommands.add(new ToolCommand());
        subcommands.add(new ViewAllCommand());
        TpAllCommand tpAllCommand = new TpAllCommand(plugin);
        subcommands.add(tpAllCommand);
        ReturnCommand returnCommand = new ReturnCommand(plugin);
        subcommands.add(returnCommand);
        subcommands.add(new CancelCommand());
        WarnCommand warnCommand = new WarnCommand(plugin);
        subcommands.add(warnCommand);
        ReloadCommand reloadCommand = new ReloadCommand(plugin);
        subcommands.add(reloadCommand);
        commandConfirmationStates.put(tpAllCommand, false);
        commandConfirmationStates.put(returnCommand, false);
        commandConfirmationStates.put(warnCommand, false);
        commandConfirmationStates.put(reloadCommand, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) return true;
        Player sender = (Player) commandSender;
        if (!sender.hasPermission(Permission.CTP_ADMIN.getString())) {
            chatMessenger.sendChat(sender, noPermissionMsg);
            return true;
        }
        if (strings.length != 1) {
            sendCommandHelp(sender);
            return true;
        }

        for (Subcommand subcommand : subcommands) {
            if (strings[0].equalsIgnoreCase(subcommand.getName())) {
                if (subcommand instanceof ConfirmableSubcommand) {
                    ConfirmableSubcommand confirmableSubcommand = (ConfirmableSubcommand) subcommand;
                    if(confirmableSubcommand instanceof MutuallyExclusiveCommand) {
                        MutuallyExclusiveCommand targetCommand = (MutuallyExclusiveCommand) confirmableSubcommand;
                        if(targetCommand.getCommandState().equals(CommandState.NOT_READY)){
                            sender.sendMessage(String.format(
                                    "&cCannot execute command &a%s &cbecause %s", confirmableSubcommand.getSyntax(),
                                    targetCommand.getNotReadyMessage()));
                            return true;
                        }
                    }
                    Boolean commandWasRun = commandConfirmationStates.get(confirmableSubcommand);
                    if (commandWasRun) {
                        commandConfirmationStates.put(confirmableSubcommand, false);
                        confirmableSubcommand.perform(sender, strings);
                        if(adminsShouldBeWarnedOnCommandExecute) {
                            chatMessenger.messageAdmins(String.format("&b(Sent to all CTP admins)\n" +
                                    "&cWARNING: &a%s &cjust executed command &a%s", sender.getName(),
                                    confirmableSubcommand.getSyntax()));
                        }
                        return true;
                    } else {
                        commandConfirmationStates.put(confirmableSubcommand, true);
                        chatMessenger.sendChat(sender, "&c" + confirmableSubcommand.getConfirmationMessage());
                        chatMessenger.sendChat(sender, String.format(
                                "&cRe-enter &a%s &cto confirm, or use &a/ctp cancel &cto cancel.", confirmableSubcommand.getSyntax()
                        ));
                        // TODO: if one admin runs a confirmablesubcommand first, then another admin runs it
                        // right after them, it won't ask the second admin for confirmation
                        return true;
                    }
                } else {
                    subcommand.perform(sender, strings);
                    return true;
                }
            }
        }
        return true;
    }

    private void sendCommandHelp(Player target) {
        chatMessenger.sendChat(target, "&b-------[&3CustomTeleport&b]-------");
        for (Subcommand subCommand : subcommands) {
            String syntax = subCommand.getSyntax();
            String description = subCommand.getDescription();
            chatMessenger.sendChat(target, String.format("&9%s &6%s", syntax, description));
        }
        chatMessenger.sendChat(target, "&b----------------------------");
    }


}
