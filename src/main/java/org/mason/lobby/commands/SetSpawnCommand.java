package org.mason.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.mason.lobby.Main;

public class SetSpawnCommand implements CommandExecutor {

    private final Main plugin;

    public SetSpawnCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission("akurra.admin")) {
            // Set the spawn location to the player's location
            Location newLocation = player.getLocation();
            plugin.getConfigManager().saveSpawnLocation(newLocation);
            player.sendMessage("Spawn location set to your current location.");
            return true;
        }

        player.sendMessage("You do not have permission to use this command.");
        return true;
    }
}