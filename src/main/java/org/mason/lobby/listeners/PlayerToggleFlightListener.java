package org.mason.lobby.listeners;

import net.akurra.akurrachat.AkurraChat;
import net.akurra.akurrachat.PlayerSettings;
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

    public PlayerToggleFlightListener() {
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        GameMode gameMode = player.getGameMode();

        if (player.hasPermission("akurra.supporter")) {
            PlayerSettings settings = AkurraChat.getInstance().getPlayerSettings(player.getUniqueId());
            if (settings.getFly()) {
                return;
            }
        }

        if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
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
            } else {
                event.setCancelled(true);
            }
        }
    }
}