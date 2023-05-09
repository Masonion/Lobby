package org.mason.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mason.lobby.Bungee;
import org.mason.lobby.Lobby;

public class ArenaCommand implements CommandExecutor {

    private final Bungee bungee;
    private final Lobby lobby;

    public ArenaCommand(Lobby lobby, Bungee bungee) {
        this.lobby = lobby;
        this.bungee = bungee;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("arena")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                bungee.sendPlayerToServer(player, "arena");
            }
        }
        return true;
    }
}
