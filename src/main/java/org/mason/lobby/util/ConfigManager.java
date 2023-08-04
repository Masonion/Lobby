package org.mason.lobby.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveHologramLocation(Location location) {
        saveLocationToConfig("hologram", location);
    }

    public Location loadHologramLocation() {
        return loadLocationFromConfig("hologram");
    }

    public void saveSpawnLocation(Location location) {
        saveLocationToConfig("spawn", location);
    }

    public Location loadSpawnLocation() {
        return loadLocationFromConfig("spawn");
    }

    private void saveLocationToConfig(String prefix, Location location) {
        config.set(prefix + ".world", location.getWorld().getName());
        config.set(prefix + ".x", location.getX());
        config.set(prefix + ".y", location.getY());
        config.set(prefix + ".z", location.getZ());

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Location loadLocationFromConfig(String prefix) {
        World world = Bukkit.getWorld(config.getString(prefix + ".world"));
        double x = config.getDouble(prefix + ".x");
        double y = config.getDouble(prefix + ".y");
        double z = config.getDouble(prefix + ".z");

        if (world == null) {
            return null;
        }

        return new Location(world, x, y, z);
    }
}