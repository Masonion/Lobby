package org.mason.lobby.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MatchHologram {

    private final JavaPlugin plugin;
    private final UpcomingMatchUtil upcomingMatchUtil;
    private Hologram hologram;
    private String lastMatches;

    private List<TextLine> lines = new ArrayList<>(); // Track of the lines

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
                        ChatColor.WHITE + "Stay in touch with the " + ChatColor.YELLOW + "Akurra" + ChatColor.WHITE + "\ncommunity and join the conversation.\n" +
                        ChatColor.WHITE + "Connect with us on " + ChatColor.YELLOW + "Discord\n" + ChatColor.AQUA + "discord.gg/akurra\n";
            }

            // Only update if matches have changed
            if (!currentMatches.equals(lastMatches)) {
                String[] matchArray = currentMatches.split("\n");

                // If the size of matchArray and lines don't match, clear all lines and reset
                if (matchArray.length != lines.size()) {
                    while (hologram.size() > 0) {
                        hologram.removeLine(0);
                    }
                    lines.clear();

                    // Add the new match lines
                    for (String match : matchArray) {
                        lines.add(hologram.appendTextLine(match));
                    }
                } else {
                    // Update the existing lines
                    for (int i = 0; i < matchArray.length; i++) {
                        lines.get(i).setText(matchArray[i]);
                    }
                }

                // Update lastMatches
                lastMatches = currentMatches;
            }
        }, 0L, 200L); // 20 ticks = 1 second, 200 ticks = 10 seconds

        return hologram;
    }
}