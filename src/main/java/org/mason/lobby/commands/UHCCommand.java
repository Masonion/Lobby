package org.mason.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mason.lobby.Bungee;
import org.mason.lobby.Lobby;

public class UHCCommand implements CommandExecutor {

    private final Bungee bungee;
    private final Lobby lobby;

    public UHCCommand(Lobby lobby, Bungee bungee) {
        this.lobby = lobby;
        this.bungee = bungee;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("uhc")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                bungee.sendPlayerToServer(player, "UHCCommand");
            }
        }
        return true;
    }
}
