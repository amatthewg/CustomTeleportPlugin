package com.pepperish.customteleportplugin.managers;

import com.pepperish.customteleportplugin.CustomTeleportPlugin;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.List;
import java.util.Set;

// Class used to handle access to the json data file
public class FileManager {

    private static CustomTeleportPlugin plugin;
    private static File saveFile = null;

    private static boolean fileOperationActive = false;

    public FileManager(CustomTeleportPlugin pl) {
        plugin = pl;
        saveFile = new File(plugin.getDataFolder(), "block_locations.ser");
    }

    public FileManager() {

    }

    public boolean createFileIfNotExists() {
        try {
            return saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveSetToFile(Set<Location> set) {
        try {
            FileOutputStream fileOut = new FileOutputStream(saveFile);
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(fileOut);
            out.writeObject(set);
            out.close();
            fileOut.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Set<Location> loadSetFromFile() {
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            BukkitObjectInputStream in = new BukkitObjectInputStream(fileIn);
            Set<Location> result = (Set<Location>) in.readObject();
            in.close();
            fileIn.close();
            return result;
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
