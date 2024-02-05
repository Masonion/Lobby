package org.mason.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mason.lobby.util.Bungee;
import org.mason.lobby.Main;

public class UHCCommand implements CommandExecutor {
    private final String uhcServerName = "UHC";
    private final Main lobby;

    public UHCCommand(Main lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("uhc")) {
                Bungee bungee = new Bungee(lobby);
                bungee.sendPlayerToServer(player, uhcServerName);
                return true;
            }
        } else {
            sender.sendMessage("You must be a player to use this command!");
        }
        return false;
    }
}