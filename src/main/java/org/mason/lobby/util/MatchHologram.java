package org.mason.lobby.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class MatchHologram {

    private final JavaPlugin plugin;
    private final UpcomingMatchUtil upcomingMatchUtil;
    private Hologram hologram;
    private String lastMatches;

    public MatchHologram(JavaPlugin plugin, UpcomingMatchUtil upcomingMatchUtil) {
        this.plugin = plugin;
        this.upcomingMatchUtil = upcomingMatchUtil;
    }

    public Hologram showUpcomingMatches(Location loc) {
        if (hologram != null) {
            hologram.delete(); // Delete the previous hologram if it exists
        }

        // Create a new hologram
        hologram = HologramsAPI.createHologram(plugin, loc);

        // Schedule the task to update the matches every second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            String currentMatches = upcomingMatchUtil.checkUpcomingGamesAndPrint();

            // Check if currentMatches is null or empty, if so, set it to "No upcoming matches"
            if (currentMatches == null || currentMatches.isEmpty()) {
                currentMatches = ChatColor.RED + "No upcoming matches at the moment.\n" +
                        ChatColor.WHITE + "Stay in touch with the " + ChatColor.YELLOW + "Akurra" + ChatColor.WHITE + " community, join the conversation.\n" +
                        ChatColor.WHITE + "Connect with us on " + ChatColor.YELLOW + "Discord" + ChatColor.WHITE + ": " + ChatColor.AQUA + "discord.gg/akurra" + ChatColor.WHITE + ".\n" +
                        ChatColor.WHITE + "Engage, share, and stay up-to-date with the latest matches.";
            }

            // Only update if matches have changed
            if (!currentMatches.equals(lastMatches)) {
                // Clear the hologram except the title line
                while (hologram.size() > 0) {
                    hologram.removeLine(0);
                }

                // Add the new match lines
                String[] matchArray = currentMatches.split("\n");
                for (String match : matchArray) {
                    hologram.appendTextLine(match);
                }

                // Update lastMatches
                lastMatches = currentMatches;
            }
        }, 0L, 200L); // 20- ticks = 10 seconds

        return hologram;
    }
}