package org.mason.lobby.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mason.lobby.Main;

public class ServerScoreboard {
    private final Main lobby;
    private Bungee bungee;

    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    LuckPerms luckPerms = provider.getProvider();

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
        arenaTeam.addEntry(ChatColor.AQUA + "Arena: ");
        Score arenaScore = objective.getScore(ChatColor.AQUA + "Arena: ");
        arenaScore.setScore(counter++);

        Team uhcTeam = scoreboard.registerNewTeam("UHC");
        uhcTeam.addEntry(ChatColor.AQUA + "UHC: ");
        Score uhcScore = objective.getScore(ChatColor.AQUA + "UHC: ");
        uhcScore.setScore(counter++);

        Score whitespace2 = objective.getScore(ChatColor.AQUA + " ");
        whitespace2.setScore(counter++);

        Team totalTeam = scoreboard.registerNewTeam("Online");
        totalTeam.addEntry(ChatColor.AQUA + "Online: ");
        Score totalScore = objective.getScore(ChatColor.AQUA + "Online: ");
        totalScore.setScore(counter++);

        Team rankTeam = scoreboard.registerNewTeam("Rank");
        rankTeam.addEntry(ChatColor.AQUA + "Rank: ");
        Score rankScore = objective.getScore(ChatColor.AQUA + "Rank: ");
        rankScore.setScore(counter++);

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

            // Fetch user from LuckPerms
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());

            // Get the prefix from the LuckPerms user
            QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(player);
            String playerPrefix = user.getCachedData().getMetaData(queryOptions).getPrefix();
            if (playerPrefix == null) {
                playerPrefix = "";
            }

            // Convert color codes
            playerPrefix = ChatColor.translateAlternateColorCodes('&', playerPrefix);

            // Only get the first color code from the prefix for the rank
            String colorCode = ChatColor.getLastColors(playerPrefix);

            // This gets the primary group of the user. Depending on your setup, you might need to change this to match your setup
            String rank = user.getPrimaryGroup();

            // Capitalize the first letter
            if (!rank.isEmpty()) {
                rank = Character.toUpperCase(rank.charAt(0)) + rank.substring(1);
            }

            // Set the suffix of the rankTeam
            rankTeam.setSuffix(colorCode + ChatColor.BOLD + rank);

        }, 0L, 20L);
    }
}