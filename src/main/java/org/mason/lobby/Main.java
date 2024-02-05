package org.mason.lobby;

import net.akurra.akurrachat.AkurraChat;
import net.akurra.akurrachat.PlayerSettings;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mason.lobby.commands.*;
import org.mason.lobby.listeners.*;
import org.mason.lobby.util.*;

public class Main extends JavaPlugin {
    private final Bungee bungee;
    private ServerScoreboard serverScoreboard;
    private MatchHologram matchHologram;
    private ConfigManager configManager;

    public Main() {
        bungee = new Bungee(this);
    }

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        ScoreboardUtil.initialize();
        serverScoreboard = new ServerScoreboard(bungee, this);

        UpcomingMatchUtil upcomingMatchUtil = new UpcomingMatchUtil();
        this.getCommand("upcomingmatch").setExecutor(new UpcomingMatchCommand(upcomingMatchUtil));

        Location hologramLocation = configManager.loadHologramLocation();
        matchHologram = new MatchHologram(this, upcomingMatchUtil);
        if(hologramLocation != null) {
            matchHologram.showUpcomingMatches(hologramLocation);
        }

        getCommand("matchhologram").setExecutor(new MatchHologramCommand(this, matchHologram));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));

        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("uhc").setExecutor(new UHCCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, serverScoreboard), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleFlightListener(), this);


        ServerSelector serverSelector = new ServerSelector(bungee, upcomingMatchUtil);

        getServer().getPluginManager().registerEvents(serverSelector, this);
        getServer().getPluginManager().registerEvents(new SettingsSelector(), this);



        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        bungee.startPlayerCountTask("UHC", "Arena");

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    if (player.hasPermission("akurra.supporter")) {
                        PlayerSettings settings = AkurraChat.getInstance().getPlayerSettings(player.getUniqueId());
                        if (!settings.getFly()) {
                            player.setFlying(false);
                        }
                    }
                    else {
                        player.setFlying(false);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 10L);
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
}