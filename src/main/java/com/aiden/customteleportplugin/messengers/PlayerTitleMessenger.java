package com.aiden.customteleportplugin.messengers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerTitleMessenger {

    public PlayerTitleMessenger() {}

    public void sendTitleMsg(Player target, String title, String subtitle) {
        Component resultTitle = LegacyComponentSerializer.legacyAmpersand().deserialize(title);
        Component resultSubtitle = LegacyComponentSerializer.legacyAmpersand().deserialize(subtitle);
        target.showTitle(Title.title(resultTitle, resultSubtitle));
    }
}
