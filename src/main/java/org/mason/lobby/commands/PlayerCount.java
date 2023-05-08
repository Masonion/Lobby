package org.mason.lobby.commands;

import org.mason.lobby.Bungee;
import org.mason.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class PlayerCount implements CommandExecutor {

    private final Bungee bungee;
    private final Lobby lobby;

    public PlayerCount(Lobby lobby, Bungee bungee) {
        this.lobby = lobby;
        this.bungee = bungee;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("playercount")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                bungee.requestServerList();
                for (String server : bungee.getServerNames()) {
                    bungee.requestPlayerCount(server);
                }
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.GREEN + "Player counts for connected servers:");
                        for (Map.Entry<String, Integer> entry : bungee.getServers().entrySet()) {
                            player.sendMessage(ChatColor.GOLD + entry.getKey() + ": " + ChatColor.AQUA + entry.getValue());
                        }
                    }
                };
                Bukkit.getScheduler().runTaskLaterAsynchronously(lobby, runnable, 5L);
            }
        }
        return true;
    }
}
