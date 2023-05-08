package org.mason.lobby;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Bungee implements PluginMessageListener {

    private final Map<String, Integer> servers = new HashMap<>();
    private Set<String> serverNames;
    private final Lobby lobby;

    public Bungee(Lobby lobby) {
        this.lobby = lobby;
        this.serverNames = new HashSet<>(); // Initialize serverNames with an empty set
    }

    public void startTask() {
        // Schedule the task to run every 30 seconds
        Bukkit.getScheduler().runTaskTimerAsynchronously(lobby, new BukkitRunnable() {
            @Override
            public void run() {
                requestServerList();
                for (String server : serverNames) {
                    requestPlayerCount(server);
                }
            }
        }, 20L, 20L);
    }

    public void requestServerList() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetServers");
            Player player = Bukkit.getOnlinePlayers().iterator().next();
            player.sendPluginMessage(lobby, "BungeeCord", out.toByteArray());
        }
    }

    public void requestPlayerCount(String server) {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(server);
            Player player = Bukkit.getOnlinePlayers().iterator().next();
            player.sendPluginMessage(lobby, "BungeeCord", out.toByteArray());
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

            try {
                String subChannel = in.readUTF();
                if (subChannel.equals("GetServers")) {
                    String[] serverArray = in.readUTF().split(", ");
                    serverNames = Set.of(serverArray); // Update serverNames with the new set of server names
                } else if (subChannel.equals("PlayerCount")) {
                    String server = in.readUTF();
                    int count = in.readInt();
                    servers.put(server, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(lobby, "BungeeCord", out.toByteArray());
    }


    public Set<String> getServerNames() {
        return serverNames;
    }

    public Map<String, Integer> getServers() {
        return servers;
    }
}
