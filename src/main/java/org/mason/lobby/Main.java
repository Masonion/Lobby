package org.mason.lobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mason.lobby.commands.ArenaCommand;
import org.mason.lobby.commands.UHCCommand;
import org.mason.lobby.listeners.*;
import org.mason.lobby.util.*;

public class Main extends JavaPlugin {
    private final Bungee bungee;
    private ServerScoreboard serverScoreboard;

    public Main() {
        bungee = new Bungee(this);
    }

    @Override
    public void onEnable() {
        // Initialize the ScoreboardUtil
        ScoreboardUtil.initialize();

        // Initialize the ServerScoreboard instance
        serverScoreboard = new ServerScoreboard(bungee, this);

        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("uhc").setExecutor(new UHCCommand(this));

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