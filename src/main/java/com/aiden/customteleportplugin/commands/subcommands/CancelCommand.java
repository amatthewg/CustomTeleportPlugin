package com.aiden.customteleportplugin.commands.subcommands;

import com.aiden.customteleportplugin.commands.CommandManager;
import com.aiden.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.entity.Player;

import java.util.Map;

public class CancelCommand extends Subcommand {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    public CancelCommand() {}

    @Override
    public String getName() { return "cancel"; }

    @Override
    public String getDescription() { return "Cancel the execution of a command that needs confirmation to be executed."; }

    @Override
    public String getSyntax() { return "/ctp cancel"; }

    @Override
    public void perform(Player sender, String[] args) {
        Map<ConfirmableSubcommand, Boolean> commandConfirmationStates = CommandManager.getCommandConfirmationStates();
        int canclledCommandCount = 0;
        for(ConfirmableSubcommand key : commandConfirmationStates.keySet()) {
            if(commandConfirmationStates.get(key)) {
                CommandManager.addCommandConfirmationState(key, false);
                chatMessenger.sendChat(sender, String.format("&e%s &awas cancelled!", key.getSyntax()));
                canclledCommandCount++;
            }
        }
        if(canclledCommandCount == 0) {
            chatMessenger.sendChat(sender, "&cThere were no commands to be cancelled!");
        }
    }

}
