package org.mason.lobby.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (location.getY() < 0) {
            Location teleportLocation = new Location(location.getWorld(), -0.5, 100, 0.5);
            player.teleport(teleportLocation);

            teleportLocation.setYaw(180);
            player.teleport(teleportLocation);
        }
        if (!player.getAllowFlight() && player.isOnGround()) {
            player.setAllowFlight(true);
        }
    }
}