package com.pepperish.customteleportplugin.commands;

import com.pepperish.customteleportplugin.commands.subcommands.*;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {
    private ArrayList<SubCommand> subcommands = new ArrayList<>();
    private static JavaPlugin plugin;

    private static String coloredNoPermissionMsg =
            ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!");

    private static PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    public CommandManager(JavaPlugin pl) {
        plugin = pl;
        subcommands.add(new ToolCommand());
        subcommands.add(new ViewAllCommand());
        subcommands.add(new TpAllCommand(plugin));
        subcommands.add(new ReturnCommand(plugin));
        subcommands.add(new ReloadCommand(plugin));
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(!player.hasPermission("ctp.command")) {
                player.sendMessage(coloredNoPermissionMsg);
                return true;
            }
            if(strings.length == 0) {
                // Player ran /ctp
                sendCommandHelp(player);
            }
            if(strings.length == 1) {
                // Player ran /ctp with one or more args
                for (SubCommand subcommand : this.subcommands) {
                    if (strings[0].equalsIgnoreCase(subcommand.getName())) {
                        if(player.hasPermission(subcommand.getPermissionString())) {
                            subcommand.perform(player, strings);
                            return true;
                        }
                        else {
                            player.sendMessage(coloredNoPermissionMsg);
                            return true;
                        }
                    }
                }
                // Subcommand not recognized
                sendCommandHelp(player);
                return true;
            }
            if(strings.length > 1) {
                sendCommandHelp(player);
            }
        }
        return true;
    }
    private void sendCommandHelp(Player target) {
        chatMessenger.sendChat(target, "&b-------[&3CustomTeleport&b]-------");
        for(SubCommand subCommand : this.subcommands) {
            String syntax = subCommand.getSyntax();
            String description = subCommand.getDescription();
            chatMessenger.sendChat(target, String.format("&9%s &6%s", syntax, description));
        }
        chatMessenger.sendChat(target, "&b----------------------------");
    }


}
