package org.mason.lobby.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;


import org.mason.lobby.Main;

public class ServerScoreboard {
    private final Main lobby;
    private Bungee bungee;

    public ServerScoreboard(Bungee bungee, Main lobby) {
        this.lobby = lobby;
        this.bungee = bungee;
    }

    public void showServerScoreboard(Player player) {
        Scoreboard scoreboard = ScoreboardUtil.getNewScoreboard(player);
        Objective objective = ScoreboardUtil.getObjective(scoreboard, "serverInfo", DisplaySlot.SIDEBAR, ChatColor.AQUA + "Akurra Lobby");

        int counter = 1;

        Team addressTeam = scoreboard.registerNewTeam("Address");
        addressTeam.setSuffix(ChatColor.GRAY + "akurra.net");
        addressTeam.addEntry(ChatColor.GRAY + "au.");
        Score addressScore = objective.getScore(ChatColor.GRAY + "au.");
        addressScore.setScore(counter++);

        Score whitespace1 = objective.getScore(ChatColor.DARK_RED + " ");
        whitespace1.setScore(counter++);

        Team arenaTeam = scoreboard.registerNewTeam("Arena");
        arenaTeam.addEntry(ChatColor.AQUA + "Arena" + ChatColor.GRAY + ": ");
        Score arenaScore = objective.getScore(ChatColor.AQUA + "Arena" + ChatColor.GRAY + ": ");
        arenaScore.setScore(counter++);

        Team uhcTeam = scoreboard.registerNewTeam("UHC");
        uhcTeam.addEntry(ChatColor.AQUA + "UHC" + ChatColor.GRAY + ": ");
        Score uhcScore = objective.getScore(ChatColor.AQUA + "UHC" + ChatColor.GRAY + ": ");
        uhcScore.setScore(counter++);

        Score whitespace2 = objective.getScore(ChatColor.AQUA + " ");
        whitespace2.setScore(counter++);

        Team totalTeam = scoreboard.registerNewTeam("Online");
        totalTeam.addEntry(ChatColor.AQUA + "Online" + ChatColor.GRAY + ": ");
        Score totalScore = objective.getScore(ChatColor.AQUA + "Online" + ChatColor.GRAY + ": ");
        totalScore.setScore(counter++);

        Score whitespace3 = objective.getScore(ChatColor.RED + " ");
        whitespace3.setScore(counter++);


        player.setScoreboard(scoreboard);

        // Register a new repeating task using Bukkit's scheduler.
        // This task will run every 30 seconds (20 ticks, since 1 second is approximately 20 ticks).
        Bukkit.getScheduler().runTaskTimerAsynchronously(lobby, () -> {
            // Update the suffixes of the teams here.
            uhcTeam.setSuffix(ChatColor.WHITE + Integer.toString(bungee.getServerPlayerCount("UHC")));
            arenaTeam.setSuffix(ChatColor.WHITE + Integer.toString(bungee.getServerPlayerCount("Arena")));
            totalTeam.setSuffix(ChatColor.WHITE + Integer.toString(bungee.getTotalPlayerCount()));

        }, 0L, 20L);
    }
}