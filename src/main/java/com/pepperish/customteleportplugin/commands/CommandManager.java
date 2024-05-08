package com.pepperish.customteleportplugin.commands;

import com.pepperish.customteleportplugin.commands.subcommands.HelpCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {
    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        subcommands.add(new HelpCommand());
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(strings.length == 0) {
                // Player ran /ctp
                this.subcommands.get(0).perform(player, strings);
            }
            if(strings.length > 0) {
                // Player ran /ctp with one or more args
                for (SubCommand subcommand : this.subcommands) {
                    if (strings[0].equalsIgnoreCase(subcommand.getName())) {
                        subcommand.perform(player, strings);
                    }
                }
            }
        }







        return true;
    }


}
