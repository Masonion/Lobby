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

        Bukkit.getScheduler().runTaskTimerAsynchronously(lobby, () -> {
            if (Bukkit.getPlayer(player.getUniqueId()) != null) {
                uhcTeam.setSuffix(ChatColor.WHITE + Integer.toString(bungee.getServerPlayerCount("UHC")));
                arenaTeam.setSuffix(ChatColor.WHITE + Integer.toString(bungee.getServerPlayerCount("Arena")));
                totalTeam.setSuffix(ChatColor.WHITE + Integer.toString(bungee.getTotalPlayerCount()));

                User user = luckPerms.getUserManager().getUser(player.getUniqueId());

                QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(player);
                String playerPrefix = user.getCachedData().getMetaData(queryOptions).getPrefix();
                if (playerPrefix == null) {
                    playerPrefix = "";
                }

                playerPrefix = ChatColor.translateAlternateColorCodes('&', playerPrefix);

                String colorCode = ChatColor.getLastColors(playerPrefix);

                String rank = user.getPrimaryGroup();

                if (!rank.isEmpty()) {
                    rank = Character.toUpperCase(rank.charAt(0)) + rank.substring(1);
                }

                rankTeam.setSuffix(colorCode + ChatColor.BOLD + rank);
            }

        }, 0L, 20L);
    }
}