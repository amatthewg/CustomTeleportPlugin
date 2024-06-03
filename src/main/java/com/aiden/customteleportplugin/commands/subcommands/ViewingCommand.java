package com.aiden.customteleportplugin.commands.subcommands;

import com.aiden.customteleportplugin.managers.BlockLocationManager;
import com.aiden.customteleportplugin.messengers.PlayerChatMessenger;
import org.bukkit.entity.Player;

public class ViewingCommand extends Subcommand {

    private static final PlayerChatMessenger chatMessenger = new PlayerChatMessenger();

    private static boolean commandToggledOn = false;

    @Override
    public String getName() { return "viewing"; }

    @Override
    public String getDescription() { return "Enable viewing all set blocks"; }

    @Override
    public String getSyntax() { return "/ctp viewing"; }

    @Override
    public void perform(Player sender, String[] args) {
        int blockCount;
        if(!commandToggledOn) {
            commandToggledOn = true;
            blockCount = BlockLocationManager.setBlockViewingEnabled(true);
            chatMessenger.sendChat(sender, String.format(
                    "&aEnabled block viewing and converted &e%d &ablocks", blockCount
            ));

        }
        else {
            commandToggledOn = false;
            blockCount = BlockLocationManager.setBlockViewingEnabled(false);
            chatMessenger.sendChat(sender, String.format(
                    "&aDisabled block viewing and reverted &e%d &ablocks", blockCount
            ));
        }
    }

}
