package org.mason.lobby.listeners;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.mason.lobby.Main;
import org.mason.lobby.util.ServerScoreboard;

import java.util.Random;

public class PlayerJoinListener implements Listener {

    private final Main Lobby;
    private final ServerScoreboard serverScoreboard;

    public PlayerJoinListener(Main Lobby, ServerScoreboard serverScoreboard) {
        this.Lobby = Lobby;
        this.serverScoreboard = serverScoreboard;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        giveClock(player);

        // Get "world" from the server
        World world = Bukkit.getServer().getWorld("world");

        // Check if the world is null
        if(world == null) {
            player.sendMessage(ChatColor.RED + "The world \"world\" does not exist!");
            return;
        }

        Location teleportLocation = new Location(world, 18, 94, -48);
        player.teleport(teleportLocation);

        teleportLocation.setYaw(0);
        player.teleport(teleportLocation);

        player.setWalkSpeed(0.2f);
        player.setAllowFlight(true);

      //  serverScoreboard.showServerScoreboard(player);
    }

    private void giveClock(Player player) {
        ItemStack clock = new ItemStack(Material.WATCH);
        ItemMeta clockMeta = clock.getItemMeta();
        clockMeta.setDisplayName(ChatColor.GREEN + "Server Selector");
        clock.setItemMeta(clockMeta);
        player.getInventory().setItem(4, clock);
    }
}
