package org.mason.lobby;

import net.citizensnpcs.Citizens;
import org.bukkit.plugin.java.JavaPlugin;
import org.mason.lobby.Data.NPCDataStore;
import org.mason.lobby.commands.*;
import org.mason.lobby.listeners.*;
import org.mason.lobby.util.*;

public class Main extends JavaPlugin {
    private final Bungee bungee;
    private ServerScoreboard serverScoreboard;
    private Citizens citizens;

    public Main() {
        bungee = new Bungee(this);
    }

    @Override
    public void onEnable() {
        // Ensure that the Citizens plugin is loaded
        if (getServer().getPluginManager().getPlugin("Citizens") == null) {
            getLogger().severe("Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Citizens citizens = (Citizens) getServer().getPluginManager().getPlugin("Citizens");

        // Initialize the ScoreboardUtil
        ScoreboardUtil.initialize();

        // Initialize the ServerScoreboard instance
        serverScoreboard = new ServerScoreboard(bungee, this);

        UpcomingMatchUtil upcomingMatchUtil = new UpcomingMatchUtil();
        this.getCommand("upcomingmatch").setExecutor(new UpcomingMatchCommand(upcomingMatchUtil));

        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("uhc").setExecutor(new UHCCommand(this));

        // Initialize NPCDataStore
        NPCDataStore npcDataStore = new NPCDataStore(this);

        getCommand("npc").setExecutor(new NPCCommand(citizens, npcDataStore));
        getCommand("listnpcs").setExecutor(new ListNPCsCommand(citizens, npcDataStore));
        getCommand("deletenpc").setExecutor(new DeleteNPCCommand(citizens, npcDataStore));

        getServer().getPluginManager().registerEvents(new NPCClickListener(bungee, npcDataStore), this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, serverScoreboard), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleFlightListener(this), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        bungee.startPlayerCountTask("UHC", "Arena");

        // Initialize ServerSelector and register events
        ServerSelector serverSelector = new ServerSelector(bungee);
        getServer().getPluginManager().registerEvents(serverSelector, this);
    }
}
