package org.mason.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mason.lobby.util.Bungee;
import org.mason.lobby.Main;

public class ArenaCommand implements CommandExecutor {
    private final String arenaServerName = "Arena";
    private final Main lobby;

    public ArenaCommand(Main lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("arena")) {
                Bungee bungee = new Bungee(lobby); // create an instance of Bungee with Lobby instance
                bungee.sendPlayerToServer(player, arenaServerName); // call the method
                return true;
            }
        } else {
            sender.sendMessage("You must be a player to use this command!");
        }
        return false;
    }
}