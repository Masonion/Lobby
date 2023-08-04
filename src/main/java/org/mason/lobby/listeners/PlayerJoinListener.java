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

    private final Main plugin;
    private final ServerScoreboard serverScoreboard;

    public PlayerJoinListener(Main plugin, ServerScoreboard serverScoreboard) {
        this.plugin = plugin;
        this.serverScoreboard = serverScoreboard;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        giveClock(player);

        // Load spawn location from ConfigManager
        Location spawnLocation = plugin.getConfigManager().loadSpawnLocation();

        // Check if spawnLocation is null (not set)
        if (spawnLocation == null) {
            player.sendMessage(ChatColor.RED + "Spawn location is not set!");
            return;
        }

        // Teleport the player to spawn location
        player.teleport(spawnLocation);

        player.setWalkSpeed(0.2f);
        player.setAllowFlight(true);

        serverScoreboard.showServerScoreboard(player);
    }

    private void giveClock(Player player) {
        ItemStack clock = new ItemStack(Material.WATCH);
        ItemMeta clockMeta = clock.getItemMeta();
        clockMeta.setDisplayName(ChatColor.GREEN + "Server Selector");
        clock.setItemMeta(clockMeta);
        player.getInventory().setItem(4, clock);
    }
}