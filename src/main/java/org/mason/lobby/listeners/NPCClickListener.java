package org.mason.lobby.listeners;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.mason.lobby.util.Bungee;
import org.mason.lobby.Data.NPCDataStore;

public class NPCClickListener implements Listener {

    private final Bungee bungee;
    private final NPCDataStore npcDataStore;

    public NPCClickListener(Bungee bungee, NPCDataStore npcDataStore) {
        this.bungee = bungee;
        this.npcDataStore = npcDataStore;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (CitizensAPI.getNPCRegistry().isNPC(event.getRightClicked())) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getRightClicked());

            // Get the server name from the NPC's data
            String serverName = npcDataStore.getServerName(npc.getId());

            Player player = event.getPlayer();

            bungee.sendPlayerToServer(player, serverName);
        }
    }
}
