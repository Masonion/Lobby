package org.mason.lobby.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.mason.lobby.Main;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Bungee implements PluginMessageListener {

    private final Map<String, Integer> servers = new HashMap<>();
    private final Main lobby;

    public Bungee(Main lobby) {
        this.lobby = lobby;
        lobby.getServer().getMessenger().registerOutgoingPluginChannel(lobby, "BungeeCord");
        lobby.getServer().getMessenger().registerIncomingPluginChannel(lobby, "BungeeCord", this);
    }

    public void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(lobby, "BungeeCord", out.toByteArray());
    }

    public void getPlayerCount(String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(serverName);
        lobby.getServer().sendPluginMessage(lobby, "BungeeCord", out.toByteArray());
    }

    public int getServerPlayerCount(String serverName) {
        return servers.getOrDefault(serverName, 0);
    }

    public int getTotalPlayerCount() {
        return servers.getOrDefault("ALL", 0);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String subChannel = in.readUTF();

            if (subChannel.equals("PlayerCount")) {
                String server = in.readUTF();
                int playerCount = in.readInt();
                servers.put(server, playerCount);

                if (server.equals("ALL")) {
                    servers.put("ALL", playerCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlayerCountTask(String... serverNames) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String serverName : serverNames) {
                    getPlayerCount(serverName);
                }
                getPlayerCount("ALL");
            }
        }.runTaskTimer(lobby, 0L, 20L);
    }
}