package org.mason.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.mason.lobby.Main;
import org.mason.lobby.util.MatchHologram;

public class MatchHologramCommand implements CommandExecutor {

    private final Main plugin;
    private final MatchHologram matchHologram;

    public MatchHologramCommand(Main plugin, MatchHologram matchHologram) {
        this.plugin = plugin;
        this.matchHologram = matchHologram;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("akurra.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // Set the hologram location to the player's location
        Location newLocation = player.getLocation();
        matchHologram.showUpcomingMatches(newLocation);
        plugin.getConfigManager().saveHologramLocation(newLocation);
        player.sendMessage("Match hologram location set to your current location.");

        return true;
    }
}