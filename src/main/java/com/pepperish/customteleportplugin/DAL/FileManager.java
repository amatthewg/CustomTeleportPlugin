package com.pepperish.customteleportplugin.DAL;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.pepperish.customteleportplugin.CustomTeleportPlugin;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

// Class used to handle access to the json data file
public class FileManager {
    private static File jsonFile = null;
    private static CustomTeleportPlugin plugin;
    private static boolean fileError = false;
    private static boolean isCurrentlyLoading = false;
    private static boolean isCurrentlySaving = false;

    public FileManager(CustomTeleportPlugin pl) {
        plugin = pl;
        jsonFile = new File(plugin.getDataFolder(), "block_locations.json");

        // Create json file if not exists
        if(!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
            } catch(IOException e) {
                fileError = true;
                e.printStackTrace();
            }
        }
    }

    public FileManager() {

    }


    public Optional<Boolean> createFileIfNotExists() {
        try{
            return Optional.of(jsonFile.createNewFile());
        } catch(IOException e) {
            e.printStackTrace();
            fileError = true;
            return Optional.empty();
        }
    }
    public boolean isFileError() { return fileError; }
    public boolean isCurrentlyLoading() { return isCurrentlyLoading; }
    public boolean isCurrentlySaving() { return isCurrentlySaving; }


    public CompletableFuture<Boolean> saveSetToJson(HashSet<Location> set) {
        isCurrentlySaving = true;
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            Gson gson = new Gson();
            String json = gson.toJson(set);
            try(Writer writer = new FileWriter(jsonFile)) {
                writer.write(json);
                future.complete(true);

            } catch(IOException e) {
                e.printStackTrace();
                fileError = true;
                future.complete(false);
            }
        });
        isCurrentlySaving = false;
        return future;
    }
    public CompletableFuture<HashSet<Location>> loadSetFromJson() {
        isCurrentlyLoading = true;
        CompletableFuture<HashSet<Location>> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
           Gson gson = new Gson();
           Type setType = new TypeToken<HashSet<Location>>(){}.getType();
           try(Reader reader = new FileReader(jsonFile)) {
               HashSet<Location> resultSet = gson.fromJson(reader, setType);
               reader.close();
               // Gson will return a null value if the file is empty
               future.complete(resultSet);
           } catch(IOException e) {
               e.printStackTrace();
               fileError = true;
               future.complete(null);
           }
        });
        isCurrentlyLoading = false;
        return future;
    }

}
