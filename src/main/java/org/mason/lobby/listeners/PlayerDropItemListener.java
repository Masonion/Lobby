package org.mason.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.mason.lobby.Main;

public class PlayerDropItemListener implements Listener {

    private final Main Lobby;

    public PlayerDropItemListener(Main Lobby) {
        this.Lobby = Lobby;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}