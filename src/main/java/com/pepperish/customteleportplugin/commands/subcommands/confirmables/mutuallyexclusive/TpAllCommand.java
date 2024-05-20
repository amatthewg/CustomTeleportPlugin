package com.pepperish.customteleportplugin.commands.subcommands.confirmables.mutuallyexclusive;

import com.pepperish.customteleportplugin.commands.subcommands.confirmables.ConfirmableSubcommand;
import com.pepperish.customteleportplugin.managers.TeleportManager;
import com.pepperish.customteleportplugin.messengers.PlayerChatMessenger;
import com.pepperish.customteleportplugin.util.CommandState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

// Handles the command /ctp tpall
public class TpAllCommand extends MutuallyExclusiveCommand {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static final TeleportManager tpManager = new TeleportManager();

    private static CommandState commandState;

    private static JavaPlugin plugin;

    private static boolean shouldWarnAdminsOnTeleportResult;

    public TpAllCommand(JavaPlugin pl) {
        plugin = pl;
        setCommandState(CommandState.READY);
        shouldWarnAdminsOnTeleportResult = plugin.getConfig().getBoolean("warn-admins-on-teleport");
    }

    public TpAllCommand() {}


    @Override
    public String getName() { return "tpall"; }

    @Override
    public String getDescription() { return "Teleport all players to the set locations."; }

    @Override
    public String getSyntax() { return "/ctp tpall"; }

    @Override
    public String getConfirmationMessage() { return "WARNING: You are about to teleport the players to their blocks!"; }

    @Override
    public boolean isCommandActive() {
        return false;
    }

    @Override
    public String getIsActiveMessage() {
        return null;
    }

    @Override
    public void perform(Player sender, String[] args) {
        Optional<Integer> teleportedPlayerCount = tpManager.tpAllPlayers();
        if(teleportedPlayerCount.isPresent()) {
            if(shouldWarnAdminsOnTeleportResult) {
                chatMessenger.messageAdmins(String.format(
                        "&aSuccessfully teleported &e%d &aplayers", teleportedPlayerCount.get()));
                return;
            }
            chatMessenger.sendChat(sender, String.format(
                    "&aSuccessfully teleported &e%d &aplayers", teleportedPlayerCount.get()
            ));
        }
        else {
            if(shouldWarnAdminsOnTeleportResult) {
                chatMessenger.messageAdmins("&cWARNING: Could not teleport all players because there\n" +
                        "weren't enough set block locations!");
                return;
            }
            chatMessenger.sendChat(sender, "&cWARNING: Could not teleport all players because there\n" +
                    "weren't enough set block locations!");
        }
        setCommandState(CommandState.NOT_READY);

    }

    @Override
    public CommandState getCommandState() {
        return commandState;
    }

    @Override
    public void setCommandState(CommandState state) {
        commandState = state;
    }

    @Override
    public String getNotReadyMessage() { return "the players are currently teleported!"; }

    @Override
    public MutuallyExclusiveCommand getMutual() {
        return new ReturnCommand();
    }
}
