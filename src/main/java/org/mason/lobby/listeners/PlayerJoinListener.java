package org.mason.lobby.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
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
        Location location = player.getLocation();

        giveClock(player);
        launchFireworksAroundPlayers(player);
        Location teleportLocation = new Location(location.getWorld(), 5, 87, 29);
        player.teleport(teleportLocation);

        teleportLocation.setYaw(180);
        player.teleport(teleportLocation);

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

    public void launchFireworksAroundPlayers(Player player) {

        // Start a BukkitRunnable with delay to fetch the player's location
        new BukkitRunnable() {
            @Override
            public void run() {

                // Define spiral parameters
                final int numFireworks = 10;
                final double radiusIncrement = 0.2; // Increase this to widen the spiral
                final double angleIncrement = (2 * Math.PI) / numFireworks;
                final Location location = player.getLocation();

                new BukkitRunnable() {

                    int count = 0;

                    @Override
                    public void run() {
                        if (count >= numFireworks) {
                            cancel();
                            return;
                        }

                        double angle = count * angleIncrement;
                        // The radius now increases with each firework, creating a spiral
                        double radius = count * radiusIncrement;
                        double x = location.getX() + (radius * Math.cos(angle));
                        double z = location.getZ() + (radius * Math.sin(angle));
                        Location fireworkLocation = new Location(location.getWorld(), x, location.getY() + 1, z);
                        Firework firework = location.getWorld().spawn(fireworkLocation, Firework.class);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();

                        // Randomize the color
                        Color randomColor = Color.fromRGB(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));

                        fireworkMeta.addEffect(FireworkEffect.builder().withColor(randomColor).with(FireworkEffect.Type.BURST).withFlicker().build());
                        fireworkMeta.setPower(1);
                        firework.setFireworkMeta(fireworkMeta);
                        count++;
                    }
                }.runTaskTimer(Lobby, 0L, 3L); // Launch a firework every 3 ticks
            }
        }.runTaskLater(Lobby, 15L); // Delay before getting the player's location
    }
}
