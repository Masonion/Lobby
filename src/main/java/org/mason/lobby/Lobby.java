package org.mason.lobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mason.lobby.commands.ArenaCommand;
import org.mason.lobby.commands.PlayerCountCommand;
import org.mason.lobby.commands.UHCCommand;
import org.mason.lobby.listeners.PlayerJoinListener;
import org.mason.lobby.listeners.PlayerMoveListener;
import org.mason.lobby.listeners.WeatherChangeListener;
import org.mason.lobby.listeners.PlayerQuitListener;

public class Lobby extends JavaPlugin {
    private final Bungee bungee;

    public Lobby() {
        bungee = new Bungee(this);
    }

    @Override
    public void onEnable() {
        bungee.startTask();
        getCommand("playercount").setExecutor(new PlayerCountCommand(this, bungee));
        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("uhc").setExecutor(new UHCCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);

        // Register plugin channels
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bungee);

        // Initialize ServerSelector and register events
        ServerSelector serverSelector = new ServerSelector(bungee);
        getServer().getPluginManager().registerEvents(serverSelector, this);

    }
}

