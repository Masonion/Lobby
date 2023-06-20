package org.mason.lobby.commands;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.mason.lobby.Data.NPCDataStore;

import java.util.Iterator;

public class ListNPCsCommand implements CommandExecutor {

    private final Citizens citizens;
    private final NPCDataStore npcDataStore;

    public ListNPCsCommand(Citizens citizens, NPCDataStore npcDataStore) {
        this.citizens = citizens;
        this.npcDataStore = npcDataStore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        NPCRegistry npcRegistry = citizens.getNPCRegistry();
        Iterator<NPC> npcIterator = npcRegistry.iterator();

        sender.sendMessage(ChatColor.AQUA + "NPCs on the server:");
        while (npcIterator.hasNext()) {
            NPC npc = npcIterator.next();
            sender.sendMessage(ChatColor.GREEN + "ID: " + npc.getId() + ", Name: " + npc.getName());
        }

        return true;
    }
}