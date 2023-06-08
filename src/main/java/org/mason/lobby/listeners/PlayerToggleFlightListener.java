package org.mason.lobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;
import org.mason.lobby.Main;
import org.bukkit.Sound;
import org.bukkit.Effect;



import org.bukkit.scheduler.BukkitRunnable;

public class PlayerToggleFlightListener implements Listener {

    private final Main Lobby;

    public PlayerToggleFlightListener(Main Lobby) {
        this.Lobby = Lobby;
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        GameMode gameMode = player.getGameMode();

        if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
            if (event.isFlying()) {
                event.setCancelled(true);
                player.setAllowFlight(false);
                player.setFlying(false);
                // Double the X and Z directions (for further) and Y direction (for higher)
                Vector jump = player.getLocation().getDirection().multiply(1).setY(1);
                player.setVelocity(player.getVelocity().add(jump));

                // Play the WOOSH sound
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isValid()) {
                            player.setAllowFlight(true);
                        }
                    }
                }.runTaskLater(this.Lobby, 10L);
            }
        }
    }
}
