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
import org.mason.lobby.Lobby;

import java.util.Random;

public class PlayerJoinListener implements Listener {

    private final Lobby Lobby;

    public PlayerJoinListener(Lobby Lobby) {
        this.Lobby = Lobby;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Location location = player.getLocation();

        giveClock(event.getPlayer());
        launchFireworksAroundPlayers(event.getPlayer());
        Location teleportLocation = new Location(location.getWorld(), -0.5, 101, 0.5);
        player.teleport(teleportLocation);

        teleportLocation.setYaw(180);
        player.teleport(teleportLocation);

        player.setWalkSpeed(0.2f);

    }

    private void giveClock(Player player) {
        ItemStack clock = new ItemStack(Material.WATCH);
        ItemMeta clockMeta = clock.getItemMeta();
        clockMeta.setDisplayName(ChatColor.GREEN + "Server Selector");
        clock.setItemMeta(clockMeta);
        player.getInventory().setItem(0, clock);
    }

    public void launchFireworksAroundPlayers(Player player) {
        if (player != null && !player.isDead()) {
            final Location location = player.getLocation();

            // Launch fireworks one by one in a circle around the player
            final int numFireworks = 4;
            final double initialRadius = 2;
            final double increment = (2 * Math.PI) / (numFireworks / 10);

            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    if (count >= numFireworks) {
                        cancel();
                        return;
                    }

                    double radius = initialRadius + count * 0.05;
                    double angle = count * increment;
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
            }.runTaskTimer(Lobby, 0L, 3L); // Launch a firework every 5 ticks (1/4 second)
        }
    }
}