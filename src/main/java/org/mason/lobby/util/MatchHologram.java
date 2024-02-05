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

    private List<TextLine> lines = new ArrayList<>();
    public MatchHologram(JavaPlugin plugin, UpcomingMatchUtil upcomingMatchUtil) {
        this.plugin = plugin;
        this.upcomingMatchUtil = upcomingMatchUtil;
    }

    public Hologram showUpcomingMatches(Location loc) {
        if (hologram != null) {
            hologram.delete();
        }

        hologram = HologramsAPI.createHologram(plugin, loc);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            String currentMatches = upcomingMatchUtil.checkUpcomingGamesAndPrint();

            if (currentMatches == null || currentMatches.isEmpty()) {
                currentMatches = ChatColor.RED + "No upcoming matches at the moment.\n" +
                        ChatColor.WHITE + "Stay in touch with the " + ChatColor.YELLOW + "Akurra" + ChatColor.WHITE + "\ncommunity and join the conversation.\n" +
                        ChatColor.WHITE + "Connect with us on " + ChatColor.YELLOW + "Discord\n" + ChatColor.AQUA + "discord.gg/akurra\n";
            }

            if (!currentMatches.equals(lastMatches)) {
                String[] matchArray = currentMatches.split("\n");

                if (matchArray.length != lines.size()) {
                    while (hologram.size() > 0) {
                        hologram.removeLine(0);
                    }
                    lines.clear();

                    for (String match : matchArray) {
                        lines.add(hologram.appendTextLine(match));
                    }
                } else {
                    for (int i = 0; i < matchArray.length; i++) {
                        lines.get(i).setText(matchArray[i]);
                    }
                }

                lastMatches = currentMatches;
            }
        }, 0L, 200L);

        return hologram;
    }
}