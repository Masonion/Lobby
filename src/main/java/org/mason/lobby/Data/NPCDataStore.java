package org.mason.lobby.Data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class NPCDataStore {
    private FileConfiguration configuration;
    private File file;

    public NPCDataStore(JavaPlugin plugin) {
        try {
            file = new File(plugin.getDataFolder() + File.separator + "npc.yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            configuration = YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerName(int npcId, String serverName) {
        configuration.set(String.valueOf(npcId), serverName);
        save();
    }

    public String getServerName(int npcId) {
        return configuration.getString(String.valueOf(npcId));
    }

    public void removeNPC(int npcId) {
        configuration.set(String.valueOf(npcId), null);
        save();
    }

    private void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}