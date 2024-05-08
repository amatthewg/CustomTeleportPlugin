package com.pepperish.customteleportplugin;

import com.pepperish.customteleportplugin.DAL.BlockStorage;
import com.pepperish.customteleportplugin.DAL.DAL;
import com.pepperish.customteleportplugin.DAL.FileManager;
import com.pepperish.customteleportplugin.commands.CommandManager;
import com.pepperish.customteleportplugin.listeners.HandleToolUse;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class CustomTeleportPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        DAL dal = new DAL(this);
        getServer().getPluginManager().registerEvents(new HandleToolUse(this), this);
        getCommand("ctp").setExecutor(new CommandManager());

        FileManager fileManager = new FileManager(this);
        Optional<Boolean> optional = fileManager.createFileIfNotExists();
        if(optional.isPresent()) {
            boolean fileWasCreated = optional.get();
            if(fileWasCreated) {
                BlockStorage blockStorage = new BlockStorage(new HashSet<>());
            }
            else {
                CompletableFuture<Void> future = fileManager.loadSetFromJson().thenApply(locationSet -> {
                    if(locationSet != null) {
                        BlockStorage blockStorage = new BlockStorage(locationSet);
                    }
                    else {
                        BlockStorage blockStorage = new BlockStorage(new HashSet<>());
                    }
                    return null;
                });
            }
        }
        else {
            getLogger().log(Level.SEVERE, "The plugin was unable to create the necessary save file. Please restart the server.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
