package com.aiden.customteleportplugin.managers;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FileManager {

    private static File saveFile = null;

    public FileManager(JavaPlugin pl) {
        saveFile = new File(pl.getDataFolder(), "block_locations.ser");
    }

    public FileManager() {}

    public Optional<Boolean> createFileIfNotExists() {
        try {
            return Optional.of(saveFile.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean saveSetToFile(Set<Location> set) {
        try {
            FileOutputStream fileOut = new FileOutputStream(saveFile);
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(fileOut);
            out.writeObject(set);
            out.close();
            fileOut.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Set<Location>> loadSetFromFile() {
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            BukkitObjectInputStream in = new BukkitObjectInputStream(fileIn);
            Object obj = in.readObject();
            in.close();
            fileIn.close();
            return Optional.of(((Set<Location>) obj).stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    /*
    public Optional<Set<Location>> loadSetFromFile() {
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            BukkitObjectInputStream in = new BukkitObjectInputStream(fileIn);
            Object obj = in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Reached point 1");
            if(obj instanceof Set<?>) {
                System.out.println("Reached point 2");
                Set<?> tempSet = (Set<?>) obj;
                Set<Location> resultSet = new HashSet<>();
                System.out.println("Reached point 3");
                boolean validSet = tempSet.stream().allMatch(element -> {
                    System.out.println("Reached point 4");
                    if(element instanceof Location) {
                        System.out.println("Reached point 5");
                        resultSet.add((Location) element);
                        return true;
                    }
                    System.out.println("Reached point 6");
                    return false;
                });
                System.out.println("Reached point 7");
                if(validSet) {
                    System.out.println("Reached point 8");
                    return Optional.of(resultSet);
                }
            }
            System.out.println("Reached point 9");
            throw new ClassNotFoundException();
        } catch(IOException | ClassNotFoundException e) {
            System.out.println("Reached point 10");
            e.printStackTrace();
            return Optional.empty();
        }
    }

     */

}
