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
            // Check if the player is trying to start flying and if there is any solid block within 5 units below them
            boolean isBlockBelow = false;
            for (int i = 1; i <= 3; i++) {
                if (player.getLocation().clone().subtract(0, i, 0).getBlock().getType().isSolid()) {
                    isBlockBelow = true;
                    break;
                }
            }
            if (event.isFlying() && isBlockBelow) {
                event.setCancelled(true);
                player.setAllowFlight(false);
                player.setFlying(false);
                Vector jump = player.getLocation().getDirection().multiply(1.5).setY(1.5);
                player.setVelocity(player.getVelocity().add(jump));
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);

                if (player.isValid()) {
                    // schedule enabling of flight in next server tick so the player can double jump again when they land
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isValid()) {
                                player.setAllowFlight(true);
                            }
                        }
                    }.runTask(Lobby);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }
}